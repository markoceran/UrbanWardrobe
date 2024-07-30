package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.SizeQuantity;
import rs.ac.uns.ftn.repository.ProductRepository;
import rs.ac.uns.ftn.service.ProductService;
import rs.ac.uns.ftn.service.SizeQuantityService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SizeQuantityService sizeQuantityService;


    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product newProduct) {

        Product savedProduct  = productRepository.save(newProduct);
        //savedProduct.setSizeQuantities(newProduct.getSizeQuantities());
        for (SizeQuantity sq : newProduct.getSizeQuantities()) {
            sq.setProduct(savedProduct);
            sizeQuantityService.createSizeQuantity(sq);
        }

        return savedProduct;
    }

    @Override
    public Product findByCode(String code) {
        return productRepository.findByCode(code).orElse(null);
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
