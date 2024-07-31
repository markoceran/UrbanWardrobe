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

        boolean onStock = false;
        for(SizeQuantity sq:product.getSizeQuantities()){
            if(sq.getQuantity() > 0){
                onStock = true;
                break;
            }
        }
        return onStock;
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


}
