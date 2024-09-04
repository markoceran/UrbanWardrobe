package rs.ac.uns.ftn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderService {

    String createOrder(String loggedUserEmail);

    Orderr cancelOrder(String loggedUserEmail, Long orderId);

    List<Orderr> getAll();

    Orderr getByCodeWithImages(String code);

    Optional<Orderr> getById(Long id);

    void save(Orderr order);

    Orderr sentOrder(Long orderId);

    Orderr deliverOrder(Long orderId);

    Page<Orderr> getSentOrders(Pageable pageable);

    Page<Orderr> getPendingOrders(Pageable pageable);

    Page<Orderr> getDeliveredOrders(Pageable pageable);

}
