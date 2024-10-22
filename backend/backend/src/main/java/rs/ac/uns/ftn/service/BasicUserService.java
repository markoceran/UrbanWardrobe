package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.BasicUser;

public interface BasicUserService {

    BasicUser findByEmail(String email);

}
