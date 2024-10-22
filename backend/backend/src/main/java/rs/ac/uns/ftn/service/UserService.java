package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Basket;
import rs.ac.uns.ftn.model.ShippingAddress;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.Wishlist;
import rs.ac.uns.ftn.model.dto.UserDTO;

public interface UserService {

    User findByEmail(String email);

    User createUser(UserDTO user);

    void save(User user);

    ShippingAddress updateShippingAddress(ShippingAddress shippingAddress, String loggedUserEmail);

    Wishlist getUserWishlist(String email);

    Basket getUserBasket(String email);

    UserDTO getUserProfile(String email);

    User updateUser(UserDTO userDTO, String loggedUserEmail);
}
