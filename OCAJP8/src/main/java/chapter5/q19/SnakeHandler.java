/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.q19;

public class SnakeHandler {

    private Snake snake;

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public static void main(String[] args) {
        new SnakeHandler().setSnake(new Cobra());
        new SnakeHandler().setSnake(new GardenSnake());
        new SnakeHandler().setSnake(new Snake());
        new SnakeHandler().setSnake(new Object());
        new SnakeHandler().setSnake(new String("String"));
        new SnakeHandler().setSnake(null);

    }
}
