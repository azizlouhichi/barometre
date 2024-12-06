package com.example.barometre;
public class WeatherItem {

    private final String detail;
    private int ic;
    private String label;
    private String value;

    public WeatherItem(String label, String value, int ic, String detail) {
        this.label = label;
        this.value = value;
        this.ic = ic;
        this.detail = detail;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
    public int getIc() {
        return ic;
    }

    public String getDetail(){
        return detail;
    }

}
