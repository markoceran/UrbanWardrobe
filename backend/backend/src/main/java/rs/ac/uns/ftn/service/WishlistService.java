package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Product;


public interface WishlistService {

    Product addProduct(Long productId, String loggedUserEmail);

    Product removeProduct(Long productId, String loggedUserEmail);

}
