package part46serialization;

/**
 * Serialization means turns objects into binary form, usually with the aim of
 * saving them to a file. Serialization gives us an easy way to implement save
 * and load functionality in our application. In this video we’ll also finally
 * solve the mystery of those annoying warnings you may have spotted in Eclipse
 * about serial version UIDs, as well as answering the popular interview/exam
 * question “how do you make an object serializable?”.
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadObjects {

    public static void main(String[] args) {
        System.out.println("Reading objects...");
        try (
                FileInputStream fi = new FileInputStream("people.bin");
                ObjectInputStream ois = new ObjectInputStream(fi);
            ) {     
            Person person1 = (Person) ois.readObject();
            Person person2 = (Person) ois.readObject();
            // os.close();
            System.out.println(person1);
            System.out.println(person2);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
