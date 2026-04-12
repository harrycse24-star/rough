import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.preprocessing import MinMaxScaler
from keras.models import Sequential
from keras.layers import LSTM, Dense, Dropout

# =========================
# Load Data
# =========================
def load_data(filepath):
    df = pd.read_csv(filepath)
    df.columns = df.columns.str.strip()
    df['Date'] = pd.to_datetime(df['Date'])
    df.sort_values('Date', inplace=True)
    df.set_index('Date', inplace=True)

    df['Close'] = df['Close'].replace(',', '', regex=True).astype(float)

    return df[['Close']]

data = load_data("C:/Users/Naresh/OneDrive/Desktop/delete/rough/ok/NIFTY.csv")

# =========================
# Preprocess
# =========================
scaler = MinMaxScaler(feature_range=(0, 1))
scaled_data = scaler.fit_transform(data)

prediction_days = 50
x_train, y_train = [], []

for i in range(prediction_days, len(scaled_data)):
    x_train.append(scaled_data[i-prediction_days:i, 0])
    y_train.append(scaled_data[i, 0])

x_train = np.array(x_train)
y_train = np.array(y_train)

if x_train.ndim == 1 or len(x_train) == 0:
    raise ValueError("Not enough data. Need more than 50 rows.")

x_train = x_train.reshape((x_train.shape[0], prediction_days, 1))

# =========================
# Model
# =========================
model = Sequential()
model.add(LSTM(50, return_sequences=True, input_shape=(prediction_days, 1)))
model.add(Dropout(0.2))
model.add(LSTM(50))
model.add(Dropout(0.2))
model.add(Dense(1))

model.compile(optimizer='adam', loss='mean_squared_error')
model.fit(x_train, y_train, epochs=10, batch_size=32)

# =========================
# Predict Next Day
# =========================
last_50 = scaled_data[-prediction_days:]
last_50 = last_50.reshape(1, prediction_days, 1)

prediction = model.predict(last_50)
prediction = scaler.inverse_transform(prediction)

predicted_price = prediction[0][0]
last_actual_price = data['Close'].iloc[-1]

# =========================
# Status
# =========================
if predicted_price > last_actual_price:
    status = "Positive 📈"
elif predicted_price < last_actual_price:
    status = "Negative 📉"
else:
    status = "Neutral ➖"

print("Last Price:", last_actual_price)
print("Predicted Next Price:", predicted_price)
print("Status:", status)

# =========================
# Plot
# =========================
plt.figure()

# Actual data
plt.plot(data.index[-100:], data['Close'][-100:], label='Actual Price')

# Predicted point (next day)
next_date = data.index[-1] + pd.Timedelta(days=1)
plt.scatter(next_date, predicted_price, label='Predicted Price')

plt.title(f"NIFTY 50 Stock Prediction ({status})")
plt.xlabel("Date")
plt.ylabel("Price")
plt.legend()
plt.grid()

plt.show()
#venv\Scripts\activate
