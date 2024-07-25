package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.BasicUser;
import rs.ac.uns.ftn.model.User;

import java.util.List;
import java.util.Optional;

public interface BasicUserService {

    BasicUser findByEmail(String email);

    Optional<BasicUser> getById(Long id);

    List<BasicUser> getAll();

}
