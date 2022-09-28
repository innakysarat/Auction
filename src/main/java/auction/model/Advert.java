package auction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
    private long id;

    @Column
    private String title;

    @Column
    private String location;
    @Column
    private String description;
    @Column
    private String advertImageLink;

    @Column
    private Long rating;

    @Column
    private Long price;

    @Column
    private String category;

    @Column
    private Date end_date;

    @Column
    private Date extension_period;
    @Column
    private String hash_image;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

}
