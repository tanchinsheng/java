package listing1403;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.util.concurrent.Exchanger;

class CoffeeShopThread extends Thread {

    private final Exchanger<String> sillyTalk;

    public CoffeeShopThread(Exchanger<String> args) {
        sillyTalk = args;
    }

    @Override
    public void run() {
        String reply = null;
        try {
            // exchange the first messages
            reply = sillyTalk.exchange("[CS] Who's there?");
            // print what Duke said
            System.out.println("[CS] Duke: " + reply);

            // exchange second message
            reply = sillyTalk.exchange("[CS] Duke who?");
            // print what Duke said
            System.out.println("[CS] Duke: " + reply);

            // there is no message to send, but to get a message from Duke thread,
            // both ends should send a message; so send a "dummy" string
            reply = sillyTalk.exchange("[CS]  ");
            System.out.println("[CS] Duke: " + reply);
        } catch (InterruptedException ie) {
            System.err.println("Got interrupted during my silly talk");
        }
    }
}
