package rs.ac.uns.ftn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "size", nullable = false)
    private Size size;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    @JsonBackReference
    private Basket basket;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Orderr order;

    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

    @Transient
    private boolean sizeOnStock = true;
    @Transient
    private boolean haveEnoughOnStock = true;
}
