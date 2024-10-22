package rs.ac.uns.ftn.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
@DiscriminatorValue("W")
public class Worker extends BasicUser{

    @PrePersist
    protected void onCreate() {
        setRole(Role.WORKER);
    }
}
