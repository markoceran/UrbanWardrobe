package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Courier;

import java.util.List;
import java.util.Optional;

public interface CourierService {

    Courier findByEmail(String email);

    Courier createCourier(Courier courier);

    Optional<Courier> getById(Long id);

    List<Courier> getAll();
}
