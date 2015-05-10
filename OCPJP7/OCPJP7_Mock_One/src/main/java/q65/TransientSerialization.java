/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q65;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class USPresident implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String name;
    private final String period;
    private static transient String term; // retained

    @Override
    public String toString() {
        return "US President [name=" + name + ",period=" + period + ",term=" + term + "]";
    }

    public USPresident(String name, String period, String term) {
        this.name = name;
        this.period = period;
        this.term = term;
    }

}

public class TransientSerialization {

    public static void main(String[] args) {
        USPresident usPresident = new USPresident("Barack Obama", "2009 to --", "56th term");
        System.out.println(usPresident);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("USPresident.data"))) {
            oos.writeObject(usPresident);
        } catch (IOException ex) {
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("USPresident.data"))) {
            Object obj = ois.readObject();
            if (obj != null && obj instanceof USPresident) {
                USPresident presidentOfUS = (USPresident) obj;
                System.out.println(presidentOfUS);
            }
        } catch (IOException ioe) {
        } catch (ClassNotFoundException e) {
        }
    }

}
