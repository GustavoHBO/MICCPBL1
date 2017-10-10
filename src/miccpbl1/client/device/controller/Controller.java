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
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import miccpbl1.client.device.view.exceptions.DataInvalidException;
import miccpbl1.client.device.view.exceptions.InvalidHeartBeatsException;
import miccpbl1.client.device.view.exceptions.IpServerInvalidException;
import miccpbl1.client.device.view.exceptions.NullHeartBeatsException;
import miccpbl1.client.device.view.exceptions.NullStatusMovementException;
import miccpbl1.client.device.view.exceptions.PortServerInvalidException;
import miccpbl1.model.Paciente;

/**
 *
 * @author gustavo
 */
public class Controller {

    private static Controller controller;
    private String ipServer;
    private int portServer;
    private int rangeRefresh = 5;
    private final int LENGTHSERVERPROTOCOL = 4;
    private final int LENGTHPROTOCOL = 2;
    private final String CHARSPLIT = "!=";

    private DatagramSocket socketClient;

    private boolean connected = false;
    private boolean randomData = true;

    private Thread thread;
    private Thread threadRefresh;

    private Paciente patient;

    public static Controller getController() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public static void resetController() {
        controller = null;
    }

    private void receiveData(String data) {

    }

    public void connectionServer(String ipServer, String portServer, String nome, String cpf, String numero) throws SocketException, IpServerInvalidException, UnknownHostException, PortServerInvalidException, IOException {
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
            this.portServer = port;
            this.ipServer = ipServer;
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

                                    cadastrarPaciente(nome, cpf, numero);
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

    public void cadastrarPaciente(String nome, String cpf, String numero) throws SocketException, DataInvalidException {
        patient = new Paciente(cpf, nome, numero);
        String data = "00";
        data += nome + CHARSPLIT + cpf + CHARSPLIT + numero + "00";
        sendDatagramPacket(data);
    }

    public void updateStatusPatient(String btmCardiacos, String statusMovimento, String pressaoSanguinea) throws NullHeartBeatsException, InvalidHeartBeatsException, NullStatusMovementException, SocketException, DataInvalidException {
        if (patient == null) {
            return;
        } else {
            String data = "01";
            data += patient.getCPF() + CHARSPLIT + btmCardiacos + CHARSPLIT + statusMovimento + CHARSPLIT + pressaoSanguinea + "01";
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
            if (!connected) {
                return;
            }
            DatagramPacket sendPacket;
            byte[] sendData = data.getBytes();
            this.ipServer = "127.0.0.1";
            this.portServer = 12345;
            InetAddress address = InetAddress.getByName(this.ipServer);
            sendPacket = new DatagramPacket(sendData, sendData.length, address, this.portServer);
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

    public void testConnection(int time) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                byte[] dataReceive;
                DatagramPacket packetReceive;
                sendDatagramPacket("09testConnection09");
                String data;
                String codeProtocol, lastCodeProtocol;
                int indexLastCode;
                do {
                    dataReceive = new byte[1024];
                    packetReceive = new DatagramPacket(dataReceive, dataReceive.length);
                    data = new String(packetReceive.getData());
                    codeProtocol = data.substring(0, LENGTHSERVERPROTOCOL);
                    indexLastCode = data.lastIndexOf(codeProtocol);
                    lastCodeProtocol = data.substring(indexLastCode, indexLastCode + LENGTHSERVERPROTOCOL);
                    data = data.substring(0, indexLastCode);
                    if (codeProtocol.equals(lastCodeProtocol)) {
                        System.out.println(codeProtocol);
                        System.out.println(data);
                        if (codeProtocol == "0x09") {
                            connected = true;
                        } else {
                            connected = false;
                        }
                    } else {
                        System.out.println("A informação recebida tem códigos diferentes!");
                        return;
                    }

                } while (connected);
            }
        };
        Thread threadRun = new Thread(runnable);
        threadRun.start();
        threadTime(threadRun, time);
    }

    private void threadTime(Thread thread, int time) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(time);
                    thread.interrupt();
                    System.out.println("Fim de uma thread");
                } catch (InterruptedException ex) {
                    System.out.println("A thread não foi encerrada");
                }
            }
        };
        Thread threadRun;
        threadRun = new Thread(runnable);
        thread.start();
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
}
