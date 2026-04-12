import pandas as pd
import numpy as np
from flask import Flask, request, jsonify
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch

app = Flask(__name__)

# Load FinBERT
tokenizer = AutoTokenizer.from_pretrained("ProsusAI/finbert")
model = AutoModelForSequenceClassification.from_pretrained("ProsusAI/finbert")

def get_sentiment(text):
    inputs = tokenizer(text, return_tensors="pt", padding=True, truncation=True)
    outputs = model(**inputs)
    prediction = torch.nn.functional.softmax(outputs.logits, dim=-1)
    return prediction.detach().numpy()[0][0] # Returns positive sentiment score

@app.route('/predict', methods=['POST'])
def predict():
    file = request.files['file']
    df = pd.read_excel(file)
    
    # Assume Excel has 'Close' and 'Headline' columns
    prices = df['Close'].values.astype(float)
    
    # Very simple LSTM training on the fly for demo
    X = prices[-10:].reshape(1, 10, 1) # Last 10 days
    y_pred = []
    
    # Predict 2 days
    current_input = X
    for _ in range(2):
        # In a real app, use a pre-trained .h5 model here
        next_val = current_input.mean() * 1.01 # Simulated logic
        y_pred.append(float(next_val))
        current_input = np.append(current_input[:, 1:, :], [[[next_val]]], axis=1)

    return jsonify({
        "history": prices.tolist(),
        "predictions": y_pred
    })

if __name__ == '__main__':
    app.run(port=5000)