package com.example.barometre;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManualLocationActivity extends AppCompatActivity {

    private static final String API_KEY = "6fe49650e98edcabbcea05edb4d5ec20";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private RecyclerView weatherRecyclerView;
    private WeatherAdapter weatherAdapter;
    private List<WeatherItem> weatherItemList = new ArrayList<>();

    private EditText latitudeInput;
    private EditText longitudeInput;
    private Button fetchWeatherButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_location);

        latitudeInput = findViewById(R.id.latitudeInput);
        longitudeInput = findViewById(R.id.longitudeInput);
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton);

        weatherRecyclerView = findViewById(R.id.weatherRecyclerView);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(weatherItemList);
        weatherRecyclerView.setAdapter(weatherAdapter);

        fetchWeatherButton.setOnClickListener(v -> {
            String latitudeStr = latitudeInput.getText().toString();
            String longitudeStr = longitudeInput.getText().toString();

            if (latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
                Toast.makeText(this, "Please enter both latitude and longitude", Toast.LENGTH_SHORT).show();
                return;
            }

            double latitude = Double.parseDouble(latitudeStr);
            double longitude = Double.parseDouble(longitudeStr);

            callWeatherApi(latitude, longitude);
        });
    }

    private void callWeatherApi(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService apiService = retrofit.create(WeatherApiService.class);

        Call<WeatherResponse> call = apiService.getWeather(latitude, longitude, API_KEY, "metric");
        fetchWeatherData(call);
    }

    private void fetchWeatherData(Call<WeatherResponse> call) {
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("API Response", "Unsuccessful response: " + response.message());
                    Toast.makeText(ManualLocationActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                WeatherResponse weatherResponse = response.body();
                if (weatherResponse == null) {
                    Log.e("API Response", "Response body is null");
                    Toast.makeText(ManualLocationActivity.this, "Error: Response is null", Toast.LENGTH_SHORT).show();
                    return;
                }

                WeatherDataUtils.populateWeatherList(ManualLocationActivity.this, weatherItemList, weatherResponse);
                weatherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch data: " + t.getMessage(), t);
                Toast.makeText(ManualLocationActivity.this, "Failed to fetch data", Toast.LENGTH_LONG).show();
            }
        });
    }
}
