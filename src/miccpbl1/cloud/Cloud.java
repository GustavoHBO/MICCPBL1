/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.cloud;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import miccpbl1.cloud.controller.Controller;

/**
 *
 * @author gustavo
 */
public class Cloud {
    
    private final String TOKENSEPARATOR = "!=";

    private Controller controllerServer = null;

    private final int port = 12345;

    private final int lengthCodeProtocol = 2;

    private DatagramSocket serverSocket = null;
    private DatagramPacket receivePacket = null;

    byte[] receiveData = null;
    byte[] sendData = null;

    /**
     * The main method.
     * @param args 
     */
    public static void main(String args[]) {
        try {
            new Cloud().startServer();
        } catch (SocketException ex) {
            Logger.getLogger(Cloud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initialize the server, for to receiver connections and identify the actions.
     * @throws SocketException 
     */
    private void startServer() throws SocketException {
        controllerServer = Controller.getController();
        serverSocket = new DatagramSocket(port);
        receiveData = new byte[1024];

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
                        String initCode = data.substring(0, lengthCodeProtocol);
                        int lastCodeIndex = data.lastIndexOf(initCode);
                        if (lastCodeIndex == 0) {
                            return;
                        }
                        String endCode = data.substring(lastCodeIndex, lastCodeIndex + lengthCodeProtocol);
                        if (!initCode.equals(endCode)) {
                            return;
                        } else {
                            data = data.substring(lengthCodeProtocol, lastCodeIndex);
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
                                case "03":
                                    System.out.println("Conectando o cliente!");
                                    String[] dataPatient = data.split(TOKENSEPARATOR);
                                    sendDatagramPacket(controllerServer.personConnect(dataPatient[0], dataPatient[1]));
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
                                case "S0":// The code that start with S* was from a server.
                                    System.out.println("Cadastrando Servidor");
                                    int value = controllerServer.registerServer(data);
                                    sendDatagramPacket("0xS0" + value + "0xS0");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                };
                new Thread(run).start();
            } catch (IOException ex) {
                Logger.getLogger(Cloud.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Send datagrams for the client connected.
     * @param data - Data to send.
     */
    private void sendDatagramPacket(String data) {
        try {
            DatagramPacket sendPacket;
            sendData = data.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
            serverSocket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(Cloud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
