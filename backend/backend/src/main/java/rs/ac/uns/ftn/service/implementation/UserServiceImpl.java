package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.helper.Helper;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.model.dto.*;
import rs.ac.uns.ftn.repository.OrderRepository;
import rs.ac.uns.ftn.repository.UserRepository;
import rs.ac.uns.ftn.service.BasicUserService;
import rs.ac.uns.ftn.service.UserService;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private Helper helper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    @Override
    public User createUser(UserDTO userForCreate) {

        BasicUser user = basicUserService.findByEmail(userForCreate.getEmail());

        if(user != null){
            return null;
        }

        User newUser = new User();
        newUser.setEmail(userForCreate.getEmail());
        newUser.setPassword(passwordEncoder.encode(userForCreate.getPassword()));
        newUser.setFirstName(userForCreate.getFirstName());
        newUser.setLastName(userForCreate.getLastName());
        newUser.setPhoneNumber(userForCreate.getPhoneNumber());
        newUser.setShippingAddress(userForCreate.getShippingAddress());
        newUser = userRepository.save(newUser);

        return newUser;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public ShippingAddress updateShippingAddress(ShippingAddress shippingAddress, String loggedUserEmail) {
        User user = this.findByEmail(loggedUserEmail);
        if (user != null && shippingAddress != null) {
            ShippingAddress existingAddress = user.getShippingAddress();

            if (existingAddress != null) {
                shippingAddress.setId(existingAddress.getId());
            } else {
                return null;
            }

            user.setShippingAddress(shippingAddress);
            this.save(user);

            return shippingAddress;
        }
        return null;
    }


    @Override
    public Wishlist getUserWishlist(String email) {
        User user = findByEmail(email);
        if (user == null) {
            return null;
        }

        Wishlist wishlist = user.getWishList();
        if (wishlist == null) {
            return null;
        }

        for (Product product : wishlist.getProducts()) {
            if (product == null) {
                continue;
            }

            try {
                List<String> fileNameList = imageService.listFiles(product.getCode());
                product.setImagesName(fileNameList);
            } catch (Exception e) {
                product.setImagesName(Collections.emptyList());
            }

            if (!helper.onStock(product)) {
                product.setOutOfStock(true);
            }
        }

        return wishlist;
    }

    @Override
    public Basket getUserBasket(String email) {
        User user = findByEmail(email);
        if (user == null) {
            return null;
        }

        Basket basket = user.getBasket();
        if (basket == null) {
            return null;
        }

        for (BasketItem basketItem : basket.getBasketItems()) {

            if (basketItem == null) {
                continue;
            }

            try {
                List<String> fileNameList = imageService.listFiles(basketItem.getProduct().getCode());
                basketItem.getProduct().setImagesName(fileNameList);
            } catch (Exception e) {
                basketItem.getProduct().setImagesName(Collections.emptyList());
            }

            if(!helper.sizeOnStock(basketItem.getProduct(), basketItem.getSize())){
                basketItem.setSizeOnStock(false);
            }

            if(!helper.haveEnoughOnStock(basketItem.getProduct(), basketItem.getSize(), basketItem.getQuantity())){
                basketItem.setHaveEnoughOnStock(false);
            }

        }
        return basket;
    }

    @Override
    public UserDTO getUserProfile(String email) {
        User user = findByEmail(email);
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setShippingAddress(user.getShippingAddress());

        // Fetch sorted orders from the repository
        Set<Orderr> sortedOrders = orderRepository.findOrdersByUserEmail(email);
        userDTO.setOrders(sortedOrders);

        return userDTO;
    }

}
