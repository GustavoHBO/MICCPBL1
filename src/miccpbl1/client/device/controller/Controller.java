/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import miccpbl1.client.device.view.exceptions.DataInvalidException;
import miccpbl1.client.device.view.exceptions.InvalidHeartBeatsException;
import miccpbl1.client.device.view.exceptions.IpServerInvalidException;
import miccpbl1.client.device.view.exceptions.NullHeartBeatsException;
import miccpbl1.client.device.view.exceptions.NullStatusMovementException;
import miccpbl1.client.device.view.exceptions.PortServerInvalidException;

/**
 *
 * @author gustavo
 */
public class Controller {

    private static Controller controller = null;
    private String ipServer = null;
    private int portServer = 0;

    public static Controller getController() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public static void resetController() {
        controller = null;
    }

    private void receiverData(String data) {

    }

    private int sendData(String data) throws SocketException, DataInvalidException {
        DatagramSocket socketClient = new DatagramSocket();
        String ipServer = null;
        InetAddress IPAddress = null;
        if (data == null) {
            throw new DataInvalidException();
        } else if (data.trim().isEmpty()) {
            throw new DataInvalidException();
        } else {
            
        }
        return 0;
    }

    public void connectionServer(String ipServer, String portServer) throws SocketException, IpServerInvalidException, UnknownHostException, PortServerInvalidException, IOException {
        DatagramSocket socketClient = new DatagramSocket();
        InetAddress ipAddress = null;
        int port = 0;
        DatagramPacket sendPacket = null;
        byte[] sendData = null;
        Runnable run = null;

        if (ipServer == null) {
            throw new IpServerInvalidException();
        } else if (ipServer.trim().isEmpty() || !ipServer.matches("\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}")) {
            throw new IpServerInvalidException();
        } else if (portServer == null) {
            throw new PortServerInvalidException();
        } else if (portServer.trim().isEmpty() || !portServer.matches("\\d{4}\\d?")) {
            throw new PortServerInvalidException();
        } else {
            ipAddress = InetAddress.getByName(ipServer);
            sendData = "09testeConnection09".getBytes();
            port = Integer.parseInt(portServer);
            if(port < 1024 || port > 65535){
                throw new PortServerInvalidException();
            }
            this.portServer = port;
            this.ipServer = ipServer;
            sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
            socketClient.send(sendPacket);
            
            
            
            run = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(".run()");
                        DatagramSocket socketClient = new DatagramSocket();
                        byte[] receiveData = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        while(true){
                            socketClient.receive(receivePacket);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            Thread thread = new Thread(run);
            thread.start();
        }

    }

    public void cadastrarPaciente(String nome, String cpf, String numero, int btmCardiacos, boolean statusMovimento, String pressaoSanguinea, boolean acEspecial) throws SocketException, DataInvalidException {

        String data = "00";
        data += nome + "!-" + cpf + "!-" + numero + "!-" + btmCardiacos + "!-" + statusMovimento + "!-" + pressaoSanguinea + "!-" + acEspecial + "00";
        sendData(data);
    }

    public void updateStatusPatient(String btmCardiacos, String statusMovimento, String pressaoSanguinea, String patientRisk) throws NullHeartBeatsException, InvalidHeartBeatsException, NullStatusMovementException, SocketException, DataInvalidException {

        String data = "01";
        if (btmCardiacos == null) {
            throw new NullHeartBeatsException();
        } else if (btmCardiacos.trim().isEmpty()) {
            throw new InvalidHeartBeatsException();
        }

        if (statusMovimento == null) {
            throw new NullStatusMovementException();
        }
        data += btmCardiacos + "!-" + statusMovimento + "!-" + pressaoSanguinea + "!-" + patientRisk + "01";
        sendData(data);
    }

    public void updateRangeRefresh(String time) throws SocketException, DataInvalidException {
        String data = "02";
        data += time + "02";
        sendData(data);
    }

    public boolean comfirmationDataReceiverServer(String code) {
        String dataReceived;

        return true;
    }
}
