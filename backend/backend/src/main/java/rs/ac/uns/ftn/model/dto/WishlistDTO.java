package rs.ac.uns.ftn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.model.ProductWithImages;
import rs.ac.uns.ftn.model.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {
    private Long id;

    private List<ProductWithImages> products;

    private User user;
}
