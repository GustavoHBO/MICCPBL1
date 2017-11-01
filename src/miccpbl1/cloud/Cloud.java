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

    /*Token for split the string*/
    private final String TOKENSEPARATOR = "!=";

    /* Controller for control usage */
    private Controller controllerServer = null;

    /*Port of cloud*/
    private final int port = 12345;

    /* Protocol code size */
    private final int lengthCodeProtocol = 2;

    /* Socket for send packet */
    private DatagramSocket serverSocket = null;
    
    /* Packet for mount the mensage */
    private DatagramPacket receivePacket = null;

    /* Array for store the data receive */
    byte[] receiveData = null;
    
    /* Array for store the data send */
    byte[] sendData = null;

    /*_______________________________________________________________________________________________________________*/
    /**
     * The main method.
     *
     * @param args
     */
    public static void main(String args[]) {
        try {
            new Cloud().startServer();
        } catch (SocketException ex) {
            Logger.getLogger(Cloud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*_______________________________________________________________________________________________________________*/
    /**
     * Initialize the server, for to receiver connections and identify the
     * actions.
     *
     * @throws SocketException
     */
    private void startServer() throws SocketException {
        controllerServer = Controller.getController();
        serverSocket = new DatagramSocket(port);
        receiveData = new byte[1024];

        /* The code below, will save the data in archives .txt */
//        try {
//            controllerServer.readData();// This exception is lunch when try read a string and convert to int
//        } catch (IOException ex) {
//            System.out.println("ERROR: Não foi possível realizar a leitura do banco de dados!");
//        }

        System.out.println("Cloud Iniciado!");
        
        while (true) {
            try {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                Runnable run;
                run = new Runnable() {
                    @Override
                    public void run() {
                        String data = new String(receivePacket.getData());
                        System.out.println("Chegou: " + data);
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
                                     {
                                        try {
                                            controllerServer.registerPacient(data);
                                        } catch (IOException ex) {
                                            System.out.println("ERROR: Não foi possível fazer backup dos dados");
                                        }
                                    }
                                    break;
                                case "01":
                                    System.out.println("Atualizando dados do Paciente");
                                    System.out.println(data);
                                     {
                                        try {
                                            controllerServer.refreshStatusPacient(data);
                                        } catch (IOException ex) {
                                           System.out.println("ERROR: Não foi possível fazer backup dos dados");
                                        }
                                    }
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
                                    sendDatagramPacket("0xS0" + value + "0xS0");// Send the datagram dor server, notify that her are resgistered.
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

    /*_______________________________________________________________________________________________________________*/
    /**
     * Send datagrams for the client connected.
     *
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
