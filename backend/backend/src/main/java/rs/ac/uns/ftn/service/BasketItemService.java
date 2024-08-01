package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.BasketItem;
import rs.ac.uns.ftn.model.User;

import java.util.Optional;

public interface BasketItemService {

    Optional<BasketItem> getById(Long id);

    void save(BasketItem basketItem);
}
