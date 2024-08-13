package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    String createOrder(String loggedUserEmail);

    Orderr cancelOrder(String loggedUserEmail, Long orderId);

    List<Orderr> getAll();

    List<Orderr> getByUser(String loggedUserEmail);

    Optional<Orderr> getById(Long id);

    void save(Orderr order);
}
