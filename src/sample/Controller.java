package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import sample.Figure.Drone;
import sample.UdpPack.UdpPackage;
import sample.UdpPack.UdpPackageReceiver;

import java.io.IOException;
import java.net.*;


public class Controller {
    public Label speed;
    public Label altitude;
    public Canvas canvas;
    public Label flying;
    public Button udpbutton;
    public TextField testmessagebox;
    private GraphicsContext gc;
    double height;
    double latitude;
    boolean takeoff;


    private ObservableList<UdpPackage> savedPackages = FXCollections.observableArrayList();
    private ObservableList<UdpPackage> loggedPackages = FXCollections.observableArrayList();

    private UdpPackageReceiver receiver;
    private DatagramSocket sender;
    private Drone drone;
    private Thread listenTimer;
    private Information information;


    public void initialize() throws UnknownHostException {
        /*UdpPackage test1 = new UdpPackage("name", "data", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000,4000);
        UdpPackage test2 = new UdpPackage("name", "hello world", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000,4000);
        loggedPackages.addAll(test1, test2)*/;




        receiver = new UdpPackageReceiver(loggedPackages, 6000);
        new Thread(receiver).start();


        try {
            sender = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }



        gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        height=canvas.getHeight()-30;
        latitude=canvas.getWidth()/2-25;

        //gc.setFill(Color.DARKRED);

        drone = new Drone(canvas, gc, latitude, height, 50, 20, receiver);
        new Thread(drone).start();
        information = new Information(this, drone);
        new Thread(information).start();
        //drone.drawDrone(latitude, height);
        //gc.fillOval(latitude, height, 50, 20);

        takeoff=false;
    }

    public void sendUdpMessage(ActionEvent actionEvent) {

        // sends a basic test message to localhost port 6000!

        String message = testmessagebox.getText();
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName("127.0.0.1"), 6000);
            sender.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*public double getHeight() {
        return ((-height)+(canvas.getHeight())-30);
    }

    public void moveUp() {
        height=height-5;
    }

    public void listen() {

    }*/

}
