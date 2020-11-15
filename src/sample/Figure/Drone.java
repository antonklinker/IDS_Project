package sample.Figure;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Information;
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
    int groundHeight;


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
       gc.setFill(Color.BLACK);
       gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
       for (int i=0; i<100; i++) {
           gc.setFill(Color.GREEN);
           gc.fillRect(0, i+(i*10), canvas.getWidth(), 1);
           gc.fillRect(i+(i*10), 0, 1, canvas.getHeight());
       }
       gc.setFill(Color.BLACK);
       gc.fillOval(latitude-1, height-1, sizeX+2, sizeY+2);
       gc.setFill(Color.GREEN);
       gc.fillOval(latitude, height, sizeX, sizeY);
       gc.setFill(Color.BLACK);
       gc.fillOval(latitude+1, height+1, sizeX-2, sizeY-2);
       gc.setFill(Color.GREEN);
       gc.fillOval(latitude+2, height+2, sizeX-4, sizeY-4);
    }

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
                if (takeoff) {
                    if (receiver.getReceived().equals("land")) {
                        land();
                    }
                }
                notMoving();
            }
                Thread.sleep(50);
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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


    public void moveLeft() {
        try {
            moving=true;
            for (int i=10; i>0; i--) {
                latitude=latitude-(i*0.1);
                drawDrone(latitude, height);
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }

    public void moveRight() {
        try {
            moving=true;
            for (int i=0; i<10; i++) {
                latitude=latitude+(i*0.1);
                drawDrone(latitude, height);
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }

    public void moveDown() {
        try {
            moving=true;
            for (int i=0; i<10; i++) {
                height=height +(i*0.1);
                drawDrone(latitude, height);
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }

    public void moveUp() {
        try {
            moving=true;
            for (int i=10; i>0; i--) {
                height=height-(i*0.1);
                drawDrone(latitude, height);
                Thread.sleep(5);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.setReceived("not moving");
        moving=false;
    }

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
