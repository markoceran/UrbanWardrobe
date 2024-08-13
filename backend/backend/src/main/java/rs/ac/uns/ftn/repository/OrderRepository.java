package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.User;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orderr, Long> {
    List<Orderr> findByUser(User user);
}
