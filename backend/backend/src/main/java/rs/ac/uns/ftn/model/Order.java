package rs.ac.uns.ftn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creationTime",nullable = false)
    private LocalDate creationTime;

    @Column(name = "estimatedDeliveryTime",nullable = false)
    private LocalDate estimatedDeliveryTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BasketItem> basketItems;
    @ManyToOne
    @JoinColumn(name = "user_id")
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
        creationTime = LocalDate.now();  // Automatically set the order date when a new order is created
        estimatedDeliveryTime = creationTime.plusDays(4);
        productsAmount = 0;
        shippingAmount = 0;
        totalAmount = 0;
        status = OrderStatus.Processing;
    }

}
