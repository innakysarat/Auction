package auction.repository;

import auction.model.Advert;
import auction.model.BuyerAdvert;
import auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AdvertPricesRepository extends JpaRepository<BuyerAdvert, Long> {
    @Query("SELECT s FROM BuyerAdvert s WHERE s.advertId = ?1")
    List<BuyerAdvert> findAllByAdvertId(Long advertId);
    @Query("SELECT s FROM BuyerAdvert s WHERE s.buyerId = ?1")
    List<BuyerAdvert> findAllByBuyerId(Long buyerId);

}
