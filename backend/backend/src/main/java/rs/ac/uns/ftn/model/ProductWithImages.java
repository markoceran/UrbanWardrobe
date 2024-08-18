package rs.ac.uns.ftn.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithImages {

    private Long id;

    private String code;

    private String name;

    private String description;

    @JsonManagedReference
    private Set<SizeQuantity> sizeQuantities;

    private double price;

    private ProductCategory category;

    private List<String> imagesName;

}