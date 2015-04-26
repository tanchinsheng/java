package listing1405;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.util.concurrent.Phaser;
// The work could be a Cook, Helper, or Attendant. Though the three work independently, the
// should all synchronize their work together to do their part and complete preparing a food item

class Worker extends Thread {

    Phaser deliveryOrder;

    Worker(Phaser order, String name) {
        deliveryOrder = order;
        this.setName(name);
        deliveryOrder.register();
        this.start();
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("[Worker," + deliveryOrder.getPhase() + ","
                    + deliveryOrder.getRegisteredParties() + ","
                    + deliveryOrder.getArrivedParties() + ","
                    + deliveryOrder.getUnarrivedParties()
                    + "]" + "\t" + getName() + " doing his work for order no. " + i);
            if (i == 3) {
                // work completed for this delivery order, so deregister
                deliveryOrder.arriveAndDeregister();
            } else {
                deliveryOrder.arriveAndAwaitAdvance();
            }
            try {
                Thread.sleep(3000); // simulate time for preparing the food item
            } catch (InterruptedException ie) {
                /* ignore exception */
                ie.printStackTrace();
            }
        }
    }
}

//Starting to process the delivery order
//[Worker,0,3,0,3]	Cook doing his work for order no. 1
//[Worker,0,4,2,2]	Helper doing his work for order no. 1
//[Worker,0,4,3,1]	Attendant doing his work for order no. 1
//[PO,1,4,0,4] Deliver food item no. 1
//[Worker,1,4,1,3]	Attendant doing his work for order no. 2
//[Worker,1,4,2,2]	Cook doing his work for order no. 2
//[Worker,1,4,3,1]	Helper doing his work for order no. 2
//[PO,2,4,0,4] Deliver food item no. 2
//[Worker,2,4,1,3]	Attendant doing his work for order no. 3
//[Worker,2,4,1,3]	Helper doing his work for order no. 3
//[Worker,2,4,1,3]	Cook doing his work for order no. 3
//[PO,3,1,0,1] Deliver food item no. 3
//Delivery order completed... give it to the customer
//
//Starting to process the delivery order
//[Worker,0,4,0,4]	Cook doing his work for order no. 1
//[Worker,0,4,2,2]	Helper doing his work for order no. 1
//[Worker,0,4,3,1]	Attendant doing his work for order no. 1
//[PO,1,4,0,4] Deliver food item no. 1
//[Worker,1,4,1,3]	Attendant doing his work for order no. 2
//[Worker,1,4,2,2]	Cook doing his work for order no. 2
//[Worker,1,4,2,2]	Helper doing his work for order no. 2
//[PO,2,4,0,4] Deliver food item no. 2
//[Worker,2,4,1,3]	Cook doing his work for order no. 3
//[Worker,2,4,1,3]	Helper doing his work for order no. 3
//[Worker,2,4,1,2]	Attendant doing his work for order no. 3
//[PO,3,1,0,1] Deliver food item no. 3
//Delivery order completed... give it to the customer
