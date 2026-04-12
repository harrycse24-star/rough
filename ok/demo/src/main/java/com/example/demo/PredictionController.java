package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class PredictionController {

    @GetMapping("/predict")
    public Map<String, Object> predict() {

        // Example values (replace later with file/Python output)
        double lastPrice = 22000;
        double predictedPrice = 22150;

        String status;
        if (predictedPrice > lastPrice) {
            status = "Positive";
        } else if (predictedPrice < lastPrice) {
            status = "Negative";
        } else {
            status = "Neutral";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("lastPrice", lastPrice);
        response.put("predictedPrice", predictedPrice);
        response.put("status", status);

        return response;
    }
}