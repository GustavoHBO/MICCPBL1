/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.cloud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import miccpbl1.model.Medico;
import miccpbl1.model.Paciente;

/**
 * Class of control the connections between the servers and clients. Storage too
 * the data of patients in risk, servers and doctors.
 *
 * @author Gustavo
 */
public class Controller implements Serializable {

    private final String TOKENSEPARATOR = "!=";

    private static Controller controller = null;
    private final ArrayList<Paciente> listaPacientes = new ArrayList<>();
    private final ArrayList<Medico> listDoctor = new ArrayList<>();
    private final ArrayList<String> listServers = new ArrayList<>();

    /*_______________________________________________________________________________________________________________*/

    /**
     * Singleton getController
     *
     * @return controller - Current controller instance.
     */
    public static Controller getController() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Reset the controller
     */
    public static void resetController() {
        controller = null;
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Register the patient on cloud.
     *
     * @param data - Data about the patient.
     */
    public void registerPacient(String data) {
        String nome;
        String cpf;
        String numero;

        Paciente paciente;
        String[] splitData = data.split(TOKENSEPARATOR);

        nome = splitData[0];
        cpf = splitData[1];
        numero = splitData[2];
        paciente = new Paciente(nome, cpf, numero);

        if (findPacient(cpf) == null) {
            System.out.println("Cadastrando Paciente");
            listaPacientes.add(paciente);
        } else {
            System.out.println("Não Cadastrando Paciente");
            return;
        }

    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Register the server on cloud.
     *
     * @param data - Data with ip and location of server.
     * @return -1 - Case the parameter data is wrong, 0 - Case exist server with same ip, 1 - Case the server have been registered.
     */
    public int registerServer(String data) {
        if (data == null) {
            return -1;
        } else if (data.trim().isEmpty()) {
            return -1;
        } else {
            String ip = splitString(data, TOKENSEPARATOR)[2];
            if(findServer(ip) == null){
                listServers.add(data);//The data will come formated.
                return 1;
            }
        }
        return 0;
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Method for update the status of patient, these case, if the data is
     * received, then the patient is in risk.
     *
     * @param data - cpf*btCardiaco*repouso*pressao*risco, * = string separator.
     */
    public void refreshStatusPacient(String data) {
        String[] splitedData = splitString(data, TOKENSEPARATOR);
        Paciente patient = findPacient(splitedData[0]);
        if (patient == null) {
            return;
        } else {
            System.out.println("Atualizando dados do paciente");
            patient.getListBtCardiacos().add(splitedData[1]);
            patient.getListRepouso().add(splitedData[2]);
            patient.getListPressaoSanguinea().add(splitedData[3]);
            patient.getListAcEspecial().add("Risk");
        }
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Search the patient registered using the cpf as parameter.
     *
     * @param cpf - Identifier of patient.
     * @return patient - Case exist patient, null case not.
     */
    private Paciente findPacient(String cpf) {
        Iterator<Paciente> it = listaPacientes.iterator();
        Paciente findPacient;
        while (it.hasNext()) {
            findPacient = it.next();
            if (findPacient.getCPF().equals(cpf)) {
                return findPacient;
            }
        }
        return null;
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Method for split the string received.
     *
     * @param string - String.
     * @param splitString - String separator.
     * @return string - String divided.
     */
    private String[] splitString(String string, String splitString) {
        return string.split(splitString);// Use the method java.
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Return string with the data of patients that are on string received.
     *
     * @param patients - Cpf's of patients.
     * @return string - String with the patients chosen.
     */
    private String getDataPatients(String[] patients) {
        String data = new String();
        int i = 0;
        Iterator<Paciente> it = listaPacientes.iterator();
        Paciente patient;
        while (it.hasNext()) {
            patient = it.next();
            if (patients[i].equals(patient.getCPF())) {
                data += patient.getCPF();
                data += TOKENSEPARATOR;
                data += patient.getListBtCardiacos().get(0);
                data += TOKENSEPARATOR;
                data += patient.getListPressaoSanguinea().get(0);
                data += TOKENSEPARATOR;
                data += patient.getListRepouso().get(0);
                data += TOKENSEPARATOR;
                data += patient.getListAcEspecial().get(0);
                if (it.hasNext()) {
                    data += TOKENSEPARATOR;
                }
            }
        }
        return data;
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Search the doctor using the cpf.
     *
     * @param cpf - Identifier of doctor.
     * @return doctor - Case exist, null - Case not exist.
     */
    private Medico findDoctor(String cpf) {
        Iterator<Medico> it = listDoctor.iterator();
        Medico findDoctor;
        while (it.hasNext()) {
            findDoctor = it.next();
            if (findDoctor.getCPF().equals(cpf)) {
                return findDoctor;
            }
        }
        return null;
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Find the server by ip.
     * @param ip - Ip server.
     * @return dataServer - Data about the server.
     */
    public String findServer(String ip) {
        String dataServer;
        String[] ipServer;
        Iterator<String> it = listServers.iterator();
        if (ip == null || ip.trim().isEmpty()) {
            return null;
        }
        while(it.hasNext()){
            dataServer = it.next();
            ipServer = splitString(dataServer, TOKENSEPARATOR);
            if(ipServer[2].equals(ip)){
                return dataServer;
            }
        }
        return null;
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Mount the list with all data of patients
     *
     * @return string - Data of all the patients.
     */
    public String mountListPatient() {
        String listPat;
        listPat = "0x08";
        Paciente pat;
        Iterator<Paciente> it = listaPacientes.iterator();
        while (it.hasNext()) {
            pat = it.next();
            listPat += pat.getCPF();
            listPat += TOKENSEPARATOR;
            listPat += pat.getNome();
            listPat += TOKENSEPARATOR;
            listPat += pat.getNumero();
            if (it.hasNext()) {
                listPat += TOKENSEPARATOR;
            }
        }
        listPat += "0x08";
        return listPat;
    }

    /*_______________________________________________________________________________________________________________*/
    
    /**
     * Return the ip of server based on distance. Calculate with relation with
     * triangle.
     *
     * @param x - Position in x.
     * @param y - Position in y.
     * @return ip - Ip of server, null - Case not exist server.
     */
    public String getIpServerByLocation(int x, int y) {
        String ip;
        String[] serverData;
        Iterator<String> it = listServers.iterator();
        double vx, vy, dist;
        if (listServers.isEmpty()) {
            return null;
        }
        serverData = listServers.get(0).split(TOKENSEPARATOR);

        vx = Integer.parseInt(serverData[0]); // Get the cordenate in x.
        vy = Integer.parseInt(serverData[1]); // Get the cordenate in y.
        ip = serverData[2];//It get the first ip, because the first is the more close.
        dist = Math.sqrt(vx * vx + vy * vy);// Use baskara for calculate the distance, use the relation of triangle.
        while (it.hasNext()) {
            serverData = it.next().split(TOKENSEPARATOR);
            vx = Integer.parseInt(serverData[0]); // Get the cordenate in x.
            vy = Integer.parseInt(serverData[1]); // Get the cordenate in y.
            if (dist > Math.sqrt(vx * vx + vy * vy)) {
                ip = serverData[2];
            }
        }
        return ip;
    }
}
