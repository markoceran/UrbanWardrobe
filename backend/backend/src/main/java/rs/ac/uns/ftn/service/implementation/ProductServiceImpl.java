package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.repository.ProductRepository;
import rs.ac.uns.ftn.service.ImageService;
import rs.ac.uns.ftn.service.ProductService;
import rs.ac.uns.ftn.service.SizeQuantityService;
import rs.ac.uns.ftn.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    public ProductWithImages findByCode(String code) throws IOException {

        Product product = productRepository.findByCode(code).orElse(null);
        if (product == null) {
            return null;
        }
        List<String> fileNameList = imageService.listFiles(code);
        ProductWithImages productWithImages = new ProductWithImages();

        productWithImages.setId(product.getId());
        productWithImages.setName(product.getName());
        productWithImages.setCode(product.getCode());
        productWithImages.setDescription(product.getDescription());
        productWithImages.setCategory(product.getCategory());
        productWithImages.setPrice(product.getPrice());
        productWithImages.setSizeQuantities(product.getSizeQuantities());

        productWithImages.setImagesName(fileNameList);

        return productWithImages;
    }

    @Override
    public Page<ProductWithImages> getProducts(Pageable pageable, String loggedUserEmail) throws IOException {

        User loggedUser = userService.findByEmail(loggedUserEmail);
        if (loggedUser == null) {
            return Page.empty(pageable);
        }

        Page<Product> productPage = productRepository.findAvailableProducts(pageable);
        List<Product> products = productPage.getContent();
        List<ProductWithImages> productsWithImages = new ArrayList<>();

        for (Product product : products) {

            List<String> fileNameList = imageService.listFiles(product.getCode());

            ProductWithImages productWithImages = new ProductWithImages();

            productWithImages.setId(product.getId());
            productWithImages.setName(product.getName());
            productWithImages.setCode(product.getCode());
            productWithImages.setDescription(product.getDescription());
            productWithImages.setCategory(product.getCategory());
            productWithImages.setPrice(product.getPrice());
            productWithImages.setSizeQuantities(product.getSizeQuantities());

            productWithImages.setImagesName(fileNameList);

            Optional<Product> productFromWishlist = loggedUser.getWishList().getProducts().stream().filter(p ->  p.getId() == product.getId()).findFirst();
            if (productFromWishlist.isPresent()) {
                productWithImages.setInWishlist(true);
            }else {
                productWithImages.setInWishlist(false);
            }

            productsWithImages.add(productWithImages);
        }

        // Convert List<ProductWithImages> to Page<ProductWithImages>
        return new PageImpl<>(productsWithImages, pageable, productPage.getTotalElements());
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
