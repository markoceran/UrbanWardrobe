package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
}
