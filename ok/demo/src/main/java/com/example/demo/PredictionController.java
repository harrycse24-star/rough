package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin
public class PredictionController {

    @GetMapping("/predict")
    public Map<String, Object> predict() {

        // Dummy values (replace later with Python output if needed)
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