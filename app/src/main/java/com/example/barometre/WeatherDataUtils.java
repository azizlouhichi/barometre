package com.example.barometre;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherDataUtils {

    public static void populateWeatherList(Context context, List<WeatherItem> weatherItemList, WeatherResponse weatherResponse) {
        weatherItemList.clear();

        weatherItemList.add(new WeatherItem(
                "City",
                "Location: " + weatherResponse.name,
                R.drawable.smart_city,
                "The name of the city where the weather data is collected."
        ));
        weatherItemList.add(new WeatherItem(
                "Temperature",
                "Current Temperature: " + weatherResponse.main.temp + "°C",
                R.drawable.temperature,
                "The current temperature measured in Celsius."
        ));
        weatherItemList.add(new WeatherItem(
                "Feels Like",
                "Feels Like: " + weatherResponse.main.feels_like + "°C",
                R.drawable.sentiment_analysis,
                "An estimate of how the temperature feels to the human body, considering factors like wind and humidity."
        ));
        weatherItemList.add(new WeatherItem(
                "Min/Max Temp",
                "Min/Max Temperature: " + weatherResponse.main.temp_min + "°C / " + weatherResponse.main.temp_max + "°C",
                R.drawable.temperatures,
                "The lowest and highest temperatures expected during the day."
        ));
        weatherItemList.add(new WeatherItem(
                "Pressure",
                "Atmospheric Pressure: " + weatherResponse.main.pressure + " hPa",
                R.drawable.ic_pressure,
                "The pressure exerted by the atmosphere at the location, measured in hectopascals (hPa)."
        ));
        weatherItemList.add(new WeatherItem(
                "Humidity",
                "Humidity Level: " + weatherResponse.main.humidity + "%",
                R.drawable.ic_humidity,
                "The percentage of moisture present in the air. High humidity often makes it feel warmer."
        ));
        weatherItemList.add(new WeatherItem(
                "Wind Speed",
                "Wind Speed: " + weatherResponse.wind.speed + " m/s",
                R.drawable.ic_wind,
                "The speed of the wind, measured in meters per second. High wind speeds can affect outdoor activities."
        ));
        weatherItemList.add(new WeatherItem(
                "Wind Direction",
                "Wind Direction: " + weatherResponse.wind.deg + "°",
                R.drawable.cardinal_points,
                "The direction from which the wind is blowing, in degrees (0° = North, 90° = East, etc.)."
        ));
        weatherItemList.add(new WeatherItem(
                "Cloudiness",
                "Cloud Cover: " + weatherResponse.clouds.all + "%",
                R.drawable.cloudy,
                "The percentage of the sky covered by clouds. Higher values indicate more cloud cover."
        ));
        weatherItemList.add(new WeatherItem(
                "Sunrise",
                "Sunrise Time: " + formatUnixTime(weatherResponse.sys.sunrise),
                R.drawable.sunrise,
                "The local time when the sun will rise."
        ));
        weatherItemList.add(new WeatherItem(
                "Sunset",
                "Sunset Time: " + formatUnixTime(weatherResponse.sys.sunset),
                R.drawable.sunset,
                "The local time when the sun will set."
        ));
    }

    private static String formatUnixTime(long unixTime) {
        Date date = new Date(unixTime * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(date);
    }
}
