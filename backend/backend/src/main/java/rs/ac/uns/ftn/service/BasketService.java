package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.BasketItem;
import rs.ac.uns.ftn.model.Size;

public interface BasketService {

    BasketItem addBasketItem(Long productId, Size size, String loggedUserEmail);

    BasketItem removeBasketItem(Long basketItemId, String loggedUserEmail);

    BasketItem decreaseQuantityFromBasketItem(Long basketItemId, String loggedUserEmail);
}
