package rs.ac.uns.ftn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.ProductCategory;
import rs.ac.uns.ftn.model.Size;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface ProductService {

    Product createProduct(Product product);

    Optional<Product> getById(Long id);

    Product findByCode(String code) throws IOException;

    Page<Product> getProducts(Pageable pageable, String loggedUserEmail) throws IOException;

    Page<Product> getProductsByCategory(Pageable pageable, ProductCategory productCategory, String loggedUserEmail) throws IOException;

    Product refillQuantity(Long productId, Size size, int quantity);

    Page<Product> getAllProducts(Pageable pageable, String loggedUserEmail);

    Page<Product> getAllProductsByCategory(Pageable pageable, ProductCategory productCategory, String loggedUserEmail);

    Set<Product> searchProductsByCode(String code, String loggedUserEmail);

    Set<Product> searchAllProductsByCode(String code, String loggedUserEmail);
}
