package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/addBasketItem")
    public ResponseEntity<JsonResponse> addBasketItem(@RequestParam("productId") Long productId, @RequestParam("size") String sizeParam, HttpServletRequest request) {
        Size size;
        try {
            size = Size.valueOf(sizeParam);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new JsonResponse("Invalid size parameter."));
        }

        if (productId < 0) {
            return ResponseEntity.badRequest().body(new JsonResponse("Product id can't be negative"));
        }

        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        BasketItem basketItem = basketService.addBasketItem(productId, size, loggedUserEmail);
        if (basketItem == null) {
            logger.info("Can't add product to basket. Product out of stock.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponse("Can't add product to basket. Product out of stock."));
        }

        return ResponseEntity.ok(new JsonResponse("Product successfully added to basket."));
    }

    @DeleteMapping("/removeBasketItem/{basketItemId}")
    public ResponseEntity<JsonResponse> removeBasketItem(@PathVariable Long basketItemId, HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        BasketItem basketItem = basketService.removeBasketItem(basketItemId, loggedUserEmail);
        if (basketItem == null) {
            logger.info("Error while removing product from basket.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponse("Error while removing product from basket."));
        }

        return ResponseEntity.ok(new JsonResponse("Product successfully removed from basket."));
    }

    @PutMapping("/decreaseQuantityFromBasketItem/{basketItemId}")
    public ResponseEntity<JsonResponse> decreaseQuantityFromBasketItem(@PathVariable Long basketItemId, HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        BasketItem basketItem = basketService.decreaseQuantityFromBasketItem(basketItemId, loggedUserEmail);
        if (basketItem == null) {
            logger.info("Error while decreasing quantity from basket item.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponse("Error while decreasing quantity from basket item."));
        }

        return ResponseEntity.ok(new JsonResponse("Quantity successfully decreased."));
    }

}
