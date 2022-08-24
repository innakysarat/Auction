package auction.service;

import auction.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import auction.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new Exception("User not found");
        }
    }

    public void createUser(User user) throws Exception {
        User userExists = userRepository.findByUsername(user.getUsername());
        if (userExists != null) {
            throw new Exception("Username is taken");
        }
        userRepository.save(user);
    }

    public ResponseEntity<User> updateUser(Long userId, User userUpdated) {
        User userPrevious = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String newFirstName = userUpdated.getFirstName();
        String newLastName = userUpdated.getLastName();
        String newEmail = userUpdated.getEmail();
        if (newFirstName != null &&
                newFirstName.length() > 0 &&
                !Objects.equals(userPrevious.getFirstName(), newFirstName)) {
            userUpdated.setFirstName(userUpdated.getFirstName());
        }
        if (newLastName != null &&
                newLastName.length() > 0 &&
                !Objects.equals(userPrevious.getLastName(), newLastName)) {
            userPrevious.setLastName(newLastName);
        }
        if (newEmail != null &&
                newEmail.length() > 0 &&
                !Objects.equals(userPrevious.getEmail(), newEmail)) {
            User userByEmail
                    = userRepository.findByEmail(newEmail);
            if (userByEmail != null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Email is taken"
                );
            }
            userPrevious.setEmail(newEmail);
        }
        return ResponseEntity.ok(this.userRepository.save(userPrevious));

    }
}
