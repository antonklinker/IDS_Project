package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;



public class Controller {
    public Label speed;
    public Label altitude;
    public Button beginButton;
    public Canvas canvas;
    public Label flying;
    private GraphicsContext gc;
    double height;
    double latitude;
    boolean takeoff;

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        height=canvas.getHeight()-30;
        gc.setFill(Color.DARKRED);
        gc.fillRect(canvas.getWidth()/2-15, height, 30, 30);
        takeoff=false;
        setInformation();
    }



    public void begin(ActionEvent actionEvent) {
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


        if (!takeoff) {
            takeOff();
            takeoff=true;
        }
        setInformation();
        gc.setFill(Color.DARKRED);
        gc.fillRect(canvas.getWidth()/2-15, height, 30, 30);
        height--;


    }

    public void takeOff() {
        for (int i=30; i>0; i--) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.GRAY);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.DARKRED);
            gc.fillRect(canvas.getWidth()/2-15, height, 30, 30);
            height--;
        }
    }

    public void setInformation() {
        boolean inAir;
        //double realHeight = ((-height)+(canvas.getHeight())-30);
        if (getHeight()>0) {
            inAir=true;
        } else {
            inAir=false;
        }
        altitude.setText("" + getHeight());
        flying.setText("" + inAir);
    }

    public double getHeight() {
        return ((-height)+(canvas.getHeight())-30);
    }
}
