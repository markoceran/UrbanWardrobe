package rs.ac.uns.ftn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.Size;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(Product product);

    Optional<Product> getById(Long id);

    List<Product> getAll();

    Product findByCode(String code) throws IOException;

    Page<Product> getProducts(Pageable pageable, String loggedUserEmail) throws IOException;

    Product refillQuantity(Long productId, Size size, int quantity);
}
