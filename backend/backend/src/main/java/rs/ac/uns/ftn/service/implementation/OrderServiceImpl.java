package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.helper.Helper;
import rs.ac.uns.ftn.model.BasketItem;
import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.repository.OrderRepository;
import rs.ac.uns.ftn.service.BasketItemService;
import rs.ac.uns.ftn.service.OrderService;
import rs.ac.uns.ftn.service.SizeQuantityService;
import rs.ac.uns.ftn.service.UserService;

import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SizeQuantityService sizeQuantityService;

    @Autowired
    private BasketItemService basketItemService;

    @Autowired
    private Helper helper;

    @Override
    public String createOrder(String loggedUserEmail) {

        User loggedUser = userService.findByEmail(loggedUserEmail);

        if (loggedUser == null || loggedUser.getBasket() == null || loggedUser.getBasket().getBasketItems().isEmpty()) {
            throw new IllegalArgumentException("Invalid user or empty basket.");
        }

        double productsAmount = 0;
        Set<BasketItem> basketItems = loggedUser.getBasket().getBasketItems();

        for(BasketItem basketItem : basketItems){
            if(!helper.sizeOnStock(basketItem.getProduct(), basketItem.getSize())){
                return "Product with code " + basketItem.getProduct().getCode() + " and size " + basketItem.getSize() + " out of stock. Remove product.";
            }
        }

        for(BasketItem basketItem : basketItems){
            if(!helper.haveEnoughOnStock(basketItem.getProduct(), basketItem.getSize(), basketItem.getQuantity())){
                return "Product with code " + basketItem.getProduct().getCode() + " and size " + basketItem.getSize() + " does not have enough stock. Remove product or decrease quantity.";
            }
        }

        for(BasketItem basketItem : basketItems){
            productsAmount += basketItem.getProduct().getPrice() * basketItem.getQuantity();
            sizeQuantityService.decreaseQuantity(basketItem.getProduct(), basketItem.getSize(), basketItem.getQuantity());
        }

        Orderr newOrder = new Orderr();
        newOrder.setUser(loggedUser);
        newOrder.setProductsAmount(productsAmount);
        if(productsAmount > 5000){
            newOrder.setShippingAmount(0);
        }
        else{
            newOrder.setShippingAmount(300);
        }
        newOrder.setTotalAmount(productsAmount + newOrder.getShippingAmount());
        orderRepository.save(newOrder);

        // Update each BasketItem to reference the Order instead of the Basket
        for (BasketItem basketItem : basketItems) {
            basketItem.setBasket(null); // Clear the Basket reference
            basketItem.setOrder(newOrder); // Set the Order reference
            basketItemService.save(basketItem);
        }

        return null;

    }

    @Override
    public List<Orderr> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Orderr> getByUser(String loggedUserEmail) {
        User loggedUser = userService.findByEmail(loggedUserEmail);
        return orderRepository.findByUser(loggedUser);
    }

    @Override
    public void save(Orderr order) {
        orderRepository.save(order);
    }
}
