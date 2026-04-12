@GetMapping("/predict")
public Map<String, Object> getData() throws Exception {

    List<String> dates = new ArrayList<>();
    List<Double> prices = new ArrayList<>();

    BufferedReader br = new BufferedReader(
        new FileReader("C:/Users/Naresh/OneDrive/Desktop/delete/rough/ok/NIFTY.csv")
    );

    String line;
    br.readLine(); // skip header

    while ((line = br.readLine()) != null) {
        String[] v = line.split(",");

        dates.add(v[0]);
        prices.add(Double.parseDouble(v[4].replace(",", "")));
    }

    br.close();

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