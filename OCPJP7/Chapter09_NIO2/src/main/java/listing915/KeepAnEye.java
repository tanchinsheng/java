

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class KeepAnEye {

//    Create a WatchService "watcher" for the file system.
//    For each directory that you want monitored, register it with the watcher.
//    When registering a directory, you specify the type of events for which you want notification.
//    You receive a WatchKey instance for each directory that you register.
//    Implement an infinite loop to wait for incoming events.
//    When an event occurs, the key is signaled and placed into the watcher's queue.
//    Retrieve the key from the watcher's queue. You can obtain the file name from the key.
//    Retrieve each pending event for the key (there might be multiple events) and process as needed.
//    Reset the key, and resume waiting for events.
//    Close the service: The watch service exits when either the thread exits or when it is closed (by invoking its closed method).
    public static void main(String[] args) {
        //The Path class implements the Watchable interface
        Path path = Paths.get("src");
        WatchService watchService = null;
        try {
            watchService = path.getFileSystem().newWatchService();
            // When registering an object with the watch service,
            // you specify the types of events that you want to monitor.
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //infinite loop
        for (; watchService != null;) {
            // WatchKeys are thread-safe
            // A watch key has a state. At any given time, its state might be one of the following:\
            // Ready indicates that the key is ready to accept events.
            //When first created, a key is in the ready state.
            //Signaled indicates that one or more events are queued.
            //Once the key has been signaled, it is no longer in the ready state until the reset method is invoked.
            //Invalid indicates that the key is no longer active. This state happens when one of the following events occurs:
            //      The process explicitly cancels the key by using the cancel method.
            //      The directory becomes inaccessible.
            //      The watch service is closed.
            WatchKey key = null;
            try {
                // Returns a queued key. If no queued key is available, this method waits.
                key = watchService.take();
                System.out.println("key's filename : " + key.watchable());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // iterate for each event
            // Process the pending events for the key.
            // You fetch the List of WatchEventsfrom the pollEvents method.
            for (WatchEvent<?> event : key.pollEvents()) //Retrieve the type of event by using the kind method.
            // No matter what events the key has registered for,
            // it is possible to receive an OVERFLOW event.
            // You can choose to handle the overflow or ignore it, but you should test for it.
            {
                // Retrieve the file name associated with the event.
                // The file name is stored as the context of the event, so the context method is used to retrieve it.
                switch (event.kind().name()) {
                    case "OVERFLOW":
                        System.out.println("We lost some events");
                        break;
                    case "ENTRY_MODIFY":
                        System.out.println("File " + event.context() + " is changed!");
                        break;
                }
            }
            //resetting the key is important to receive subsequent notifications
            key.reset();
        }
    }
}
