package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.OrderStatus;
import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.User;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Orderr, Long> {
    List<Orderr> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Orderr o WHERE o.user.email = :email ORDER BY o.creationTime DESC")
    Set<Orderr> findOrdersByUserEmail(@Param("email") String email);
}
