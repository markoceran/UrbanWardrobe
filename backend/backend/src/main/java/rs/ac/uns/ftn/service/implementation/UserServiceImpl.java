package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.model.dto.BasketDTO;
import rs.ac.uns.ftn.model.dto.BasketItemDTO;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.model.dto.WishlistDTO;
import rs.ac.uns.ftn.repository.UserRepository;
import rs.ac.uns.ftn.service.BasicUserService;
import rs.ac.uns.ftn.service.ImageService;
import rs.ac.uns.ftn.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private ImageService imageService;

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
        if (user != null) {
            shippingAddress.setId(user.getShippingAddress().getId());
            user.setShippingAddress(shippingAddress);
            this.save(user);
            return shippingAddress;
        }
        return null;
    }

    @Override
    public WishlistDTO getUserWishlist(String email) throws IOException {
        User user = findByEmail(email);
        if (user == null) {
            return null;
        }

        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setId(user.getWishList().getId());

        List<ProductWithImages> productsWithImages = new ArrayList<>();

        for (Product product : user.getWishList().getProducts()) {

            List<String> fileNameList = imageService.listFiles(product.getCode());

            ProductWithImages productWithImages = new ProductWithImages();

            productWithImages.setId(product.getId());
            productWithImages.setName(product.getName());
            productWithImages.setCode(product.getCode());
            productWithImages.setDescription(product.getDescription());
            productWithImages.setCategory(product.getCategory());
            productWithImages.setPrice(product.getPrice());
            productWithImages.setSizeQuantities(product.getSizeQuantities());

            productWithImages.setImagesName(fileNameList);


            productsWithImages.add(productWithImages);
        }

        wishlistDTO.setProducts(productsWithImages);

        return wishlistDTO;

    }

    @Override
    public BasketDTO getUserBasket(String email) throws IOException {
        User user = findByEmail(email);
        if (user == null) {
            return null;
        }

        BasketDTO basketDTO = new BasketDTO();
        basketDTO.setId(user.getBasket().getId());

        List<BasketItemDTO> basketItemDTOS = new ArrayList<>();

        for (BasketItem basketItem : user.getBasket().getBasketItems()) {

            List<String> fileNameList = imageService.listFiles(basketItem.getProduct().getCode());

            BasketItemDTO basketItemDTO = new BasketItemDTO();

            basketItemDTO.setId(basketItem.getId());
            basketItemDTO.setBasket(basketItem.getBasket());
            basketItemDTO.setSize(basketItem.getSize());
            basketItemDTO.setQuantity(basketItem.getQuantity());
            basketItemDTO.setOrder(basketItem.getOrder());

            ProductWithImages productWithImages = new ProductWithImages();

            productWithImages.setId(basketItem.getProduct().getId());
            productWithImages.setName(basketItem.getProduct().getName());
            productWithImages.setCode(basketItem.getProduct().getCode());
            productWithImages.setDescription(basketItem.getProduct().getDescription());
            productWithImages.setCategory(basketItem.getProduct().getCategory());
            productWithImages.setPrice(basketItem.getProduct().getPrice());
            productWithImages.setSizeQuantities(basketItem.getProduct().getSizeQuantities());
            productWithImages.setImagesName(fileNameList);

            basketItemDTO.setProduct(productWithImages);

            basketItemDTOS.add(basketItemDTO);
        }

        basketDTO.setBasketItems(basketItemDTOS);

        return basketDTO;

    }
}
