package rs.ac.uns.ftn.helper;

import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.model.Product;
import rs.ac.uns.ftn.model.Size;
import rs.ac.uns.ftn.model.SizeQuantity;

@Component
public class Helper {

    public boolean onStock(Product product){
        if (product == null) {
            return false;
        }

        for (SizeQuantity sq : product.getSizeQuantities()) {
            if (sq.getQuantity() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean sizeOnStock(Product product, Size size) {
        if (product == null || size == null) {
            return false;
        }

        for (SizeQuantity sq : product.getSizeQuantities()) {
            if (sq.getQuantity() > 0 && size.equals(sq.getSize())) {
                return true;
            }
        }
        return false;
    }

    public boolean haveEnoughOnStock(Product product, Size size, int quantity) {
        if (product == null || size == null || quantity <= 0) {
            return false;
        }

        for (SizeQuantity sq : product.getSizeQuantities()) {
            if (sq.getQuantity() >= quantity && size.equals(sq.getSize())) {
                return true;
            }
        }
        return false;
    }


}