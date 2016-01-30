package q436.inheritance;

interface Account {

    public default String getId() {
        return "0000";
    }
}

interface PremiumAccount extends Account {

    public class app {

        public static void main(String[] args) {

        }

    }
}
