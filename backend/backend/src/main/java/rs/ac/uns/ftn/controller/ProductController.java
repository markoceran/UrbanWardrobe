package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.dto.JsonResponse;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.dto.PaginationResponse;
import rs.ac.uns.ftn.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<JsonResponse> create(@RequestBody Product newProduct) {

        String validationError = validateProduct(newProduct);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(new JsonResponse(validationError));
        }

        try {
            productService.createProduct(newProduct);
            return ResponseEntity.ok(new JsonResponse("Product successfully created."));
        } catch (DataIntegrityViolationException e) {
            logger.info("Unique constraint violation.");
            return ResponseEntity.badRequest().body(new JsonResponse("Product code already exists."));
        } catch (Exception e) {
            logger.info("Database error.");
            return ResponseEntity.internalServerError().body(new JsonResponse("Database error."));
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

        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Order.asc("id")));

        Page<Product> productsPage = productService.getProducts(pageable);

        PaginationResponse response = new PaginationResponse("Products fetched successfully.");
        response.setData(productsPage.getContent());
        response.setTotalElements(productsPage.getTotalElements());
        response.setTotalPages(productsPage.getTotalPages());
        response.setCurrentPage(productsPage.getNumber());

        return ResponseEntity.ok(response);
    }



}
