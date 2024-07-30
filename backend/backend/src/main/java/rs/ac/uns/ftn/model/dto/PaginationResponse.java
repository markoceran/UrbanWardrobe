package rs.ac.uns.ftn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private String message;
    private Object data;
    private long totalElements;
    private int totalPages;
    private int currentPage;

    public PaginationResponse(String message) {
        this.message = message;
    }
}
