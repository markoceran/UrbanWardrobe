package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Admin;
import java.util.List;
import java.util.Optional;

public interface AdminService {
    List<Admin> getAll();
    Optional<Admin> getById(Long id);
    Admin createAdmin(Admin admin);

    Admin findByEmail(String email);
}
