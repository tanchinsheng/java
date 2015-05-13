/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q13;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Point2D implements Externalizable {

    private int x, y;

    public Point2D(int x, int y) {
        x = x;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("Point " + x + ":" + y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }

    public static void main(String[] args) {
        Point2D point = new Point2D(10, 20);
        System.out.println(point);
    }

}
