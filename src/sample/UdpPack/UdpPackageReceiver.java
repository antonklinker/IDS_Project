package sample.UdpPack;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import sample.Controller;
import sample.UdpPack.UdpPackage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

public class UdpPackageReceiver implements Runnable {

    boolean running = false;
    DatagramSocket socket;
    private byte[] buf = new byte[256];
    int port;
    private String received;


    List udpPackages;


    public UdpPackageReceiver(List udpPackages, int port) {
        this.running = true;
        this.udpPackages = udpPackages;
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        running = false;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    @Override
    public void run() {
        received = "WELCOME";
        while (running) {
                            DatagramPacket packet = new DatagramPacket(buf, buf.length);
                            try {
                                socket.receive(packet);
                                System.out.println("package arrived!");
                                UdpPackage udpPackage = new UdpPackage("name", packet.getData(), packet.getAddress(), socket.getLocalAddress(), packet.getPort(), socket.getLocalPort());
                                udpPackages.add(udpPackage);
                                received
                                        = new String(packet.getData(), 0, packet.getLength());
                                //System.out.println(udpPackage.getDataAsString());
                                if (getReceived().equals("hey")) {
                                    System.out.println("hey yourself");
                                }
                                //controller.loggedPackages.add(udpPackage);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

    }
}
