package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.helper.TokenUtils;
import rs.ac.uns.ftn.model.Orderr;
import rs.ac.uns.ftn.model.dto.JsonResponse;
import rs.ac.uns.ftn.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    @GetMapping("/myOrders")
    public ResponseEntity<?> getById(HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        List<Orderr> myOrders = orderService.getByUser(loggedUserEmail);

        return ResponseEntity.ok(myOrders);
    }

}
