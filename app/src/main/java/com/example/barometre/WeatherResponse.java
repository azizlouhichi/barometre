package com.example.barometre;

import java.util.List;

public class WeatherResponse {

    public Coord coord;
    public List<Weather> weather;
    public Main main;
    public Wind wind;
    public Clouds clouds;
    public Sys sys;
    public String name;
    public int timezone;

    public class Coord {
        public double lon;
        public double lat;
    }

    public class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public class Main {
        public float temp;
        public float feels_like;
        public float temp_min;
        public float temp_max;
        public int pressure;
        public int humidity;
    }

    public class Wind {
        public float speed;
        public int deg;
        public float gust;
    }

    public class Clouds {
        public int all;
    }

    public class Sys {
        public int type;
        public int id;
        public String country;
        public long sunrise;
        public long sunset;
    }
}
