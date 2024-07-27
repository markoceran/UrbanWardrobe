package rs.ac.uns.ftn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("U")
public class User extends BasicUser{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private ShippingAddress shippingAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basket_id", referencedColumnName = "id")
    private Basket basket;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wishlist_id", referencedColumnName = "id")
    private WishList wishList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Orderr> orders;

    @PrePersist
    protected void onCreate() {
        if (basket == null) {
            basket = new Basket();
            basket.setUser(this);
        }

        if (wishList == null) {
            wishList = new WishList();
            wishList.setUser(this);
        }

        if (orders == null) {
            orders = new HashSet<>();
        }

        setRole(Role.USER);

    }

}
