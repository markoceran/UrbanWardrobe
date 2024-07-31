package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.dto.JsonResponse;
import rs.ac.uns.ftn.helper.TokenUtils;
import rs.ac.uns.ftn.service.WishlistService;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@RestController
@RequestMapping("/wishList")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    private final Logger logger;
    @Autowired
    private TokenUtils tokenUtils;

    public WishlistController() {
        this.logger = Logger.getLogger(String.valueOf(UserController.class));
    }

    @PutMapping("/addProduct/{productId}")
    public ResponseEntity<JsonResponse> addProduct(@PathVariable Long productId, HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        Product product = wishlistService.addProduct(productId, loggedUserEmail);
        if (product == null) {
            logger.info("Error while adding product to wishlist");
            return ResponseEntity.badRequest().body(new JsonResponse("Error while adding product to wishlist"));
        }

        return ResponseEntity.ok(new JsonResponse("Product successfully added to wishlist."));
    }

    @PutMapping("/removeProduct/{productId}")
    public ResponseEntity<JsonResponse> removeProduct(@PathVariable Long productId, HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        Product product = wishlistService.removeProduct(productId, loggedUserEmail);
        if (product == null) {
            logger.info("Error while removing product from wishlist");
            return ResponseEntity.badRequest().body(new JsonResponse("Error while removing product from wishlist"));
        }

        return ResponseEntity.ok(new JsonResponse("Product successfully removed from wishlist."));
    }



}
