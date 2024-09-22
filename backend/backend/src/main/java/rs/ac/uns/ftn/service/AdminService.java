package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Admin;
import rs.ac.uns.ftn.model.dto.UserDTO;

public interface AdminService {

    Admin createAdmin(UserDTO admin);

}
