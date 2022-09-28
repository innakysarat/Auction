package auction.service;

import auction.model.Advert;
import auction.model.BuyerAdvert;
import auction.model.User;
import auction.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class AdvertService {
    private final AdvertRepository advertRepository;
    private final UserRepository userRepository;
    private final AdvertPricesRepository advertPricesRepository;
    private final LocationDao locationDao;
    private final CategoryDao categoryDao;

    public void createAdvert(Advert advert, String username) {
        User seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Seller with such username not found"));
        advert.setSeller(seller);
        seller.addSellersAdvert(advert);
        locationDao.addLocation(advert.getLocation());
        categoryDao.addCategory(advert.getCategory());
        advertRepository.save(advert);
        userRepository.save(seller);
    }

    public ResponseEntity<Advert> addPrice(String username, Long advert_id, Long price) {
        User buyer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only authorized user can offer price for the item"));
        Long buyer_id = buyer.getUserId();
        BuyerAdvert newPrice = new BuyerAdvert(buyer_id, advert_id, price);
        advertPricesRepository.save(newPrice);
        return updatePrice(advert_id, price);
    }

    public Advert getAdvert(Long advert_id) {
        return advertRepository.findById(advert_id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Advert not found"
                ));
    }

    public List<BuyerAdvert> getAdvertsPrices(Long advert_id) {
        return advertPricesRepository.findAllByAdvertId(advert_id);
    }

    public List<Advert> getAdverts() {
        return advertRepository.findAll();
    }

    public void deleteAdvert(Long id, String username) {
        Advert advert = advertRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advert not found"));
        if (Objects.equals(advert.getSeller().getUsername(), username)) {
            advertRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Advert can be deleted only by its seller");
        }
    }

    public boolean primaryCheckUpdate(String prevField, String newField) {
        return newField != null && newField.length() > 0 && !Objects.equals(prevField, newField)
                || prevField == null;
    }

    public ResponseEntity<Advert> updatePrice(Long advertId, Long price) {
        Advert advert = advertRepository.findById(advertId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advert not found"));
        if (price > advert.getPrice()) {
            advert.setPrice(price);
        }
        return ResponseEntity.ok(this.advertRepository.save(advert));
    }

    public ResponseEntity<Advert> updateAdvert(Long id, Advert advert, String username) {
        Advert prevAdvert = advertRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advert not found"));
        if (Objects.equals(prevAdvert.getSeller().getUsername(), username)) {
            if (primaryCheckUpdate(prevAdvert.getCategory(), advert.getCategory())) {
                prevAdvert.setCategory(advert.getCategory());
            }
            if (primaryCheckUpdate(prevAdvert.getDescription(), advert.getDescription())) {
                prevAdvert.setDescription(advert.getDescription());
            }
            if (primaryCheckUpdate(prevAdvert.getLocation(), advert.getLocation())) {
                prevAdvert.setLocation(advert.getLocation());
            }
            if (primaryCheckUpdate(prevAdvert.getAdvertImageLink(), advert.getAdvertImageLink())) {
                prevAdvert.setAdvertImageLink(advert.getAdvertImageLink());
            }
            if (primaryCheckUpdate(prevAdvert.getTitle(), advert.getTitle())) {
                prevAdvert.setTitle(advert.getTitle());
            }

            return ResponseEntity.ok(this.advertRepository.save(prevAdvert));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The advert can be modified only by its seller");
        }
    }

    public User getSeller(Long advertId) {
        Advert advert = advertRepository.findById(advertId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advert not found"));
        return advert.getSeller();
    }

    public Set<String> getLocations() {
        return locationDao.getLocations();
    }

    public Set<String> getCategories() {
        return categoryDao.getCategories();
    }
}
