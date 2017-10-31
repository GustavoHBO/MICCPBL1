/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import miccpbl1.server.controller.Controller;

/**
 *
 * @author gustavo
 */
public class Server {
    
    private Controller controllerServer = null;
    
    private final int port = 12346;
    
    private String ipCloud;
    private int portCloud;
    
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
            System.out.println("ERROR: Não é possível estabelecer qualquer conexão!");
        } catch (UnknownHostException ex) {
            System.out.println("ERROR: O Host não é reconhecido!");
        }
    }
    
    private void startServer() throws SocketException, UnknownHostException {
        controllerServer = Controller.getController();
        serverSocket = new DatagramSocket(port);
        receiveData = new byte[1024];

        /* Now, the server will send a packet to cloud for register the server*/
        sendDatagramPacket(mountDataRegisterCloud(), InetAddress.getByName(ipCloud), portCloud);
        System.out.println("Resposta do Cloud:" + replyServer());
        
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
                                    sendDatagramPacket(stri, receivePacket.getAddress(), receivePacket.getPort());
                                    break;
                                case "09":
                                    System.out.println("Testando Conexão");
                                    System.out.println(data);
                                    sendDatagramPacket("0x09testSucessful0x09", receivePacket.getAddress(), receivePacket.getPort());
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
    
    private void sendDatagramPacket(String data, InetAddress ip, int port) {
        try {
            DatagramPacket sendPacket;
            sendData = data.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
            serverSocket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String replyServer() {
        byte[] dataReceive;
        DatagramPacket packetReceive;
        String data = "";
        String codeProtocol, lastCodeProtocol;
        int indexLastCode;
        
        dataReceive = new byte[1024];
        packetReceive = new DatagramPacket(dataReceive, dataReceive.length);
        try {
            serverSocket.receive(packetReceive);//To await the reply of server.
            data = new String(packetReceive.getData());// Convert the data bytes for char.
            if(data.trim().isEmpty()){
                return data;
            }
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
    
    /**
     * This method registers these server on cloud, sending the IP, Port, Position X and Position Y. This method ask some
     * data using the prompt.
     * @return dataSend - If the data inserted are valid, null - In case of an error in reading the input data.
     */
    public String mountDataRegisterCloud() {
        /* Now, the server will send a packet to cloud for register the ip */
        String dataSend = ""; // This will contains the ip.
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));// IOS - For read in terminal the ip and port of the cloud.
        try {
            System.out.println("Bem-Vindo!");
            String input;
            dataSend = "S0" + InetAddress.getLocalHost().getHostAddress() + controllerServer.getTOKENSEPARATOR() + port + controllerServer.getTOKENSEPARATOR() + controllerServer.getPosX()+ controllerServer.getTOKENSEPARATOR() + controllerServer.getPosY() +"S0";
            do {
                System.out.println("\nDigite o IP do servidor Cloud!");
                input = inFromUser.readLine();
            } while (!input.matches("\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}"));
            this.ipCloud = input;
            input = "";
            do {
                System.out.println("\nDigite a PORTA do servidor Cloud!");
                input = inFromUser.readLine();
            } while (!input.matches("\\d{4}\\d?"));
            this.portCloud = Integer.parseInt(input);
        } catch (IOException ex) {
            System.out.println("ERROR: Impossível ler os dados de entrada!");
        }
        return dataSend;
    }
}
