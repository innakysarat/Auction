package auction.controller;

import auction.model.Advert;
import auction.model.BuyerAdvert;
import auction.model.User;
import auction.service.AdvertService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RestController
@RequestMapping("api/adverts")
@AllArgsConstructor
public class AdvertController {
    private final AdvertService advertService;

    @PostMapping
    @PreAuthorize("hasAuthority('advert:add')")
    public void createAdvert(@RequestBody Advert advert) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            advertService.createAdvert(advert, username);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Advert cannot be added by anonymous user");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('advert:edit')")
    public ResponseEntity<Advert> updateAdvert(@PathVariable(value = "id") Long id,
                                               @Validated @RequestBody Advert advert) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            return advertService.updateAdvert(id, advert, username);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Advert can be modified only by its seller");
        }
    }

    @GetMapping("/{id}")
    public Advert getAdvert(@PathVariable("id") Long advert_id) {
        return advertService.getAdvert(advert_id);
    }

    @GetMapping
    public List<Advert> getAdverts() {
        return advertService.getAdverts();
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAuthority('advert:delete')")
    public void deleteAdvert(@PathVariable Long id) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        advertService.deleteAdvert(id, username);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @PutMapping("/price/{id}")
    //@PreAuthorize("hasAuthority('add price')")
    public ResponseEntity<Advert> addPrice(@RequestParam Long price,
                                           @PathVariable Long id) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        return advertService.addPrice(username, id, price);
    }

    @GetMapping("/{id}/seller")
    public User getSeller(@PathVariable Long id) {
        return advertService.getSeller(id);
    }


    @GetMapping("/{id}/prices")
    @PreAuthorize("hasAuthority('advert:add')")
    public List<BuyerAdvert> getAdvertsPrices(@PathVariable("id") Long advert_id) {
        return advertService.getAdvertsPrices(advert_id);
    }

    @GetMapping("/locations")
    public Set<String> getLocations(){
        return advertService.getLocations();
    }
    @GetMapping("/categories")
    public Set<String> getCategories(){
        return advertService.getCategories();
    }

}
