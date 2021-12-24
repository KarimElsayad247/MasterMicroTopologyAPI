/**
 * Simple POJO to store attributes
 */
public class Attributes {
    private String name;
    private double defaultValue;
    private double minValue;
    private double maxValue;

    public Attributes(String name, double defaultValue, double minValue, double maxValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String getName() {
        return name;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }
}
