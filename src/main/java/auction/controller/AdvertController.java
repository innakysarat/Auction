package auction.controller;

import auction.model.Advert;
import auction.model.BuyerAdvert;
import auction.service.AdvertService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/adverts")
@AllArgsConstructor
public class AdvertController {
    private final AdvertService advertService;

    @CrossOrigin
    @PostMapping
    public void createAdvert(@RequestBody Advert advert,
                             @RequestParam("seller_id") Long seller_id) {
        advertService.createAdvert(advert, seller_id);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public void addPrice(@RequestParam("buyer_id") Long buyer_id,
                         @RequestParam("price") double price,
                         @PathVariable Long id) {
        advertService.addPrice(buyer_id, id, price);
    }
    @CrossOrigin
    @GetMapping("/{id}")
    public Advert getAdvert(@PathVariable("id") Long advert_id){
        return advertService.getAdvert(advert_id);
    }

    @CrossOrigin
    @GetMapping("/{id}/prices")
    public List<BuyerAdvert> getAdvertsPrices(@PathVariable("id") Long advert_id){
        return advertService.getAdvertsPrices(advert_id);
    }
}
