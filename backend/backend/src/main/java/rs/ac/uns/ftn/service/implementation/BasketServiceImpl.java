package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.helper.Helper;
import rs.ac.uns.ftn.model.BasketItem;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.Size;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.service.BasketItemService;
import rs.ac.uns.ftn.service.BasketService;
import rs.ac.uns.ftn.service.ProductService;
import rs.ac.uns.ftn.service.UserService;

import java.util.Optional;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    Helper helper;

    @Autowired
    BasketItemService basketItemService;

    @Override
    public BasketItem addBasketItem(Long productId, Size size, String loggedUserEmail) {
        Optional<Product> productOptional = productService.getById(productId);

        if (!productOptional.isPresent() || !helper.sizeOnStock(productOptional.get(), size)) {
            return null;
        }

        User loggedUser = userService.findByEmail(loggedUserEmail);

        if (loggedUser == null) {
            return null;
        }

        BasketItem existBasketItem = getBasketItemByProductAndSize(loggedUser, productOptional.get(), size);

        if (existBasketItem != null) {
            int newQuantity = existBasketItem.getQuantity() + 1;
            if (helper.haveEnoughOnStock(productOptional.get(), size, newQuantity)) {
                existBasketItem.setQuantity(newQuantity);
                basketItemService.save(existBasketItem);
                return existBasketItem;
            } else {
                return null;
            }
        } else {
            BasketItem basketItem = new BasketItem();
            basketItem.setProduct(productOptional.get());
            basketItem.setSize(size);
            basketItem.setBasket(loggedUser.getBasket());

            if (loggedUser.getBasket().getBasketItems().add(basketItem)) {
                userService.save(loggedUser);
                return basketItem;
            }
        }

        return null;
    }


    @Override
    public BasketItem removeBasketItem(Long basketItemId, String loggedUserEmail) {
        Optional<BasketItem> basketItemOptional = basketItemService.getById(basketItemId);

        if (!basketItemOptional.isPresent()) {
            return null;
        }

        User loggedUser = userService.findByEmail(loggedUserEmail);

        if (loggedUser == null) {
            return null;
        }

        BasketItem basketItem = basketItemOptional.get();

        if (loggedUser.getBasket().getBasketItems().remove(basketItem)) {
            userService.save(loggedUser);
            return basketItem;
        }

        return null;
    }


    @Override
    public BasketItem decreaseQuantityFromBasketItem(Long basketItemId, String loggedUserEmail) {
        Optional<BasketItem> basketItemOptional = basketItemService.getById(basketItemId);

        if (!basketItemOptional.isPresent()) {
            return null;
        }

        BasketItem basketItem = basketItemOptional.get();
        User loggedUser = userService.findByEmail(loggedUserEmail);

        if (loggedUser == null) {
            return null;
        }

        if (basketItem.getQuantity() > 1) {
            basketItem.setQuantity(basketItem.getQuantity() - 1);
            basketItemService.save(basketItem);
            return basketItem;
        } else if (basketItem.getQuantity() == 1) {
            BasketItem removedBasketItem = removeBasketItem(basketItemId, loggedUserEmail);
            if (removedBasketItem != null) {
                return removedBasketItem;
            }
        }

        return null;
    }


    public BasketItem getBasketItemByProductAndSize(User user, Product product, Size size) {
        if (user == null || product == null || size == null || user.getBasket() == null) {
            return null;
        }

        for (BasketItem basketItem : user.getBasket().getBasketItems()) {
            if (basketItem.getProduct().equals(product) && basketItem.getSize().equals(size)) {
                return basketItem;
            }
        }
        return null;
    }


}
