package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.SizeQuantity;
import rs.ac.uns.ftn.repository.SizeQuantityRepository;
import rs.ac.uns.ftn.service.SizeQuantityService;

import java.util.List;

@Service
public class SizeQuantityServiceImpl implements SizeQuantityService {

    @Autowired
    private SizeQuantityRepository sizeQuantityRepository;


    @Override
    public List<SizeQuantity> getAll() {
        return sizeQuantityRepository.findAll();
    }

    @Override
    public SizeQuantity createSizeQuantity(SizeQuantity newSizeQuantity) {

        if (newSizeQuantity.getProduct() == null) {
            throw new IllegalArgumentException("Product must be provided.");
        }

        return sizeQuantityRepository.save(newSizeQuantity);
    }

    @Override
    public void save(SizeQuantity sizeQuantity) {
        sizeQuantityRepository.save(sizeQuantity);
    }
}
