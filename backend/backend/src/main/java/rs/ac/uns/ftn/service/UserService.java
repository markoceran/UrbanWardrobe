package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User findByEmail(String email);

    User createUser(User user);

    Optional<User> getById(Long id);

    List<User> getAll();

}
