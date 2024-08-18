package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.model.dto.JsonResponse;
import rs.ac.uns.ftn.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload/{productCode}")
    public ResponseEntity<JsonResponse> uploadFiles(@PathVariable String productCode, @RequestParam("images") List<MultipartFile> files) {
        try {
            for (MultipartFile file : files) {
                imageService.uploadFile(productCode, file);
            }
            return ResponseEntity.ok(new JsonResponse("Files uploaded successfully"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(new JsonResponse("Error occurred while uploading files"));
        }
    }

    @GetMapping("/download/{productCode}/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String productCode, @PathVariable String fileName) {
        try {
            InputStream inputStream = imageService.downloadFile(productCode, fileName);
            byte[] imageBytes = inputStream.readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/list/{productCode}")
    public ResponseEntity<List<String>> listFiles(@PathVariable String productCode) {
        try {
            List<String> files = imageService.listFiles(productCode);
            return ResponseEntity.ok(files);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

}
