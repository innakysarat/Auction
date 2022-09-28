package auction.service;

import auction.model.Advert;
import auction.model.BuyerAdvert;
import auction.model.User;
import auction.model.UserRole;
import auction.repository.AdvertPricesRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import auction.repository.UserRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdvertPricesRepository advertPricesRepository;
    private final PasswordEncoder passwordEncoder;
    private final static String USER_NOT_FOUND = "user with username %s not found";
    private final static String USERNAME_IS_TAKEN = "user with username %s already exists";

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    public void createUser(User user) throws Exception {
        String username = user.getUsername();
        Optional<User> userExists = userRepository.findByUsername(username);
        if (userExists.isPresent()) {
            throw new Exception("Username is taken");
        } else {
            if (username.endsWith("admin")) {
                user.setUserRole(UserRole.ADMIN);
                user.setGrantedAuthorities(UserRole.ADMIN.getGrantedAuthorities());
            } else if (username.endsWith("seller")) {
                user.setUserRole(UserRole.SELLER);
                user.setGrantedAuthorities(UserRole.SELLER.getGrantedAuthorities());
            } else {
                user.setUserRole(UserRole.BUYER);
                user.setGrantedAuthorities(UserRole.BUYER.getGrantedAuthorities());
            }
            userRepository.save(user);
        }
    }

    public boolean primaryCheckUpdate(String newField, String prevField) {
        return newField != null && newField.length() > 0 && !Objects.equals(prevField, newField);
    }

    public ResponseEntity<User> updateUser(String username, Long userId, User userUpdated) {
        User userPrevious = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (Objects.equals(username, userPrevious.getUsername())) {
            if (primaryCheckUpdate(userUpdated.getUsername(), userPrevious.getUsername())) {
                Optional<User> userByUsername = userRepository.findByUsername(userUpdated.getUsername());
                if (userByUsername.isPresent()) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, USERNAME_IS_TAKEN
                    );
                }
                userPrevious.setUsername(userUpdated.getUsername());
            }
            if (primaryCheckUpdate(userUpdated.getFirstName(), userPrevious.getFirstName())) {
                userPrevious.setFirstName(userUpdated.getFirstName());
            }
            if (primaryCheckUpdate(userUpdated.getLastName(), userPrevious.getLastName())) {
                userPrevious.setLastName(userUpdated.getLastName());
            }
            if (primaryCheckUpdate(userUpdated.getEmail(), userPrevious.getEmail())) {
                User userByEmail
                        = userRepository.findByEmail(userUpdated.getEmail());
                if (userByEmail != null) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Email is taken"
                    );
                }
                userPrevious.setEmail(userUpdated.getEmail());
            }
            if (primaryCheckUpdate(userUpdated.getPhoneNumber(), userPrevious.getPhoneNumber())) {
                User userByPhone = userRepository.findByPhoneNumber(userUpdated.getPhoneNumber());
                if (userByPhone != null) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Phone number is taken"
                    );
                }
                userPrevious.setPhoneNumber(userUpdated.getPhoneNumber());
            }
            if (primaryCheckUpdate(userUpdated.getLocation(), userPrevious.getLocation())) {
                userPrevious.setLocation(userUpdated.getLocation());
            }
            if (primaryCheckUpdate(userUpdated.getUserImageLink(), userPrevious.getUserImageLink())){
                userPrevious.setUserImageLink(userUpdated.getUserImageLink());
            }
            return ResponseEntity.ok(userRepository.save(userPrevious));
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Another user cannot update someone's else info");
        }

    }
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
        String password = passwordEncoder.encode(user.getPassword());
        String role = user.getUserRole().toString();
        if (role.equals("ADMIN")) {
            user.setGrantedAuthorities(UserRole.ADMIN.getGrantedAuthorities());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), password, UserRole.ADMIN.getGrantedAuthorities());
        } else if (role.equals("SELLER")) {
            user.setGrantedAuthorities(UserRole.SELLER.getGrantedAuthorities());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), password, UserRole.SELLER.getGrantedAuthorities());
        }
        user.setGrantedAuthorities(UserRole.BUYER.getGrantedAuthorities());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), password, UserRole.BUYER.getGrantedAuthorities());
    }

    public Set<Advert> getSellersAdverts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getSellers_adverts();
    }
    public List<BuyerAdvert> getBuyersAdverts(Long buyer_id) {
        return advertPricesRepository.findAllByBuyerId(buyer_id);
    }

    public void deleteUser(Long id) {
        boolean exists = userRepository.existsById(id);
        if (exists){
            userRepository.deleteById(id);
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found"
            );
        }
    }
}
