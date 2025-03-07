package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Admin;
import rs.ac.uns.ftn.model.BasicUser;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.repository.AdminRepository;
import rs.ac.uns.ftn.service.AdminService;
import rs.ac.uns.ftn.service.BasicUserService;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BasicUserService basicUserService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin createAdmin(UserDTO admin) {

        BasicUser user = basicUserService.findByEmail(admin.getEmail());

        if(user != null){
            return null;
        }

        Admin newAdmin = new Admin();
        newAdmin.setEmail(admin.getEmail());
        newAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        newAdmin.setFirstName(admin.getFirstName());
        newAdmin.setLastName(admin.getLastName());
        newAdmin.setPhoneNumber(admin.getPhoneNumber());
        newAdmin = adminRepository.save(newAdmin);

        return newAdmin;
    }
}
