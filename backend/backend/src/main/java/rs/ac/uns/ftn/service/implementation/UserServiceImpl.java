package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.BasicUser;
import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.repository.UserRepository;
import rs.ac.uns.ftn.service.BasicUserService;
import rs.ac.uns.ftn.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasicUserService basicUserService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }


    @Override
    public User findByEmail(String email) {
        return getAll().stream().filter(u->u.getEmail().equals(email)).findAny().orElse(null);
    }
    @Override
    public User createUser(User userForCreate) {

        BasicUser user = basicUserService.findByEmail(userForCreate.getEmail());

        if(user != null){
            return null;
        }

        User newUser = new User();
        newUser.setEmail(userForCreate.getEmail());
        newUser.setPassword(passwordEncoder.encode(userForCreate.getPassword()));
        newUser.setFirstName(userForCreate.getFirstName());
        newUser.setLastName(userForCreate.getLastName());
        newUser.setPhoneNumber(userForCreate.getPhoneNumber());
        newUser.setShippingAddress(userForCreate.getShippingAddress());
        newUser = userRepository.save(newUser);

        return newUser;
    }
}
