package com.example.stockapp; // Change this to match your project folder structure

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*") // Allows the HTML file to talk to Java
public class StockController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Initialize RestTemplate to talk to Python
            RestTemplate restTemplate = new RestTemplate();
            
            // 2. Set up headers for File Upload (Multipart)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 3. Wrap the uploaded file into a format Python understands
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 4. Send to Python Flask (Brain)
            String url = "http://localhost:5000/predict";
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error communicating with Python: " + e.getMessage());
        }
    }
}