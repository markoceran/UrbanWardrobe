package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.helper.Helper;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.repository.OrderRepository;
import rs.ac.uns.ftn.service.*;

import java.time.LocalDateTime;
import java.util.*;

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
    public Orderr getByCodeWithImages(String code) {
        Optional<Orderr> orderOptional = orderRepository.findByCode(code);
        if (orderOptional.isPresent()) {
            Orderr order = orderOptional.get();
            for (BasketItem basketItem : order.getBasketItems()){
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
            User user = order.getUser();
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(user.getEmail());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setShippingAddress(user.getShippingAddress());
            order.setUserDTO(userDTO);
            return order;
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
    public Page<Orderr> getSentOrders(Pageable pageable) {

        Page<Orderr> orderPage = orderRepository.findByStatus(OrderStatus.Sent, pageable);
        List<Orderr> orders = orderPage.getContent();

        return new PageImpl<>(orders, pageable, orderPage.getTotalElements());
    }


    @Override
    public Page<Orderr> getPendingOrders(Pageable pageable) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        Page<Orderr> orderPage = orderRepository.findPendingOrders(oneDayAgo, pageable);
        List<Orderr> orders = orderPage.getContent();

        return new PageImpl<>(orders, pageable, orderPage.getTotalElements());
    }

    @Override
    public Page<Orderr> getDeliveredOrders(Pageable pageable) {

        Page<Orderr> orderPage = orderRepository.findByStatus(OrderStatus.Delivered, pageable);
        List<Orderr> orders = orderPage.getContent();

        return new PageImpl<>(orders, pageable, orderPage.getTotalElements());
    }

    @Override
    public Set<Orderr> searchPendingOrdersByCode(String code) {
        return orderRepository.findPendingOrdersBySearch(code.toUpperCase());
    }

    @Override
    public Set<Orderr> searchSentOrdersByCode(String code) {
        return orderRepository.findSentOrdersBySearch(code.toUpperCase());
    }


}
