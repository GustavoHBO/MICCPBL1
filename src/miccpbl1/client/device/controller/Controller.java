/*
 * To change this license header, choose License Headers in Project Properties.  
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import miccpbl1.client.exceptions.DataInvalidException;
import miccpbl1.client.exceptions.InvalidHeartBeatsException;
import miccpbl1.client.exceptions.IpServerInvalidException;
import miccpbl1.client.exceptions.NullHeartBeatsException;
import miccpbl1.client.exceptions.NullStatusMovementException;
import miccpbl1.client.exceptions.PortServerInvalidException;
import miccpbl1.model.Pessoa;

/**
 *
 * @author gustavo
 */
public class Controller implements Serializable {

    private static Controller controller;
    private String ipServer = "127.0.0.1";
    private int portServer = 12345;
    private int rangeRefresh = 5;
    private final int LENGTHSERVERPROTOCOL = 4;
    private final int LENGTHPROTOCOL = 2;
    private final String CHARSPLIT = "!=";

    private DatagramSocket socketClient;

    private boolean connected = false;
    private boolean connectedAccount = false;
    private boolean randomData = true;

    private Thread thread;
    private Thread threadRefresh;

    private Pessoa pessoa;

    public static Controller getController() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public static void resetController() {
        controller = null;
    }

    private Controller() {
    }

    ;
    
    private void receiveData(String data) {

    }

    public void connectionServer(String ipServer, String portServer) throws SocketException, IpServerInvalidException, UnknownHostException, PortServerInvalidException, IOException {
        socketClient = new DatagramSocket();
        InetAddress ipAddress;
        int port;
        DatagramPacket sendPacket;
        byte[] sendData;
        Runnable run;

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
            if (port < 1024 || port > 65535) {
                throw new PortServerInvalidException();
            }
            this.setPortServer(port);
            this.setIpServer(ipServer);
            sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
            socketClient.send(sendPacket);

            run = new Runnable() {
                @Override
                public void run() {
                    try {
                        String data;
                        byte[] receiveData = new byte[1024];
                        String codeInit;
                        int lastCodeIndex;
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socketClient.receive(receivePacket);
                        data = new String(receivePacket.getData());
                        System.out.println("Recebendo:" + new String(receivePacket.getData()));
                        if (!(data.length() > LENGTHSERVERPROTOCOL * 2) || data.trim().isEmpty()) {
                            return;
                        } else {
                            System.out.println(data);
                            codeInit = data.substring(0, LENGTHSERVERPROTOCOL);
                            System.out.println(codeInit);
                            lastCodeIndex = data.lastIndexOf(codeInit);
                            if (lastCodeIndex == -1) {
                                return;
                            }
                            System.out.println(lastCodeIndex);
                            data = data.substring(LENGTHSERVERPROTOCOL, lastCodeIndex);
                            if (data.equals("testSucessful")) {
                                System.out.println("Mensagem enviada e recebida corretamente");
                                connected = true;
                                try {
                                    //cadastrarPaciente(nome, cpf, numero, senha);
                                    updateRandom();
                                } catch (SocketException | DataInvalidException | InterruptedException | NullHeartBeatsException | InvalidHeartBeatsException | NullStatusMovementException ex) {
                                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                connected = false;
                            }
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            if (thread == null) {
                this.thread = new Thread(run);
                thread.start();
            }
        }

    }

    public void cadastrarPaciente(String nome, String cpf, String numero, String senha, String posX, String posY) throws SocketException {
        pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setCPF(cpf);
        pessoa.setNumero(numero);
        pessoa.setSenha(senha);
        pessoa.setX(Integer.parseInt(posX));
        pessoa.setY(Integer.parseInt(posY));
        String data = "00";
        data += cpf + getCHARSPLIT() + nome + getCHARSPLIT() + numero + getCHARSPLIT() + senha + getCHARSPLIT() + posX + getCHARSPLIT() + posY + "00";
        sendDatagramPacket(data);
    }

    public void updateStatusPatient(String btmCardiacos, String statusMovimento, String pressaoSanguinea) throws NullHeartBeatsException, InvalidHeartBeatsException, NullStatusMovementException, SocketException, DataInvalidException {
        if (pessoa == null) {
            return;
        } else {
            String data = "01";
            data += pessoa.getCPF() + getCHARSPLIT() + btmCardiacos + getCHARSPLIT() + statusMovimento + getCHARSPLIT() + pressaoSanguinea + "01";
            sendDatagramPacket(data);
        }
    }

    public void updateRangeRefresh(String time) throws SocketException, DataInvalidException {
        String data = "02";
        data += time + "02";
        sendDatagramPacket(data);
    }

    private void sendDatagramPacket(String data) {

        try {
            if (socketClient == null) {
                socketClient = new DatagramSocket();
            }
            if (socketClient.isClosed()) {
                socketClient = new DatagramSocket();
            }
            DatagramPacket sendPacket;
            byte[] sendData = data.getBytes();
            InetAddress address = InetAddress.getByName(this.getIpServer());
            sendPacket = new DatagramPacket(sendData, sendData.length, address, this.getPortServer());
            socketClient.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateRandom() throws InterruptedException, NullHeartBeatsException, InvalidHeartBeatsException, NullStatusMovementException, SocketException, DataInvalidException {
        Random random = new Random();
        String btmCardiacos;
        String statusMovimento;
        String pressaoSanguinea;
        String patientRisk;
        String data;
        while (randomData) {
            btmCardiacos = Integer.toString(random.nextInt(131) + 30);
            statusMovimento = random.nextBoolean() ? "Em Movimento" : "Em Repouso";
            pressaoSanguinea = Integer.toString(random.nextInt(61) + 80) + "/" + Integer.toString(random.nextInt(61) + 60);
            patientRisk = random.nextBoolean() ? "Risk" : "Not Risk";
            try {
                updateStatusPatient(btmCardiacos, statusMovimento, pressaoSanguinea);
            } catch (NullHeartBeatsException | InvalidHeartBeatsException | NullStatusMovementException | SocketException | DataInvalidException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            TimeUnit.SECONDS.sleep(rangeRefresh);
        }
    }

    /**
     * Method for test the connection with cloud.
     *
     * @return true - If the server responds.
     */
    public boolean testConnection() {
        try {
            socketClient = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        Runnable runnable;
        runnable = new Runnable() {
            @Override
            public void run() {
                byte[] dataReceive;
                DatagramPacket packetReceive;
                sendDatagramPacket("09testConnection09");
                String data;
                String codeProtocol, lastCodeProtocol;
                int indexLastCode;
                try {
                    dataReceive = new byte[1024];
                    packetReceive = new DatagramPacket(dataReceive, dataReceive.length);
                    socketClient.receive(packetReceive);
                    data = new String(packetReceive.getData());
                    codeProtocol = data.substring(0, LENGTHSERVERPROTOCOL);
                    indexLastCode = data.lastIndexOf(codeProtocol);
                    lastCodeProtocol = data.substring(indexLastCode, indexLastCode + LENGTHSERVERPROTOCOL);
                    data = data.substring(0, indexLastCode);
                    if (codeProtocol.equals(lastCodeProtocol)) {
                        System.out.println(codeProtocol);
                        System.out.println(data);
                        connected = codeProtocol == "0x09";
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    finalize();
                } catch (Throwable ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);// Catch Default
                }
            }
        };
        Thread threadRun = new Thread(runnable);
        threadRun.start();
        return connected;
    }

    public void connectAccount(String cpf, String senha) {

        String data = "";
        data = "03" + cpf + getCHARSPLIT() + senha + "03";
        sendDatagramPacket(data);
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
            socketClient.receive(packetReceive);//To await the reply of server.
            data = new String(packetReceive.getData());// Convert the data bytes for char.
            System.out.println("Chegou: " + data);
            System.out.println(data);
            codeProtocol = data.substring(0, LENGTHSERVERPROTOCOL);// Get the code of protocol.
            data = data.substring(LENGTHSERVERPROTOCOL);// Cut the first code.
            indexLastCode = data.lastIndexOf(codeProtocol);// Get the last index of code.
            lastCodeProtocol = data.substring(indexLastCode, indexLastCode + LENGTHSERVERPROTOCOL);// Get the last code.
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
     * @param rangeRefresh the rangeRefresh to set
     */
    public void setRangeRefresh(int rangeRefresh) {
        if (rangeRefresh < 1) {
            return;
        }
        this.rangeRefresh = rangeRefresh;
    }

    /**
     * @param randomData the randomData to set
     */
    public void setRandomData(boolean randomData) {
        this.randomData = randomData;
    }

    /**
     * @return the connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * @return the connectedAccount
     */
    public boolean isConnectedAccount() {
        return connectedAccount;
    }

    /**
     * @return the CHARSPLIT
     */
    public String getCHARSPLIT() {
        return CHARSPLIT;
    }

    /**
     * @return the ipServer
     */
    public String getIpServer() {
        return ipServer;
    }

    /**
     * @param ipServer the ipServer to set
     */
    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

    /**
     * @return the portServer
     */
    public int getPortServer() {
        return portServer;
    }

    /**
     * @param portServer the portServer to set
     */
    public void setPortServer(int portServer) {
        this.portServer = portServer;
    }
}
