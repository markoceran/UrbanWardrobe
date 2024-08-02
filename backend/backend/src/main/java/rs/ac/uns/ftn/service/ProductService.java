package rs.ac.uns.ftn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.Size;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(Product product);

    Optional<Product> getById(Long id);

    List<Product> getAll();

    Product findByCode(String code);

    Page<Product> getProducts(Pageable pageable);

    Product refillQuantity(Long productId, Size size, int quantity);
}
