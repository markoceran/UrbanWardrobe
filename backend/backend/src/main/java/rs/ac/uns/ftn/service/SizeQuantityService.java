package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.Size;
import rs.ac.uns.ftn.model.SizeQuantity;

import java.util.List;

public interface SizeQuantityService {

    SizeQuantity createSizeQuantity(SizeQuantity sizeQuantity);

    List<SizeQuantity> getAll();

    void save(SizeQuantity sizeQuantity);

    void decreaseQuantity(Product product, Size size, int quantity);
}
