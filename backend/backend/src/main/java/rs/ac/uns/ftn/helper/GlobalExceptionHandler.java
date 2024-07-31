package rs.ac.uns.ftn.helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rs.ac.uns.ftn.model.dto.JsonResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResponse> handleAllExceptions(Exception ex) {
        logger.error("Exception occurred: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonResponse("Internal server error"));
    }

    // You can add more specific exception handlers here if needed
//    @ExceptionHandler(SpecificException.class)
//    public ResponseEntity<JsonResponse> handleSpecificException(SpecificException ex, WebRequest request) {
//        logger.error("SpecificException occurred: ", ex);
//        return ResponseEntity.badRequest().body(new JsonResponse("Specific error message"));
//    }


}

