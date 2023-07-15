package com.example.pacepal.view.weather;

public interface WeatherView {
    /**
     * Sets the image based on the weather id
     *
     * @param id indicates the weather as an integer
     */
    public void setImage(int id);

    /**
     * Gets the city
     *
     * @return the written city as a string
     */
    public String getText();

    /**
     * Creates the box with the weather info
     *
     * @param temp        the temperature as a string
     * @param pressure    the pressure as a string
     * @param humidity    the humidity as a string
     * @param main        the main weather as a string
     * @param description the description of the overall weather as a string
     */
    public void setStatus(String temp, String pressure, String humidity, String main, String description);
}
