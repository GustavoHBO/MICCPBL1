/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import miccpbl1.server.controller.Controller;

/**
 *
 * @author gustavo
 */
public class Server {

    private Controller controllerServer = null;

    private final int port = 12345;

    private final int LENGTHCODEPROTOCOL = 2;
    private final int LENGTHCODEPROTOCOLSERVER = 4;

    private DatagramSocket serverSocket = null;
    private DatagramPacket receivePacket = null;

    byte[] receiveData = null;
    byte[] sendData = null;

    public static void main(String args[]) {
        try {
            new Server().startServer();
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startServer() throws SocketException {
        controllerServer = Controller.getController();
        serverSocket = new DatagramSocket(port);
        receiveData = new byte[1024];

        /* Now, the server will send a packet to cloud for register the server*/
        sendDatagramPacket(controllerServer.mountDataRegisterCloud());
        
        while (true) {
            try {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                Runnable run;
                run = new Runnable() {
                    @Override
                    public void run() {
                        String data = new String(receivePacket.getData());
                        identifyAction(data);
                    }

                    private void identifyAction(String data) {
                        String initCode = data.substring(0, LENGTHCODEPROTOCOL);
                        int lastCodeIndex = data.lastIndexOf(initCode);
                        if (lastCodeIndex == 0) {
                            return;
                        }
                        String endCode = data.substring(lastCodeIndex, lastCodeIndex + LENGTHCODEPROTOCOL);
                        if (!initCode.equals(endCode)) {
                            return;
                        } else {
                            data = data.substring(LENGTHCODEPROTOCOL, lastCodeIndex);
                            switch (initCode) {
                                case "00":
                                    System.out.println("Paciente Registrado!");
                                    System.out.println(data);
                                    controllerServer.registerPacient(data);
                                    break;
                                case "01":
                                    System.out.println("Atualizando dados do Paciente");
                                    System.out.println(data);
                                    controllerServer.refreshStatusPacient(data);
                                    break;
                                case "08":
                                    String stri = controllerServer.mountListPatient();
                                    sendDatagramPacket(stri);
                                    break;
                                case "09":
                                    System.out.println("Testando Conexão");
                                    System.out.println(data);
                                    sendDatagramPacket("0x09testSucessful0x09");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                };
                new Thread(run).start();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void sendDatagramPacket(String data) {
        try {
            DatagramPacket sendPacket;
            sendData = data.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            serverSocket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String replyServer() {
        byte[] dataReceive;
        DatagramPacket packetReceive;
        String data = null;
        String codeProtocol, lastCodeProtocol;
        int indexLastCode;

        dataReceive = new byte[1024];
        packetReceive = new DatagramPacket(dataReceive, dataReceive.length);
        try {
            serverSocket.receive(packetReceive);//To await the reply of server.
            data = new String(packetReceive.getData());// Convert the data bytes for char.
            codeProtocol = data.substring(0, LENGTHCODEPROTOCOLSERVER);// Get the code of protocol.
            data = data.substring(LENGTHCODEPROTOCOLSERVER);// Cut the first code.
            indexLastCode = data.lastIndexOf(codeProtocol);// Get the last index of code.
            lastCodeProtocol = data.substring(indexLastCode, indexLastCode + LENGTHCODEPROTOCOLSERVER);// Get the last code.
            data = data.substring(0, indexLastCode);// Now data have only the data
            if (!codeProtocol.equals(lastCodeProtocol)) {// If the protocol is wrong the data is discarded.
                data = null;
            }
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);// Logger default
        }
        return data;
    }
}
