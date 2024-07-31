package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.helper.Helper;
import rs.ac.uns.ftn.helper.TokenUtils;
import rs.ac.uns.ftn.model.BasketItem;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.Size;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.repository.BasketRepository;
import rs.ac.uns.ftn.service.BasketItemService;
import rs.ac.uns.ftn.service.BasketService;
import rs.ac.uns.ftn.service.ProductService;
import rs.ac.uns.ftn.service.UserService;

import java.util.Optional;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    BasketRepository basketRepository;

    @Autowired
    ProductService productService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    UserService userService;

    @Autowired
    Helper helper;

    @Autowired
    BasketItemService basketItemService;

    @Override
    public BasketItem addBasketItem(Long productId, Size size, String loggedUserEmail) {
        Optional<Product> productOptional  = productService.getById(productId);
        if(productOptional.isPresent()) {
            if(helper.sizeOnStock(productOptional.get(), size)){
                User loggedUser = userService.findByEmail(loggedUserEmail);
                if(loggedUser != null) {
                    BasketItem basketItem = new BasketItem();
                    basketItem.setProduct(productOptional.get());
                    basketItem.setSize(size);
                    basketItem.setBasket(loggedUser.getBasket());
                    if(loggedUser.getBasket().getBasketItems().add(basketItem)){
                        userService.save(loggedUser);
                        return basketItem;
                    }
                }
            }
        }
        return null;

    }

    @Override
    public BasketItem removeBasketItem(Long basketItemId, String loggedUserEmail) {
        Optional<BasketItem> basketItemOptional = basketItemService.getById(basketItemId);
        if(basketItemOptional.isPresent()) {
            User loggedUser = userService.findByEmail(loggedUserEmail);
            if(loggedUser != null) {
                if(loggedUser.getBasket().getBasketItems().remove(basketItemOptional.get())) {
                    userService.save(loggedUser);
                    return basketItemOptional.get();
                }
            }
        }
        return null;
    }
}
