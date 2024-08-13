package rs.ac.uns.ftn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.sizeQuantities sq WHERE sq.quantity > 0")
    Page<Product> findAvailableProducts(Pageable pageable);
}