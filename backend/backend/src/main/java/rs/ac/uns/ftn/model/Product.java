package rs.ac.uns.ftn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Random;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code",nullable = false, unique = true)
    private String code;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description",nullable = false)
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SizeQuantity> sizeQuantities;

    @Column(name = "price",nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "wishList_id")
    private WishList wishList;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @PrePersist
    protected void onCreate() {
        price = 0;
        code = generateCode();
    }

    private String generateCode() {
        String prefix = "URB";
        StringBuilder codeBuilder = new StringBuilder(prefix);
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int digit = random.nextInt(10);
            codeBuilder.append(digit);
        }
        return codeBuilder.toString();
    }


}
