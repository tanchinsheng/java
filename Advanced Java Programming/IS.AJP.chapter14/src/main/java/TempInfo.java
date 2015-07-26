
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TempInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor temp = new PropertyDescriptor("temp", Temperature.class);
            PropertyDescriptor pd[] = {temp};
            return pd;
        } catch (IntrospectionException ex) {
            Logger.getLogger(TempInfo.class.getName()).log(Level.SEVERE, "Exception thrown.", ex);
        }
        return null;
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        try {
            Class cl = Temperature.class;
            Class args[] = {};
            Method cToF = cl.getMethod("cToF", args);
            MethodDescriptor cToFDesc = new MethodDescriptor(cToF);
            cToFDesc.setShortDescription("Convert Celsius to Fahrenheit");
            Method fToC = cl.getMethod("fToC", args);
            MethodDescriptor fToCDesc = new MethodDescriptor(fToC);
            fToCDesc.setShortDescription("Convert Fahrenheit to Celsius");
            MethodDescriptor[] md = {cToFDesc, fToCDesc};
            return md;
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(TempInfo.class.getName()).log(Level.SEVERE, "Exception thrown.", ex);
        }
        return null;
    }

}
