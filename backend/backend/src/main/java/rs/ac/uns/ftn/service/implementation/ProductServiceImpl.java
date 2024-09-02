package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.repository.ProductRepository;
import rs.ac.uns.ftn.service.BasicUserService;
import rs.ac.uns.ftn.service.ProductService;
import rs.ac.uns.ftn.service.SizeQuantityService;
import rs.ac.uns.ftn.service.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SizeQuantityService sizeQuantityService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private BasicUserService basicUserService;


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

        Product product = productRepository.findByCode(code).orElse(null);
        if (product == null) {
            return null;
        }

        try {
            List<String> fileNameList = imageService.listFiles(code);
            product.setImagesName(fileNameList);
        } catch (Exception e) {
            product.setImagesName(Collections.emptyList());
        }

        return product;
    }

    @Override
    public Page<Product> getProducts(Pageable pageable, String loggedUserEmail) {

        BasicUser loggedUser = basicUserService.findByEmail(loggedUserEmail);
        if (loggedUser == null) {
            return Page.empty(pageable);
        }

        Page<Product> productPage = productRepository.findAvailableProducts(pageable);
        List<Product> products = productPage.getContent();

        for (Product product : products) {

            try {
                List<String> fileNameList = imageService.listFiles(product.getCode());
                product.setImagesName(fileNameList);
            } catch (Exception e) {
                product.setImagesName(Collections.emptyList());
            }

            if(loggedUser.getRole() == Role.USER){
                User user = userService.findByEmail(loggedUserEmail);
                if (user == null) {
                    return Page.empty(pageable);
                }
                Optional<Product> productFromWishlist = user.getWishList().getProducts().stream().filter(p ->  p.getId() == product.getId()).findFirst();
                if (productFromWishlist.isPresent()) {
                    product.setInWishlist(true);
                }
            }

        }
        return new PageImpl<>(products, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<Product> getProductsByCategory(Pageable pageable, ProductCategory productCategory, String loggedUserEmail) throws IOException {

        BasicUser loggedUser = basicUserService.findByEmail(loggedUserEmail);
        if (loggedUser == null) {
            return Page.empty(pageable);
        }

        Page<Product> productPage = productRepository.findAvailableProductsByCategory(productCategory, pageable);
        List<Product> products = productPage.getContent();

        for (Product product : products) {

            try {
                List<String> fileNameList = imageService.listFiles(product.getCode());
                product.setImagesName(fileNameList);
            } catch (Exception e) {
                product.setImagesName(Collections.emptyList());
            }

            if(loggedUser.getRole() == Role.USER){
                User user = userService.findByEmail(loggedUserEmail);
                if (user == null) {
                    return Page.empty(pageable);
                }
                Optional<Product> productFromWishlist = user.getWishList().getProducts().stream().filter(p ->  p.getId() == product.getId()).findFirst();
                if (productFromWishlist.isPresent()) {
                    product.setInWishlist(true);
                }
            }

        }
        return new PageImpl<>(products, pageable, productPage.getTotalElements());
    }


    @Override
    public Product refillQuantity(Long productId, Size size, int quantity) {
        Optional<Product> productOptional = this.getById(productId);

        if (productOptional.isEmpty()) {
            return null;
        } else {
            Product product = productOptional.get();
            boolean found = false;
            for (SizeQuantity sq : product.getSizeQuantities()) {
                if (sq.getSize().equals(size)) {
                    sq.setQuantity(sq.getQuantity() + quantity);
                    sizeQuantityService.save(sq);
                    found = true;
                    break;
                }
            }
            if (!found) {
                SizeQuantity sizeQuantity = new SizeQuantity();
                sizeQuantity.setSize(size);
                sizeQuantity.setQuantity(quantity);
                sizeQuantity.setProduct(product);
                sizeQuantityService.save(sizeQuantity);
                product.getSizeQuantities().add(sizeQuantity);
            }
            return product;
        }
    }


}
