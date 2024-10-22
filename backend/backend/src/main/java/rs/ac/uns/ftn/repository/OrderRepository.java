package rs.ac.uns.ftn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.OrderStatus;
import rs.ac.uns.ftn.model.Orderr;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Orderr, Long> {

    Page<Orderr> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Orderr o WHERE o.status = 'Processing' AND o.creationTime < :oneDayAgo")
    Page<Orderr> findPendingOrders(@Param("oneDayAgo") LocalDateTime oneDayAgo, Pageable pageable);

    Optional<Orderr> findByCode(String code);

    @Query("SELECT o FROM Orderr o WHERE o.user.email = :email ORDER BY o.creationTime DESC")
    Set<Orderr> findOrdersByUserEmail(@Param("email") String email);

    @Query("SELECT o FROM Orderr o WHERE o.status = 'Processing' AND o.creationTime < :oneDayAgo AND o.code LIKE %:code%")
    Set<Orderr> findPendingOrdersBySearch(@Param("code") String code, @Param("oneDayAgo") LocalDateTime oneDayAgo);

    @Query("SELECT o FROM Orderr o WHERE o.status = 'Sent' AND o.code LIKE %:code%")
    Set<Orderr> findSentOrdersBySearch(@Param("code") String code);
}
