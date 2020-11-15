package sample.Figure;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Controller;
import sample.Information;
import sample.UdpPack.UdpPackageReceiver;

import java.net.UnknownHostException;

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
    int groundHeight;


    // Constructer with the necessary information given from the Controller class
    public Drone(Canvas canvas, GraphicsContext gc, double latitude, double height, int sizeX, int sizeY, UdpPackageReceiver receiver) {
        this.running=true;
        this.canvas=canvas;
        this.gc=gc;
        this.latitude=latitude;
        this.height=height;
        this.sizeX=sizeX;
        this.sizeY=sizeY;
        this.receiver=receiver;
    }

    // Draws the drone on the canvas. This method is used for 2D animation
   public void drawDrone(double latitude, double height) {
       this.latitude=latitude;
       this.height=height;
       gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
       gc.setFill(Color.BLACK);
       gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
       for (int i=0; i<100; i++) {
           gc.setFill(Color.GREEN);
           gc.fillRect(0, i+(i*10), canvas.getWidth(), 1);
           gc.fillRect(i+(i*10), 0, 1, canvas.getHeight());
       }
       gc.setFill(Color.DARKGREEN);
       gc.fillRect(0, canvas.getHeight()-11, canvas.getWidth(), 11);

       //left wing
       gc.setFill(Color.GREEN);
       gc.fillRect(latitude+4, height-10, sizeX-37, sizeY-10);
       gc.fillRect(latitude-3, height-10, sizeX-24, sizeY-18);

       //right wing
       gc.setFill(Color.GREEN);
       gc.fillRect(latitude+31, height-10, sizeX-37, sizeY-10);
       gc.fillRect(latitude+24, height-10, sizeX-24, sizeY-18);

       //body
       gc.setFill(Color.BLACK);
       gc.fillRect(latitude-1, height-1, sizeX+2, sizeY+2);
       gc.setFill(Color.GREEN);
       gc.fillRect(latitude, height, sizeX, sizeY);
       gc.setFill(Color.BLACK);
       gc.fillRect(latitude+1, height+1, sizeX-2, sizeY-2);
       gc.setFill(Color.GREEN);
       gc.fillRect(latitude+2, height+2, sizeX-4, sizeY-4);


    }

    // Draws the drone on the canvas with 3D information. Used to move forward and backward
    // (the string is used to distinguish this method from the other drawDrone method)
    public void drawDrone(int sizeX, int sizeY, String distinguish) {
        this.sizeX=sizeX;
        this.sizeY=sizeY;
        drawDrone(latitude, height);
    }

    public double getHeight() {
        return height;
    }

    @Override
    public void run() {
        groundHeight=(int)height;
        drawDrone(latitude, height);
        while (running) {
            try {
            if (!takeoff) {
                if (receiver.getReceived().equals("takeoff")) {
                    takeOff();
                }
            }

            while (!moving) {
                if (receiver.getReceived().equals("left")) {
                    moveLeft();
                }
                if (receiver.getReceived().equals("right")) {
                    moveRight();
                }
                if (receiver.getReceived().equals("up")) {
                    moveUp();
                }
                if (receiver.getReceived().equals("down")) {
                    moveDown();
                }
                if (receiver.getReceived().equals("forward")) {
                    forward();
                }
                if (receiver.getReceived().equals("back")) {
                    back();
                }
                if (receiver.getReceived().equals("notmoving")) {
                    notMoving();
                }
                if (takeoff) {
                    if (receiver.getReceived().equals("land")) {
                        land();
                    }
                }
                //notMoving();
            }
                Thread.sleep(100);
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Animation for when the drone is stalling
    public void notMoving() {
        try {
                for (int i = 0; i <= 6; i++) {
                    height=height-(i*0.1);
                    drawDrone(latitude, height);
                    Thread.sleep(50);
                }
                for (int i = 6; i >= 0; i--) {
                    height=height+(i*0.1);
                    drawDrone(latitude, height);
                    Thread.sleep(50);
                }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Animating the takeoff from the ground
    public void takeOff() {
        try {
            moving=true;
            for (int i = 100; i > 0; i--) {
                height=height-(i*0.05);
                drawDrone(latitude, height);
                Thread.sleep(20);
            }
            receiver.setReceived("not moving");
            moving=false;
            takeoff=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // The next couple of methods animates 2D movement
    /*public void moveLeft() {
        try {
            moving=true;
            for (int i=15; i>0; i--) {
                latitude=latitude-(i*0.1);
                drawDrone(latitude, height);
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }*/

    public void moveLeft() {
            moving=true;
        latitude--;
        drawDrone(latitude, height);
        receiver.setReceived("not moving");
        moving=false;
    }

    /*public void moveRight() {
        try {
            moving=true;
            for (int i=0; i<15; i++) {
                latitude=latitude+(i*0.1);
                drawDrone(latitude, height);
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }*/

    public void moveRight() {

        moving=true;
        latitude++;
        drawDrone(latitude, height);
        receiver.setReceived("not moving");
        moving=false;
    }

    /*public void moveDown() {
        try {
            moving=true;
            for (int i=0; i<15; i++) {
                height=height +(i*0.1);
                drawDrone(latitude, height);
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }*/

    public void moveDown() {

            moving=true;
        height++;
        drawDrone(latitude, height);

        receiver.setReceived("not moving");
        moving=false;
    }

    /*public void moveUp() {
        try {
            moving=true;
            for (int i=15; i>0; i--) {
                height=height-(i*0.1);
                drawDrone(latitude, height);
                Thread.sleep(5);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }*/
    public void moveUp() {
            moving=true;
        height--;
        drawDrone(latitude, height);
        receiver.setReceived("not moving");
        moving=false;
    }

    // Animates the landing sequence
    public void land() {
        try {
            moving=true;
            while (height<groundHeight) {
                while (height+100<groundHeight) {
                    height=height+3;
                    drawDrone(latitude, height);
                    Thread.sleep(20);
                }
                while (height+50<groundHeight) {
                    height=height+2;
                    drawDrone(latitude, height);
                    Thread.sleep(20);
                }
                height++;
                drawDrone(latitude, height);
                Thread.sleep(20);
            }
            takeoff=false;
            receiver.setReceived("not moving");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Forward and back increases the size of the drone, making it appear close or further away
    public void forward() {
        try {
            moving=true;
            for (int i=0; i<2; i++) {
                sizeX=sizeX-i;
                sizeY=sizeY-i;
                drawDrone(sizeX, sizeY, "");
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }

    public void back() {
        try {
            moving=true;
            for (int i=0; i<2; i++) {
                sizeX=sizeX+i;
                sizeY=sizeY+i;
                drawDrone(sizeX, sizeY, "");
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }
}
