package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Courier;
import rs.ac.uns.ftn.model.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface CourierService {

    Courier findByEmail(String email);

    Courier createCourier(UserDTO courier);

    Optional<Courier> getById(Long id);

    List<Courier> getAll();
}
