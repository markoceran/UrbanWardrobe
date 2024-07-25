package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.BasicUser;
import rs.ac.uns.ftn.repository.BasicUserRepository;
import rs.ac.uns.ftn.service.BasicUserService;

import java.util.List;
import java.util.Optional;

@Service
public class BasicUserServiceImpl implements BasicUserService {

    @Autowired
    private BasicUserRepository basicUserRepository;

    @Override
    public List<BasicUser> getAll() {
        return basicUserRepository.findAll();
    }

    @Override
    public Optional<BasicUser> getById(Long id) {
        return basicUserRepository.findById(id);
    }

    @Override
    public BasicUser findByEmail(String email) {
        return getAll().stream().filter(u->u.getEmail().equals(email)).findAny().orElse(null);
    }
}
