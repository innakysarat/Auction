package auction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "adverts_prices"
)
@Getter
@Setter
@NoArgsConstructor
public class BuyerAdvert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            updatable = false
    )
    private Long id;
    @Column
    private Long buyerId;
    @Column
    private Long advertId;
    @Column
    private double price;
    @Column
    private Date date;

    public BuyerAdvert(Long buyerId, Long advertId, double price) {
        this.price = price;
        this.buyerId = buyerId;
        this.advertId = advertId;
    }

}
