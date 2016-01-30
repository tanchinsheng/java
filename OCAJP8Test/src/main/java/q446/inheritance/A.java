package q446.inheritance;

class A {

    protected int i;

    A(int i) {
        this.i = i;
    }

    public static void main(String[] args) {

    }

}
// 1 : Insert code here

class B {
}

class C extends A {
}

//Since class B does not have any constructor, the compiler will try to insert the default constructor,
// which will look like this:
// B(){     
//  super();  //Notice that it is trying to call the no args constructor of the super class, A.
// }
//Since A doesn't have any no-args constructor, the above code will fail to compile.
class D extends A {

    D() {
        System.out.println("i = " + i);
    }
}//It has the same problem as the one above.

class E {

    E() {
    }
}
