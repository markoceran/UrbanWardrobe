package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import rs.ac.uns.ftn.model.dto.JsonResponse;
import rs.ac.uns.ftn.service.MinioService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload/{productCode}")
    public ResponseEntity<JsonResponse> uploadFiles(@PathVariable String productCode, @RequestParam("images") List<MultipartFile> files) {
        try {
            for (MultipartFile file : files) {
                minioService.uploadFile(productCode, file);
            }
            return ResponseEntity.ok(new JsonResponse("Files uploaded successfully"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(new JsonResponse("Error occurred while uploading files"));
        }
    }

    @GetMapping("/download/{productCode}/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String productCode, @PathVariable String fileName) {
        try {
            InputStream inputStream = minioService.downloadFile(productCode, fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + fileName)
                    .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/list/{productCode}")
    public ResponseEntity<List<String>> listFiles(@PathVariable String productCode) {
        try {
            List<String> files = minioService.listFiles(productCode);
            return ResponseEntity.ok(files);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

}
