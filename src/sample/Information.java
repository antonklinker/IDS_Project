package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import sample.Figure.Drone;

import java.util.ArrayList;

public class Information implements Runnable {
    Controller controller;
    private ArrayList<Integer> battery = new ArrayList<>();

    int batterycountdown = 50;
    boolean running;
    Drone drone;
    private boolean crashed;

    public Information(Controller controller, Drone drone) {
        this.controller=controller;
        this.drone=drone;
    }

    @Override
    public void run() {
        crashed=false;
        running=true;
        for (int i=0; i<=10; i++) {
            battery.add(i);
        }
        while (running) {
            Platform.runLater(
                    new Runnable() {
                        @Override
                        public void run() {

                            int realHeight = (int)(-drone.getHeight()+565);
                            controller.altitude.setText(String.valueOf(realHeight));
                            controller.speed.setText(String.valueOf((int)drone.getLatitude()));
                            batterycountdown--;
                            if (realHeight>0) {
                                controller.flying.setText("Drone is airborne");
                            } else {
                                controller.flying.setText("Drone is on the ground");
                                if (batterycountdown==0) {
                                    if (battery.size()<=10) {
                                        battery.add(1);
                                        controller.setMessage("Battery level: " + battery.size() + "/11");
                                        System.out.println("Battery level: " + battery.size() + "/11");
                                        controller.sendUdpMessageToESP();
                                    }
                                    batterycountdown=50;
                                }
                            }

                            if (battery.size()>0) {
                                if (batterycountdown == 0) {
                                    battery.remove(battery.size() - 1);
                                    controller.setMessage("Battery level: " + battery.size() + "/11");
                                    System.out.println("Battery level: " + battery.size() + "/11");
                                    controller.sendUdpMessageToESP();
                                    batterycountdown = 50;
                                }

                                controller.bgc.clearRect(0, 0, controller.batteryLevel.getWidth(), controller.batteryLevel.getHeight());
                                controller.bgc.setFill(Color.BLACK);
                                controller.bgc.fillRect(0, 0, controller.batteryLevel.getWidth()-11, controller.batteryLevel.getHeight());
                                for (int i = 0; i < battery.size(); i++) {
                                    if (battery.size() > 5) {
                                        controller.bgc.setFill(Color.GREEN);
                                    } else if (battery.size() > 2) {
                                        controller.bgc.setFill(Color.YELLOW);
                                    } else {
                                        controller.bgc.setFill(Color.RED);
                                    }
                                    controller.bgc.fillRect(i + (i * 10), 0, 10, controller.batteryLevel.getHeight());
                                }
                            }
                            if (battery.size()==0 && crashed==false) {
                                controller.setMessage("crash");
                                controller.sendUdpMessageToDrone();
                                crashed=true;
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
