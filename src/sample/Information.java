package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import sample.Figure.Drone;

public class Information implements Runnable {
    Controller controller;

    boolean running;
    Drone drone;

    public Information(Controller controller, Drone drone) {
        this.controller=controller;
        this.drone=drone;
    }

    @Override
    public void run() {
        running=true;
        while (running) {
            Platform.runLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            int realHeight = (int)(-drone.getHeight()+565);
                            controller.altitude.setText(String.valueOf(realHeight));
                            if (realHeight>0) {
                                controller.flying.setText("Drone is airborne");
                            } else {
                                controller.flying.setText("Drone is on the ground");
                            }
                        }
                    });
            try {

                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
