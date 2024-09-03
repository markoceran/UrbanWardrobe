package rs.ac.uns.ftn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.OrderStatus;
import rs.ac.uns.ftn.model.Orderr;

import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Orderr, Long> {

    Page<Orderr> findByStatus(OrderStatus status, Pageable pageable);

    Optional<Orderr> findByCode(String code);

    @Query("SELECT o FROM Orderr o WHERE o.user.email = :email ORDER BY o.creationTime DESC")
    Set<Orderr> findOrdersByUserEmail(@Param("email") String email);
}
