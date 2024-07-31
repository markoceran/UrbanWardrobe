package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.BasketItem;
import rs.ac.uns.ftn.repository.BasketItemRepository;
import rs.ac.uns.ftn.service.BasketItemService;

import java.util.Optional;

@Service
public class BasketItemServiceImpl implements BasketItemService {

    @Autowired
    BasketItemRepository basketItemRepository;

    @Override
    public Optional<BasketItem> getById(Long id) {
        return basketItemRepository.findById(id);
    }
}
