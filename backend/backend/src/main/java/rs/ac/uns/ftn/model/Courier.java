package rs.ac.uns.ftn.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
@DiscriminatorValue("C")
public class Courier extends BasicUser{

    @PrePersist
    protected void onCreate() {
        setRole(Role.COURIER);
    }
}
