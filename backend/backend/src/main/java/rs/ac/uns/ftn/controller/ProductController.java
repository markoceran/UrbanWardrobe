package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.Size;
import rs.ac.uns.ftn.model.dto.JsonResponse;
import rs.ac.uns.ftn.model.dto.PaginationResponse;
import rs.ac.uns.ftn.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    private final Logger logger;

    public ProductController() {
        this.logger = Logger.getLogger(String.valueOf(UserController.class));
    }

    @GetMapping("allProduct")
    public List<Product> allProduct() {
        return this.productService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        logger.info("Find product by id");
        Optional<Product> product = this.productService.getById(Long.valueOf(id));

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse("Product not found"));
        }
    }

    @PostMapping("")
    public ResponseEntity<Product> create(@RequestBody Product newProduct) {

        String validationError = validateProduct(newProduct);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Product createdProduct = productService.createProduct(newProduct);
            return ResponseEntity.ok(createdProduct);
        } catch (DataIntegrityViolationException e) {
            logger.info("Unique constraint violation.");
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.info("Database error.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    public String validateProduct(Product newProduct) {
        if (newProduct.getName() == null || newProduct.getName().isEmpty() ||
                newProduct.getDescription() == null || newProduct.getDescription().isEmpty() ||
                newProduct.getCategory() == null) {
            return "Validation error. All fields are required.";
        }
        if (newProduct.getPrice() <= 0) {
            return "Validation error. Price can't be less than 0 or 0.";
        }
        if (newProduct.getSizeQuantities() == null || newProduct.getSizeQuantities().isEmpty()) {
            return "Validation error. Size quantities are required.";
        }
        return null;
    }

    @GetMapping("")
    public ResponseEntity<PaginationResponse> getProducts(
            @RequestParam(defaultValue = "0") int page) {

        if(page < 0){
            PaginationResponse response = new PaginationResponse("Page number is less than 0.");
            return ResponseEntity.badRequest().body(response);
        }

        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Order.asc("id")));

        Page<Product> productsPage = productService.getProducts(pageable);

        PaginationResponse response = new PaginationResponse("Products fetched successfully.");
        response.setData(productsPage.getContent());
        response.setTotalElements(productsPage.getTotalElements());
        response.setTotalPages(productsPage.getTotalPages());
        response.setCurrentPage(productsPage.getNumber());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/refillQuantity/{productId}/{quantity}")
    public ResponseEntity<JsonResponse> refillQuantity(@PathVariable Long productId, @RequestBody Size size, @PathVariable int quantity) {

        if (quantity <= 0){
            return ResponseEntity.badRequest().body(new JsonResponse("Can't refill quantity. Quantity can't be less than 0 or 0."));
        }

        Product product = productService.refillQuantity(productId, size, quantity);
        if (product == null) {
            logger.info("Error refilling quantity.");
            return ResponseEntity.badRequest().body(new JsonResponse("Error refilling quantity."));
        }

        return ResponseEntity.ok(new JsonResponse("Product size successfully refilled."));
    }



}
