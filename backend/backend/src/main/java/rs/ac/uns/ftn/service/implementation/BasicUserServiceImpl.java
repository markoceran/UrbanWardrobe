package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.BasicUser;
import rs.ac.uns.ftn.repository.BasicUserRepository;
import rs.ac.uns.ftn.service.BasicUserService;

@Service
public class BasicUserServiceImpl implements BasicUserService {

    @Autowired
    private BasicUserRepository basicUserRepository;

    @Override
    public BasicUser findByEmail(String email) {
        return basicUserRepository.findByEmail(email).orElse(null);
    }
}
