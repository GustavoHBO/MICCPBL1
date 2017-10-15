/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import miccpbl1.client.exceptions.IpServerInvalidException;
import miccpbl1.client.exceptions.PortServerInvalidException;

/**
 *
 * @author gustavo
 */
public class TestConnection implements Runnable{

    @Override
    public void run() {
        
    }
    
    public void connection(String ipServer, String portServer) throws SocketException, IpServerInvalidException, PortServerInvalidException, UnknownHostException, IOException{
        DatagramSocket socketClient = new DatagramSocket();
        InetAddress ipAddress = null;
        int port = 0;
        DatagramPacket sendPacket = null;
        byte[] sendData = null;
        byte[] receiveData = null;

        if (ipServer == null) {
            throw new IpServerInvalidException();
        } else if (ipServer.trim().isEmpty() || !ipServer.matches("\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}")) {
            throw new IpServerInvalidException();
        } else if (portServer == null) {
            throw new PortServerInvalidException();
        } else if (portServer.trim().isEmpty() || !portServer.matches("\\d{5}")) {
            throw new PortServerInvalidException();
        } else {
            ipAddress = InetAddress.getByName(ipServer);
            sendData = "09testeConnection09".getBytes();
            port = Integer.parseInt(portServer);
            if(port < 1024 || port > 65535){
                throw new PortServerInvalidException();
            }
            sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
            socketClient.send(sendPacket);
        }
    }
    
}
