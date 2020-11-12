package sample.Figure;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Drone implements Runnable {
    private GraphicsContext gc;
    private double height;
    private double latitude;
    private int[] size = new int[2];

    public Drone(GraphicsContext gc, double height, double latitude) {
        this.gc=gc;
        this.height=height;
        this.latitude=latitude;
        this.size[0]=50;
        this.size[1]=20;
    }

    @Override
    public void run() {

    }

   /* public void drawDrone() {
        gc.setFill(Color.DARKRED);
        gc.fillOval(latitude, height, size[0], size[1]);
    }*/
}
