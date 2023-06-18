package com.example.pacepal.view.weather;

public interface WeatherView {
    public void setImage(int id);
    public String getText();
    public void setStatus(String temp, String pressure, String humidity, String main, String description);
}
