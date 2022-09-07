package auction.controller;

import auction.model.Advert;
import auction.model.BuyerAdvert;
import auction.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import auction.service.UserService;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

   // @CrossOrigin
    @CrossOrigin(origins = "http://localhost:3000")
    //@CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }
    //@CrossOrigin
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{username}")
    public User getUser(@PathVariable(value = "username") String username) {
        return userService.getUser(username);
    }


   // @CrossOrigin
    @PostMapping
    public void createUser(@RequestBody User user) throws Exception {
        userService.createUser(user);
    }

   // @CrossOrigin
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('advert:add')")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @Validated @RequestBody User user) {
        return userService.updateUser(userId, user);
    }
   // @CrossOrigin
    @GetMapping("/seller/adverts/{id}")
    public Set<Advert> getSellersAdverts(@PathVariable(value= "id") Long userId){
        return userService.getSellersAdverts(userId);
    }
   // @CrossOrigin
    @GetMapping("/adverts/{id}")
    public List<BuyerAdvert> getBuyersAdverts(@PathVariable("id") Long buyer_id){
        return userService.getBuyersAdverts(buyer_id);
    }
}
