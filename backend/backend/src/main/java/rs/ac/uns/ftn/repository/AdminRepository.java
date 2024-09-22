package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
