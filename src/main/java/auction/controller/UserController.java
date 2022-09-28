package auction.controller;

import auction.model.Advert;
import auction.model.BuyerAdvert;
import auction.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import auction.service.UserService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public User getUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        User user;
        if (!Objects.equals(username, "anonymousUser")) {
            user = userService.getUser(username);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }
    @PostMapping
    public void createUser(@RequestBody User user) throws Exception {
        userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @Validated @RequestBody User user) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        return userService.updateUser(username, userId, user);
    }

    @GetMapping("/seller/adverts/{id}")
    public Set<Advert> getSellersAdverts(@PathVariable(value = "id") Long userId) {
        return userService.getSellersAdverts(userId);
    }

    @GetMapping("/adverts/{id}")
    public List<BuyerAdvert> getBuyersAdverts(@PathVariable("id") Long buyer_id) {
        return userService.getBuyersAdverts(buyer_id);
    }
    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            userService.deleteUser(id);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User can be deleted only by himself");
        }
    }

}
