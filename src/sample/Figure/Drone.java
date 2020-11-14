package sample.Figure;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.UdpPack.UdpPackageReceiver;

public class Drone implements Runnable {
    private GraphicsContext gc;
    private Canvas canvas;
    private UdpPackageReceiver receiver;
    private double height;
    private double latitude;
    private int sizeX, sizeY;
    boolean running = false;
    boolean moving = true;
    boolean takeoff = false;

    public Drone(Canvas canvas, GraphicsContext gc, double latitude, double height, int sizeX, int sizeY, UdpPackageReceiver receiver) {
        this.running=true;
        this.canvas=canvas;
        this.gc=gc;
        this.latitude=latitude;
        this.height=height;
        this.sizeX=sizeX;
        this.sizeY=sizeY;
        this.receiver=receiver;
        //gc = canvas.getGraphicsContext2D();
    }

   public void drawDrone(double latitude, double height) {
       this.latitude=latitude;
       this.height=height;
       gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
       gc.setFill(Color.GRAY);
       gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
       gc.setFill(Color.DARKRED);
       gc.fillOval(latitude, height, sizeX, sizeY);
    }

    @Override
    public void run() {
        drawDrone(latitude, height);
        while (running) {
            if (!takeoff) {
                if (receiver.getReceived().equals("takeoff")) {
                    takeOff();
                }
            }

            while (!moving) {

                notMoving();
            }
            try {
                Thread.sleep(1000);
                System.out.println("hey");
                } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*try {
                System.out.println(receiver.getReceived());
                if (receiver.getReceived().equals("takeoff")) {
                    System.out.println("TAKEOFF INITIATED");
                    takeOff();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }*/

        }
    }

    public void notMoving() {
        try {
                for (int i = 0; i <= 6; i++) {
                    height=height-(i*0.1);
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    gc.setFill(Color.GRAY);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    gc.setFill(Color.DARKRED);
                    gc.fillOval(latitude, height, sizeX, sizeY);
                    Thread.sleep(50);
                }
                for (int i = 6; i >= 0; i--) {
                    height=height+(i*0.1);
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    gc.setFill(Color.GRAY);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    gc.setFill(Color.DARKRED);
                    gc.fillOval(latitude, height, sizeX, sizeY);
                    Thread.sleep(50);
                }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void takeOff() {
        try {
            moving=true;
            for (int i = 100; i > 0; i--) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setFill(Color.GRAY);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setFill(Color.DARKRED);
                gc.fillOval(latitude, height, 50, 20);
                height=height-(i*0.05);
                Thread.sleep(20);
            }
            moving=false;
            takeoff=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
