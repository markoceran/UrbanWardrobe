package rs.ac.uns.ftn.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
@DiscriminatorValue("A")
public class Admin extends BasicUser{

    @PrePersist
    protected void onCreate() {
        setRole(Role.ADMIN);
    }

}
