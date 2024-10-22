package rs.ac.uns.ftn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.model.dto.UserDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orderr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code",nullable = false, unique = true)
    private String code;

    @Column(name = "creationTime",nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "estimatedDeliveryTime",nullable = false)
    private LocalDate estimatedDeliveryTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("order-basketItems")
    private Set<BasketItem> basketItems;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-orders")
    private User user;

    @Column(name = "productsAmount",nullable = false)
    private double productsAmount;

    @Column(name = "shippingAmount",nullable = false)
    private double shippingAmount;

    @Column(name = "totalAmount",nullable = false)
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @PrePersist
    protected void onCreate() {
        creationTime = LocalDateTime.now();  // Automatically set the order date when a new order is created
        estimatedDeliveryTime = LocalDate.from(creationTime.plusDays(4));
        status = OrderStatus.Processing;
        code = generateCode();
    }

    private String generateCode() {
        String prefix = "ORD";
        StringBuilder codeBuilder = new StringBuilder(prefix);
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int digit = random.nextInt(10);
            codeBuilder.append(digit);
        }
        return codeBuilder.toString();
    }

    @Transient
    private UserDTO userDTO;

}
