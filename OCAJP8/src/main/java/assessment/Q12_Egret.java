/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assessment;

class Q12_Egret {

    private String color;

    public Q12_Egret() {
        this("white");
    }

    public Q12_Egret(String color) {
        color = color;
    }

    public static void main(String[] args) {
        Q12_Egret e = new Q12_Egret();
        System.out.println("Color: " + e.color);
    }

}
