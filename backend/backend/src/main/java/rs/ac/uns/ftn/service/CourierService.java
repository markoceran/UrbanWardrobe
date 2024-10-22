package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Courier;
import rs.ac.uns.ftn.model.dto.UserDTO;

public interface CourierService {

    Courier createCourier(UserDTO courier);

}
