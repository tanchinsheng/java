package part43innernestedclasses;

/**
Java lets you declare classes almost anywhere, even inside other classes and methods. 
* In this tutorial we’ll take a look at some of the possibilities and why you might 
* want to make use of them. In particular, we’ll look at inner classes, static inner
* classes and local classes.
 */
public class App {
 
     
    public static void main(String[] args) {
     
        Robot robot = new Robot(7);
        robot.start();
         
        // The syntax below will only work if Brain is
        // declared public. It is quite unusual to do this.
        // Robot.Brain brain = robot.new Brain();
        // brain.think();
         
        // This is very typical Java syntax, using
        // a static inner class.
        Robot.Battery battery = new Robot.Battery();
        battery.charge();
    }
 
}
