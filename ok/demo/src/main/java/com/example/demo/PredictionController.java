package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
public class PredictionController {

    @PostMapping("/predict")
public Map<String, Object> predict(@RequestParam("file") MultipartFile file) throws Exception {

    List<String> dates = new ArrayList<>();
    List<Double> prices = new ArrayList<>();

    BufferedReader br = new BufferedReader(
            new InputStreamReader(file.getInputStream())
    );

    String line;
    br.readLine(); // skip header

    while ((line = br.readLine()) != null) {

        String[] v = line.split(",");

        // ✅ Prevent crash if row is invalid
        if (v.length < 5) continue;

        try {
            dates.add(v[0]);
            prices.add(Double.parseDouble(v[4].replace(",", "")));
        } catch (Exception e) {
            continue; // skip bad rows
        }
    }

    br.close();

    if (prices.size() == 0) {
        throw new RuntimeException("CSV parsing failed");
    }

    double lastPrice = prices.get(prices.size() - 1);
    double predictedPrice = lastPrice + 100;

    String status = predictedPrice > lastPrice ? "Positive"
            : predictedPrice < lastPrice ? "Negative"
            : "Neutral";

    Map<String, Object> res = new HashMap<>();
    res.put("dates", dates);
    res.put("prices", prices);
    res.put("predictedPrice", predictedPrice);
    res.put("status", status);

    return res;
}
}