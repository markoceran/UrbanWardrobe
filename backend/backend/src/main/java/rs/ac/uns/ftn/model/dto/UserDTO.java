package rs.ac.uns.ftn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.model.ShippingAddress;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private ShippingAddress shippingAddress;

}
