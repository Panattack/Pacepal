import java.io.Serializable;

public class Weather implements Serializable {
    private String temperature;
    private String pressure;
    private String humidity;
    private String main;
    private String description;
    private String place;
    
    public Weather(String temperature, String pressure, String humidity, String main, String description, String place) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.main = main;
        this.description = description;
        this.place = place;
    }
    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public String getPressure() {
        return pressure;
    }
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
    public String getHumidity() {
        return humidity;
    }
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    public String getMain() {
        return main;
    }
    public void setMain(String main) {
        this.main = main;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "\n" +
        "Place : " + this.place + "\n" +
        "Temperature : " + this.temperature + " Celsius" + "\n" +
        "Pressure : " + this.pressure + "\n" +
        "Humidity : " + this.humidity + "\n" +
        "Main : " + this.main + "\n" +
        "Description : " + this.description + "\n";
    }
}
