package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.helper.TokenUtils;
import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.dto.JsonResponse;
import rs.ac.uns.ftn.model.dto.PaginationResponse;
import rs.ac.uns.ftn.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private OrderService orderService;

    public OrderController() {
        this.logger = Logger.getLogger(String.valueOf(UserController.class));
    }

    @PostMapping("/create")
    public ResponseEntity<JsonResponse> createOrder(HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        String message = orderService.createOrder(loggedUserEmail);
        if (message != null) {
            logger.info(message);
            return ResponseEntity.badRequest().body(new JsonResponse(message));
        }

        return ResponseEntity.ok(new JsonResponse("Order successfully created."));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Orderr> getByCode(@PathVariable String code) {
        Orderr order = orderService.getByCodeWithImages(code);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<JsonResponse> cancelOrder(@PathVariable Long orderId, HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        Orderr order = orderService.cancelOrder(loggedUserEmail, orderId);
        if (order == null) {
            logger.info("Order can't be cancelled.");
            return ResponseEntity.badRequest().body(new JsonResponse("Order can't be cancelled."));
        }

        return ResponseEntity.ok(new JsonResponse("Order successfully cancelled."));
    }

    @PutMapping("/sentOrder/{orderId}")
    public ResponseEntity<JsonResponse> sentOrder(@PathVariable Long orderId) {

        Orderr order = orderService.sentOrder(orderId);
        if (order == null) {
            logger.info("Order can't be sent.");
            return ResponseEntity.badRequest().body(new JsonResponse("Order can't be sent."));
        }

        return ResponseEntity.ok(new JsonResponse("Order successfully sent."));
    }

    @PutMapping("/deliverOrder/{orderId}")
    public ResponseEntity<JsonResponse> deliverOrder(@PathVariable Long orderId) {

        Orderr order = orderService.deliverOrder(orderId);
        if (order == null) {
            logger.info("Order can't be deliver.");
            return ResponseEntity.badRequest().body(new JsonResponse("Order can't be deliver."));
        }

        return ResponseEntity.ok(new JsonResponse("Order successfully delivered."));
    }

    @GetMapping("/sentOrders")
    public ResponseEntity<PaginationResponse> getSendOrders(@RequestParam(defaultValue = "0") int page) {
        if(page < 0){
            PaginationResponse response = new PaginationResponse("Page number is less than 0.");
            return ResponseEntity.badRequest().body(response);
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.asc("creationTime")));

        Page<Orderr> ordersPage = orderService.getSentOrders(pageable);

        PaginationResponse response = new PaginationResponse("Orders fetched successfully.");
        response.setData(ordersPage.getContent());
        response.setTotalElements(ordersPage.getTotalElements());
        response.setTotalPages(ordersPage.getTotalPages());
        response.setCurrentPage(ordersPage.getNumber());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pendingOrders")
    public ResponseEntity<PaginationResponse> getPendingOrders(@RequestParam(defaultValue = "0") int page) {
        if(page < 0){
            PaginationResponse response = new PaginationResponse("Page number is less than 0.");
            return ResponseEntity.badRequest().body(response);
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.asc("creationTime")));

        Page<Orderr> ordersPage = orderService.getPendingOrders(pageable);

        PaginationResponse response = new PaginationResponse("Orders fetched successfully.");
        response.setData(ordersPage.getContent());
        response.setTotalElements(ordersPage.getTotalElements());
        response.setTotalPages(ordersPage.getTotalPages());
        response.setCurrentPage(ordersPage.getNumber());

        return ResponseEntity.ok(response);
    }

}
