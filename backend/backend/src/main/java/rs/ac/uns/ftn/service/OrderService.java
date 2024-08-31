package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderService {

    String createOrder(String loggedUserEmail);

    Orderr cancelOrder(String loggedUserEmail, Long orderId);

    List<Orderr> getAll();

    Orderr getByIdWithImages(Long id);

    Optional<Orderr> getById(Long id);

    void save(Orderr order);

    List<Orderr> getPendingOrders();

    Orderr sentOrder(Long orderId);

    Orderr deliverOrder(Long orderId);

    List<Orderr> getSentOrders();

}
