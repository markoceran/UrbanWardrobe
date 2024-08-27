package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.ShippingAddress;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.dto.BasketDTO;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.model.dto.WishlistDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User findByEmail(String email);

    User createUser(UserDTO user);

    Optional<User> getById(Long id);

    List<User> getAll();

    void save(User user);

    ShippingAddress updateShippingAddress(ShippingAddress shippingAddress, String loggedUserEmail);

    WishlistDTO getUserWishlist(String email) throws IOException;

    BasketDTO getUserBasket(String email) throws IOException;
}
