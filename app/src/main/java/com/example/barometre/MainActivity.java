package com.example.barometre;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "6fe49650e98edcabbcea05edb4d5ec20";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private RecyclerView weatherRecyclerView;
    private WeatherAdapter weatherAdapter;
    private List<WeatherItem> weatherItemList = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        Button manualInputButton = findViewById(R.id.manualInputButton);
        manualInputButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ManualLocationActivity.class);
            startActivity(intent);
        });
        weatherRecyclerView = findViewById(R.id.weatherRecyclerView);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(weatherItemList);
        weatherRecyclerView.setAdapter(weatherAdapter);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            callWeatherApi(latitude, longitude);
                        } else {
                            Toast.makeText(MainActivity.this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission is required to fetch weather data", Toast.LENGTH_SHORT).show();
            }
        }
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
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                WeatherResponse weatherResponse = response.body();
                if (weatherResponse == null) {
                    Log.e("API Response", "Response body is null");
                    Toast.makeText(MainActivity.this, "Error: Response is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                populateWeatherData(weatherResponse);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch data: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateWeatherData(WeatherResponse weatherResponse) {
        WeatherDataUtils.populateWeatherList(this, weatherItemList, weatherResponse);
        weatherAdapter.notifyDataSetChanged();
    }
}
