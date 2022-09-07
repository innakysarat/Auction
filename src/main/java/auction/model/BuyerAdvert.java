package auction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "adverts_prices"
)
@Getter
@Setter
@NoArgsConstructor
public class BuyerAdvert {
    // @EmbeddedId
    // BuyerAdvertKey id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            updatable = false
    )
    Long id;
//    @ManyToOne
//    //@MapsId("userId")
//    @JoinColumn(name = "buyer_id")
//    User buyer;
//
//    @ManyToOne
//    //@MapsId("advertId")
//    @JoinColumn(name = "advert_id")
//    Advert advert;

    @Column
    Long buyerId;
    @Column
    Long advertId;
    @Column
    double price;

    public BuyerAdvert(Long buyerId, Long advertId, double price) {
        this.price = price;
        this.buyerId = buyerId;
        this.advertId = advertId;
    }

}
