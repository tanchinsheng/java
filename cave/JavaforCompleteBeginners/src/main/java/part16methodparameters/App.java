package part16methodparameters;

class Robot {
    public void speak(String text) {
        System.out.println(text);
    }
    public void jump(int height) {
        System.out.println("Jumping: " + height);
    }
    public void move(String direction, double distance) {
        System.out.println("Moving " + distance + "metres in direction " + direction);
    }
}

public class App {
    public static void main(String[] args) {
        Robot sam = new Robot();
        sam.speak("Hi I am Sam");
        sam.jump(7);
        sam.move("West", 12.2);
        String greeting = "Hello there."; // reference to class
        sam.speak(greeting);
        int value = 14; //value
        sam.jump(value);
    }
}
