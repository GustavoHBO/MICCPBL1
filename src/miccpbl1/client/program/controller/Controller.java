/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.program.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import miccpbl1.client.exceptions.IpServerInvalidException;
import miccpbl1.client.exceptions.PortServerInvalidException;
import miccpbl1.model.doctor.Paciente;

/**
 *
 * @author gustavo
 */
public class Controller {

    private String ipServer;
    private int portServer;
    private int rangeRefresh = 5;
    private boolean connected = false;
    private ArrayList<Paciente> listPatient;
    private static Controller controller;
    private DatagramSocket socket;
    private final int LENGTHSERVERPROTOCOL = 4;
    private final int lengthProtocol = 4;
    private final String charSplit = "!=";
    private Thread thread;

    private Thread threadConnection;

    public static Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public static void resetController() {
        controller = null;
    }

    public Controller() {
        this.listPatient = new ArrayList<Paciente>();
    }

    /**
     * @return the listPatient
     */
    public ArrayList<Paciente> getListPatient() {
        return listPatient;
    }

    /**
     * @param listPatient the listPatient to set
     */
    public void setListPatient(ArrayList<Paciente> listPatient) {
        this.listPatient = listPatient;
    }

    private void sendDatagramPacket(String data) {
        try {
            DatagramPacket sendPacket;
            byte[] sendData = data.getBytes();
            InetAddress address = InetAddress.getByName(this.ipServer);
            sendPacket = new DatagramPacket(sendData, sendData.length, address, portServer);
            socket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String receiveData() {
        DatagramPacket receivePacket;
        byte[] receiveData = new byte[1024];
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    socket.receive(receivePacket);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        new Thread(run).start();

        return new String(receivePacket.getData());
    }

    private void sendDatagramPacketSolicitation(String data) throws SocketException {
        try {
            DatagramPacket sendPacket;
            DatagramPacket receivePacket;
            String dataReceived;
            byte[] sendData = data.getBytes();
            byte[] receiveData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            InetAddress address = InetAddress.getByName(this.ipServer);
            sendPacket = new DatagramPacket(sendData, sendData.length, address, portServer);
            socket.send(sendPacket);

            Runnable run;
            run = new Runnable() {
                @Override
                public void run() {
                    try {
                        socket.receive(receivePacket);
                        String dataReceived = new String(receivePacket.getData());
                        System.out.println(dataReceived);

                        String code = dataReceived.substring(0, LENGTHSERVERPROTOCOL);
                        if (code.equals("0x08")) {
                            dataReceived = dataReceived.substring(LENGTHSERVERPROTOCOL);
                            dataReceived = dataReceived.substring(0, dataReceived.lastIndexOf(charSplit) - 1);
                            controller.updateList(dataReceived);
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            new Thread(run).start();

        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateLocalData() throws SocketException {
        String dataSolicitation = "08update08";
        sendDatagramPacketSolicitation(dataSolicitation);
    }

    public void updateList(String data) {
        String[] dataArray = data.split(charSplit);
        Paciente patient;
        int tam = Integer.parseInt(dataArray[0]);
        listPatient = new ArrayList();
        for (int i = 1; i < tam; i += 3) {
            patient = new Paciente(dataArray[i], dataArray[i + 1], dataArray[i + 2]);
        }
    }

    public void testConection(String ip, int porta) throws IpServerInvalidException, PortServerInvalidException, SocketException {
        Runnable run;
        socket = new DatagramSocket();
        this.ipServer = ip;
        this.portServer = porta;

        if (ip == null) {
            throw new IpServerInvalidException();
        } else if (ip.trim().isEmpty() || !ip.matches("\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}")) {
            throw new IpServerInvalidException();
        } else if (porta < 1024 || porta > 65535) {
            throw new PortServerInvalidException();
        } else {

            run = new Runnable() {
                @Override
                public void run() {
                    try {
                        DatagramPacket dataSend, dataReceived;
                        String data, initCode, endCode;
                        int indexLastCode = 0;
                        byte[] send = "09testConnection09".getBytes();
                        byte[] received = new byte[1024];
                        InetAddress ipS = InetAddress.getByName(ipServer);
                        dataSend = new DatagramPacket(send, send.length, ipS, portServer);
                        dataReceived = new DatagramPacket(received, received.length);
                        socket.send(dataSend);
                        socket.receive(dataReceived);
                        System.out.println(new String(dataReceived.getData()));
                        data = new String(dataReceived.getData());
                        initCode = data.substring(0, LENGTHSERVERPROTOCOL);
                        indexLastCode = data.lastIndexOf(initCode);
                        if (indexLastCode == 0) {
                            return;
                        }
                        endCode = data.substring(indexLastCode, indexLastCode + LENGTHSERVERPROTOCOL);
                        //data = data.substring(lengthProtocolServer, indexLastCode);
                        if (initCode.equals(endCode)) {
                            if ("0x09".equals(initCode)) {
                                setConnected(true);
                            }
                        } else {
                            setConnected(false);
                        }
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            this.threadConnection = new Thread(run);
            this.threadConnection.start();
            threadTime(this.threadConnection, rangeRefresh);
        }
    }

    public void connectionServer(String ipServer, String portServer, String nome, String cpf, String numero, String crm) throws SocketException, IpServerInvalidException, UnknownHostException, PortServerInvalidException, IOException {
        socket = new DatagramSocket();
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
            socket.send(sendPacket);

            run = new Runnable() {
                @Override
                public void run() {
                    try {
                        String data;
                        byte[] receiveData = new byte[1024];
                        String codeInit;
                        int lastCodeIndex;
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socket.receive(receivePacket);
                        data = new String(receivePacket.getData());
                        System.out.println("Recebendo:" + new String(receivePacket.getData()));
                        if (!(data.length() > LENGTHSERVERPROTOCOL * 2) || data.trim().isEmpty()) {
                            return;
                        } else {
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
                                sendDatagramPacket("08update08");
                                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                                socket.receive(receivePacket);
                                connected = true;
                                refresh();
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
    
    private void threadTime(Thread thread, int time) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(time);
                    thread.interrupt();
                    System.out.println("Fim de uma thread");
                } catch (InterruptedException ex) {

                }
            }
        };
        Thread threadRun;
        threadRun = new Thread(runnable);
        try {
            threadRun.start();
            TimeUnit.SECONDS.sleep(time + 2);
            threadRun.interrupt();
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * @param connected the connected to set
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    private void refresh(){
        Runnable run;
        run = new Runnable() {
            @Override
            public void run() {
                while (connected) {                    
                    try {
                        updateLocalData();
                    } catch (SocketException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        Thread thread = new Thread(run);
        thread.start();
    }
}
