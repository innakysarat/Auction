package auction.service;

import auction.model.Advert;
import auction.model.BuyerAdvert;
import auction.model.User;
import auction.repository.AdvertPricesRepository;
import auction.repository.AdvertRepository;
import auction.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AdvertService {
    private final AdvertRepository advertRepository;
    private final UserRepository userRepository;
    private final AdvertPricesRepository advertPricesRepository;

    public void createAdvert(Advert advert, Long seller_id) {
       User seller = userRepository.findById(seller_id)
               .orElseThrow(() -> new ResponseStatusException(
                       HttpStatus.BAD_REQUEST, "Seller with such id NOT FOUND"));
        advert.setSeller(seller);
        seller.addSellersAdvert(advert);
        advertRepository.save(advert);
        userRepository.save(seller);
        // update seller in repository??
    }

    public void addPrice(Long buyer_id, Long advert_id, double price) {
        BuyerAdvert newPrice = new BuyerAdvert(buyer_id, advert_id, price);
        advertPricesRepository.save(newPrice);
    }

    public Advert getAdvert(Long advert_id) {
        return advertRepository.findById(advert_id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Advert NOT FOUND"
                ));
    }

    public List<BuyerAdvert> getAdvertsPrices(Long advert_id) {
        return advertPricesRepository.findAllByAdvertId(advert_id);
    }

}
