package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.helper.Helper;
import rs.ac.uns.ftn.model.BasketItem;
import rs.ac.uns.ftn.model.OrderStatus;
import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.repository.OrderRepository;
import rs.ac.uns.ftn.service.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SizeQuantityService sizeQuantityService;

    @Autowired
    private BasketItemService basketItemService;

    @Autowired
    private Helper helper;

    @Autowired
    private ImageService imageService;

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
        if(productsAmount > 100){
            newOrder.setShippingAmount(0);
        }
        else{
            newOrder.setShippingAmount(3);
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
    public Orderr cancelOrder(String loggedUserEmail, Long orderId) {

        User loggedUser = userService.findByEmail(loggedUserEmail);
        if (loggedUser == null) {
            throw new IllegalArgumentException("Logged user not found.");
        }

        Orderr orderForCancel = loggedUser.getOrders().stream()
                .filter(orderr -> orderr.getId().equals(orderId))
                .findFirst()
                .orElse(null);

        if (orderForCancel == null) {
            throw new IllegalArgumentException("Order not found for the given ID.");
        }


        if(helper.isOrderLessThanOneDayOld(orderForCancel) && orderForCancel.getStatus().equals(OrderStatus.Processing)){
            for(BasketItem basketItem : orderForCancel.getBasketItems()){
                productService.refillQuantity(basketItem.getProduct().getId(), basketItem.getSize(), basketItem.getQuantity());
            }
            orderForCancel.setStatus(OrderStatus.Cancelled);
            save(orderForCancel);
            return orderForCancel;
        }

        return null;
    }

    @Override
    public List<Orderr> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Orderr getByIdWithImages(Long id) {
        Optional<Orderr> orderOptional =  orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            for (BasketItem basketItem : orderOptional.get().getBasketItems()){
                if (basketItem == null) {
                    continue;
                }
                try {
                    List<String> fileNameList = imageService.listFiles(basketItem.getProduct().getCode());
                    basketItem.getProduct().setImagesName(fileNameList);
                } catch (Exception e) {
                    basketItem.getProduct().setImagesName(Collections.emptyList());
                }
            }
            return orderOptional.get();
        }else{
            return null;
        }
    }

    @Override
    public Optional<Orderr> getById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void save(Orderr order) {
        orderRepository.save(order);
    }

    @Override
    public List<Orderr> getPendingOrders() {
        List<Orderr> processingOrders = orderRepository.findByStatus(OrderStatus.Processing);

        return processingOrders.stream()
                .filter(order -> !helper.isOrderLessThanOneDayOld(order))
                .collect(Collectors.toList());
    }

    @Override
    public Orderr sentOrder(Long orderId) {
        Optional<Orderr> orderForSent = getById(orderId);

        if(orderForSent.isPresent() && orderForSent.get().getStatus().equals(OrderStatus.Processing) && !helper.isOrderLessThanOneDayOld(orderForSent.get())){
            orderForSent.get().setStatus(OrderStatus.Sent);
            save(orderForSent.get());
            return orderForSent.get();
        }

        return null;
    }

    @Override
    public Orderr deliverOrder(Long orderId) {
        Optional<Orderr> orderForSent = getById(orderId);

        if(orderForSent.isPresent() && orderForSent.get().getStatus().equals(OrderStatus.Sent) && !helper.isOrderLessThanOneDayOld(orderForSent.get())){
            orderForSent.get().setStatus(OrderStatus.Delivered);
            save(orderForSent.get());
            return orderForSent.get();
        }

        return null;
    }

    @Override
    public List<Orderr> getSentOrders() {
        List<Orderr> processingOrders = orderRepository.findByStatus(OrderStatus.Sent);

        return processingOrders;
    }

}
