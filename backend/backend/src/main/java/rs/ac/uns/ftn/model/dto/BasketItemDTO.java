package rs.ac.uns.ftn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.model.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemDTO {

    private Long id;

    private ProductWithImages product;

    private Size size;

    private Basket basket;

    private Orderr order;

    private int quantity;

    private boolean sizeOnStock = true;

    private boolean haveEnoughOnStock = true;
}
