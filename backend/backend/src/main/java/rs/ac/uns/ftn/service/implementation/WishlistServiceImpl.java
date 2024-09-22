package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.helper.Helper;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.service.ProductService;
import rs.ac.uns.ftn.service.UserService;
import rs.ac.uns.ftn.service.WishlistService;

import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    ProductService productService;

    @Autowired
    Helper helper;

    @Autowired
    UserService userService;

    @Override
    public Product addProduct(Long productId, String loggedUserEmail) {
        Optional<Product> productOptional = productService.getById(productId);

        if (!productOptional.isPresent() || !helper.onStock(productOptional.get())) {
            return null;
        }

        User loggedUser = userService.findByEmail(loggedUserEmail);

        if (loggedUser == null) {
            return null;
        }

        if (loggedUser.getWishList().getProducts().add(productOptional.get())) {
            userService.save(loggedUser);
            return productOptional.get();
        }

        return null;
    }


    @Override
    public Product removeProduct(Long productId, String loggedUserEmail) {
        Optional<Product> productOptional = productService.getById(productId);

        if (!productOptional.isPresent()) {
            return null;
        }

        User loggedUser = userService.findByEmail(loggedUserEmail);

        if (loggedUser == null) {
            return null;
        }

        if (loggedUser.getWishList().getProducts().remove(productOptional.get())) {
            userService.save(loggedUser);
            return productOptional.get();
        }

        return null;
    }

}
