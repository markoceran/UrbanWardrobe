package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.helper.TokenUtils;
import rs.ac.uns.ftn.model.BasketItem;
import rs.ac.uns.ftn.model.Size;
import rs.ac.uns.ftn.model.dto.JsonResponse;
import rs.ac.uns.ftn.service.BasketService;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@RestController
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private BasketService basketService;

    private final Logger logger;

    @Autowired
    private TokenUtils tokenUtils;

    public BasketController() {
        this.logger = Logger.getLogger(String.valueOf(UserController.class));
    }

    @PutMapping("/addBasketItem/{productId}")
    public ResponseEntity<JsonResponse> addBasketItem(@PathVariable Long productId, @RequestBody Size size, HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        BasketItem basketItem = basketService.addBasketItem(productId, size, loggedUserEmail);
        if (basketItem == null) {
            logger.info("Error while adding product to basket");
            return ResponseEntity.badRequest().body(new JsonResponse("Error while adding product to basket"));
        }

        return ResponseEntity.ok(new JsonResponse("Product successfully added to basket."));
    }

    @PutMapping("/removeBasketItem/{basketItemId}")
    public ResponseEntity<JsonResponse> removeBasketItem(@PathVariable Long basketItemId, HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        BasketItem basketItem = basketService.removeBasketItem(basketItemId, loggedUserEmail);
        if (basketItem == null) {
            logger.info("Error while removing product from basket");
            return ResponseEntity.badRequest().body(new JsonResponse("Error while removing product from basket"));
        }

        return ResponseEntity.ok(new JsonResponse("Product successfully removed from basket."));
    }

}
