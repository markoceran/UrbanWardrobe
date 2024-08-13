package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.User;

import java.util.List;

public interface OrderService {

    String createOrder(String loggedUserEmail);

    List<Orderr> getAll();

    List<Orderr> getByUser(String loggedUserEmail);

    void save(Orderr order);
}
