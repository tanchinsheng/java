package com.company.pretest;

interface Side {

    String getSide();
}

class Head implements Side {

    public String getSide() {
        return "Head ";
    }
}

class Tail implements Side {

    public String getSide() {
        return "Tail ";
    }
}

class Coin {

    public static void overload(Head side) {
        System.out.print(side.getSide());
    }

    public static void overload(Tail side) {
        System.out.print(side.getSide());
    }

    public static void overload(Side side) {
        System.out.print("Side ");
    }

    public static void overload(Object side) {
        System.out.print("Object ");
    }

    public static void main(String[] args) {
        // Overloading is based on the static type of the objects 
        // (while overriding and runtime resolution resolves to the
        // dynamic type of the objects). Here is how the calls to the overload() 
        // method are resolved:
        
        // Side Object Tail Side
        
        Side firstAttempt = new Head();
        Tail secondAttempt = new Tail();
        // overload(firstAttempt); --> firstAttempt is of type Side, 
        // hence it resolves to overload(Side).
        overload(firstAttempt);
        //overload((Object)firstAttempt); -> firstAttempt is casted to Object, 
        // hence it resolves to overload(Object).
        overload((Object) firstAttempt);
        // overload(secondAttempt); -> secondAttempt is of type Tail, hence it resolves to
        // overload(Tail).
        overload(secondAttempt);
        // overload((Side)secondAttempt); -> secondAttempt is casted to Side, 
        // hence it resolves to overload(Side).
        overload((Side) secondAttempt);
    }
}
