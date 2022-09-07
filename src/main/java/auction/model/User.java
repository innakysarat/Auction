package auction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "User")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_unique", columnNames = "username")
                , @UniqueConstraint(name = "email_unique", columnNames = "email")
                , @UniqueConstraint(name = "phoneNumber_unique", columnNames = "phoneNumber")
        }
)
@Getter
@Setter
@NoArgsConstructor
//@EqualsAndHashCode
public class User implements UserDetails {

    public User(String username, String password, String firstName, String lastName, String email, String phoneNumber, String role, String userImageLink) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userImageLink = userImageLink;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            updatable = false
    )
    private long userId;

    @Column
    private String username;
    //@Transient
    @Column
    private String password;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String phoneNumber;

    @Column
    private String location;
    @Column
    private Boolean isVerified;
    @Column
    private String userImageLink;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @JsonIgnore
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Advert> sellers_adverts = new HashSet<>();

//    @JsonIgnore
//    buyer's adverts for which he offered a price
//    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private final Set<BuyerAdvert> buyers_adverts = new HashSet<>();

    public void addSellersAdvert(Advert advert){
        sellers_adverts.add(advert);
    }

    @Transient
    private Boolean locked = false;
    @Transient
    private Boolean enabled = false;
    @Transient
    private Set<? extends GrantedAuthority> grantedAuthorities;
    // @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority =
//                new SimpleGrantedAuthority(userRole.name());
//        return Collections.singletonList(authority);
//    }
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (userRole.toString().equals("ADMIN"))
            return UserRole.ADMIN.getGrantedAuthorities();
        if (userRole.toString().equals("SELLER")) {
            return UserRole.SELLER.getGrantedAuthorities();
        } else {
            return UserRole.BUYER.getGrantedAuthorities();
        }
    }

    public void setGrantedAuthorities(Set<? extends GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // list of adverts for seller
    // list of adverts for buyer
    // favourite list of adverts for buyer
}
