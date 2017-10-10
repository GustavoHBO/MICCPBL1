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

    private final int lengthCodeProtocol = 2;

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
                            if (initCode.equals("00")) {
                                System.out.println("Paciente Registrado!");
                                System.out.println(data);
                                controllerServer.registerPacient(data);

                            } else if (initCode.equals("01")) {
                                System.out.println("Atualizando dados do Paciente");
                                System.out.println(data);
                                controllerServer.refreshStatusPacient(data);
                            } else if (initCode.equals("08")) {
                                String stri = controllerServer.mountListPatient();
                                sendDatagramPacket(stri);
                            } else if (initCode.equals("09")) {
                                System.out.println("Testando Conexão");
                                System.out.println(data);
                                sendDatagramPacket("0x09testSucessful0x09");
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
}
