
import java.io.Serializable;

public class Temperature implements Serializable {

    private double temp;

    public Temperature() {
        temp = 0.0;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double t) {
        this.temp = t;
    }

    public void cToF() {
        this.temp = this.temp * (9.0 / 5.0) + 32.0;
    }

    public void fToC() {
        this.temp = (this.temp - 32.0) * (5.0 / 9.0);
    }

}
