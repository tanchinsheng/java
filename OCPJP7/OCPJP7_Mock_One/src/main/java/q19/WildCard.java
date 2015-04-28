/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q19;

class WildCard {

    interface BI {
    }

    interface DI extends BI {
    }

    interface DDI extends DI {
    }

    static class C<T> {
    }

    static void foo(C<? super DI> arg) {
    }

    public static void main(String[] args) {
        foo(new C<BI>());
        foo(new C<DI>());
        foo(new C<DDI>());
        foo(new C());
    }

}
