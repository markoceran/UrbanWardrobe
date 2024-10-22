package rs.ac.uns.ftn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("product-sizeQuantities")
    private Set<SizeQuantity> sizeQuantities;

    @Column(name = "price",nullable = false)
    private double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @PrePersist
    protected void onCreate() {
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

    @Transient
    private List<String> imagesName;
    @Transient
    private boolean inWishlist = false;
    @Transient
    private boolean outOfStock = false;

}
