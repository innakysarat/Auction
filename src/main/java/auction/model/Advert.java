package auction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Advert")
@Table(
        name = "adverts"
)
@Getter
@Setter
@NoArgsConstructor
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            updatable = false
    )
    private long advertId;

    @Column
    private String name;

    @Column
    private String location;
    @Column
    private String description;
    @Column
    private String advertImageLink;

    // зачем хранить инфу про объявление??
//    @OneToMany(mappedBy = "advert")
//    Set<BuyerAdvert> prices;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

    public Advert(String name, String location, String description, String advertImageLink) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.advertImageLink = advertImageLink;
    }

//    public BuyerAdvert addBuyersPrice(User buyer, BuyerAdvert newPrice){
//        //BuyerAdvert newPrice = new BuyerAdvert(buyer, this, price);
//        prices.add(newPrice);
//        buyer.getBuyers_adverts().add(newPrice);
//        return newPrice;
//    }

}
