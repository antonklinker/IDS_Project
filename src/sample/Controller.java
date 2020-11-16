package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
    public Canvas batteryLevel;
    public Canvas mainbackground;
    public TableView tableViewLog;
    public TableColumn logTime;
    public TableColumn logFromIP;
    public TableColumn logFromPort;
    public TableColumn logToIP;
    private GraphicsContext gc;
    private GraphicsContext mgc;
    public GraphicsContext bgc;
    private String message;
    private String ESP_IPaddress;
    double height;
    double latitude;
    boolean takeoff;


    //private ObservableList<UdpPackage> savedPackages = FXCollections.observableArrayList();
    //private ObservableList<UdpPackage> loggedPackages = FXCollections.observableArrayList();

    private UdpPackageReceiver receiver;
    private DatagramSocket sender;
    private Drone drone;
    private Thread listenTimer;
    private Information information;


    public void initialize() throws UnknownHostException {
        /*UdpPackage test1 = new UdpPackage("name", "data", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000,4000);
        UdpPackage test2 = new UdpPackage("name", "hello world", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000,4000);
        loggedPackages.addAll(test1, test2);*/

        // not the correct address of the ESP of course, but this is where we'll store it
        ESP_IPaddress = "127.0.0.1";

        /*tableViewLog.setItems(loggedPackages);

        //set columns content
        logTime.setCellValueFactory(
                new PropertyValueFactory<UdpPackage,String>("formattedDate")
        );
        logFromIP.setCellValueFactory(
                new PropertyValueFactory<UdpPackage, String>("fromIp")
        );
        logFromPort.setCellValueFactory(
                new PropertyValueFactory<UdpPackage, Integer>("fromPort")
        );
        logToIP.setCellValueFactory(
                new PropertyValueFactory<UdpPackage, String>("toIp")
        );


        receiver = new UdpPackageReceiver(loggedPackages, 6000);

         */

        // initializes a new thread with the sole purpose of handling UDP messages on port 6000.
        receiver = new UdpPackageReceiver(6000);
        new Thread(receiver).start();


        try {
            sender = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }


        gc = canvas.getGraphicsContext2D();
        bgc = batteryLevel.getGraphicsContext2D();
        mgc = mainbackground.getGraphicsContext2D();
        mgc.setFill(Color.DARKBLUE);
        mgc.setGlobalAlpha(0.5);
        for (int i=0; i<mainbackground.getWidth()/15; i++) {
            mgc.fillRect(i+(i*15), 0, 1, mainbackground.getHeight());
        }
        for (int i=0; i<mainbackground.getHeight()/15; i++) {
            mgc.fillRect(0, i+(i*15), mainbackground.getWidth(), 1);
        }


        height=canvas.getHeight()-30;
        latitude=canvas.getWidth()/2-25;


        drone = new Drone(canvas, gc, latitude, height, 40, 20, receiver);
        new Thread(drone).start();
        information = new Information(this, drone);
        new Thread(information).start();

        takeoff=false;
    }

    // this method is called when you press the button in the top right
    // will send a USP message from the emulator to the drone (from itself to itself)
    public void sendUdpMessageToDrone(ActionEvent actionEvent) {

        String message = testmessagebox.getText();
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName("127.0.0.1"), 6000);
            sender.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // essentially the same method as the one above, but this can be called without pushing a button
    public void sendUdpMessageToDrone() {

        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName("127.0.0.1"), 6000);
            sender.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sends a UDP message to the controller
    public void sendUdpMessageToESP() {

        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(ESP_IPaddress), 6900);
            sender.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMessage() {
        return receiver.getReceived();
    }

    public UdpPackageReceiver getReceiver() {
        return receiver;
    }

    public void setMessage(String message) {
        this.message=message;
    }


}
