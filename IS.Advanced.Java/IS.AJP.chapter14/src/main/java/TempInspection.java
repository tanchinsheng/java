
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TempInspection {

    public static void main(String[] args) throws IntrospectionException {
        try {
            Class c1 = Class.forName("Temperature");
            BeanInfo beanInfo = Introspector.getBeanInfo(c1);
            System.out.println("Properties of Temperature class: ");
            PropertyDescriptor propertyDescriptor[] = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptor) {
                System.out.println(pd.getName());
            }
            System.out.println("Methods of Temperature class: ");
            MethodDescriptor methodDescriptor[] = beanInfo.getMethodDescriptors();
            for (MethodDescriptor md : methodDescriptor) {
                System.out.println(md.getName());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TempInspection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
