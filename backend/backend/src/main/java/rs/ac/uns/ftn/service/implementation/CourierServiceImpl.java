package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.BasicUser;
import rs.ac.uns.ftn.model.Courier;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.repository.CourierRepository;
import rs.ac.uns.ftn.service.BasicUserService;
import rs.ac.uns.ftn.service.CourierService;

@Service
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private BasicUserService basicUserService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CourierServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Courier createCourier(UserDTO courier) {

        BasicUser user = basicUserService.findByEmail(courier.getEmail());

        if(user != null){
            return null;
        }

        Courier newCourier = new Courier();
        newCourier.setEmail(courier.getEmail());
        newCourier.setPassword(passwordEncoder.encode(courier.getPassword()));
        newCourier.setFirstName(courier.getFirstName());
        newCourier.setLastName(courier.getLastName());
        newCourier.setPhoneNumber(courier.getPhoneNumber());
        newCourier = courierRepository.save(newCourier);

        return newCourier;
    }
}
