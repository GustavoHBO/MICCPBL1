/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.cloud.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

    private Controller() {
    }

    /*_______________________________________________________________________________________________________________*/
    /**
     * Register the patient on cloud.
     *
     * @param data - Data about the patient.
     * @throws java.io.IOException
     */
    public void registerPacient(String data) throws IOException {
        String nome;
        String cpf;
        String numero;
        String senha;
        int posX, posY;

        Paciente paciente;
        String[] splitData = data.split(TOKENSEPARATOR);

        if (splitData.length < 4) {
            return;
        }

        cpf = splitData[0];
        nome = splitData[1];
        numero = splitData[2];
        senha = splitData[3];
        posX = Integer.parseInt(splitData[4]);
        posY = Integer.parseInt(splitData[5]);
        paciente = new Paciente(cpf, nome, numero, senha);
        paciente.setX(posX);
        paciente.setY(posY);

        if (findPacient(cpf) == null) {
            listaPacientes.add(paciente);
        } else {
            return;
        }
        saveDataPatient();
    }

    /*_______________________________________________________________________________________________________________*/
    /**
     * Register the server on cloud.
     *
     * @param data - Data with ip and location of server.
     * @return -1 - Case the parameter data is wrong, 0 - Case exist server with
     * same ip, 1 - Case the server have been registered.
     */
    public int registerServer(String data) {
        if (data == null) {
            return -1;
        } else if (data.trim().isEmpty()) {
            return -1;
        } else {
            String ip = splitString(data, TOKENSEPARATOR)[0];
            if (findServer(ip) == null) {
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
    public void refreshStatusPacient(String data) throws IOException {
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
        saveDataPatient();
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
     *
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
        while (it.hasNext()) {
            dataServer = it.next();
            ipServer = splitString(dataServer, TOKENSEPARATOR);
            if (ipServer[0].equals(ip)) {
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
        String server, otherServer;
        String[] serverData;
        Iterator<String> it;
        double vx, vy, dist;
        if (listServers.isEmpty()) {
            return "";
        }
        it = listServers.iterator();
        server = listServers.get(0);
        serverData = server.split(TOKENSEPARATOR);//Divide the string in a array.

        vx = Integer.parseInt(serverData[2]); // Get the cordenate in x.
        vy = Integer.parseInt(serverData[3]); // Get the cordenate in y.
        dist = Math.sqrt(vx - x * vx - x + vy - y * vy - y);// Use baskara for calculate the distance, use the relation of triangle.
        while (it.hasNext()) {
            otherServer = it.next();
            serverData = otherServer.split(TOKENSEPARATOR);
            vx = Integer.parseInt(serverData[2]); // Get the cordenate in x.
            vy = Integer.parseInt(serverData[3]); // Get the cordenate in y.
            if (dist > Math.sqrt(vx - x * vx - x + vy - y * vy - y)) {
                dist = Math.sqrt(vx - x * vx - x + vy - y * vy - y);
                server = otherServer;
            }
        }
        return server;
    }

    /**
     * This method returns the person using the cpf and password registered.
     *
     * @param cpf - Identification the patient.
     * @param senha - Password the patient.
     * @return person - If the patient exist and the cpf and password are
     * correct, null - Otherwise.
     */
    public String personConnect(String cpf, String senha) {
        Paciente patient = findPacient(cpf);
        if (patient == null) {
            return "0x030x03";
        } else if (patient.getSenha().equals(senha)) {
            String ipServer = getIpServerByLocation(patient.getX(), patient.getY());
            if (ipServer == null) {
                ipServer = "";//If the ip is null
            }
            return "0x03" + patient.getCPF() + TOKENSEPARATOR + patient.getNome() + TOKENSEPARATOR + patient.getNumero() + TOKENSEPARATOR + ipServer + "0x03";
        }
        return "0x030x03";
    }

    private void saveDataServer() throws IOException {

        OutputStream os;
        OutputStreamWriter osw;
        BufferedWriter bw;
        Iterator<String> it;
        String server;

        File fileServers = new File("./backup/servers");
        if (!fileServers.exists()) {
            fileServers.mkdirs();
        }
        fileServers = new File("./backup/servers/data.iot");
        if (!fileServers.exists()) {
            fileServers.createNewFile();
        }

        os = new FileOutputStream("./backup/servers/data.iot");
        osw = new OutputStreamWriter(os);
        bw = new BufferedWriter(osw);

        bw.write(listServers.size());
        bw.newLine();
        it = listServers.iterator();
        while (it.hasNext()) {
            server = it.next();
            bw.write(server);
            bw.newLine();
        }
        bw.close();
        osw.close();
        os.close();
    }

    private void saveDataDoctor() throws IOException {

        OutputStream os;
        OutputStreamWriter osw;
        BufferedWriter bw;
        Iterator<Medico> it;
        Medico doctor;

        File fileDoctors = new File("./backup/doctors");
        if (!fileDoctors.exists()) {
            fileDoctors.mkdirs();
        }
        fileDoctors = new File("./backup/doctors/data.iot");
        if (!fileDoctors.exists()) {
            fileDoctors.createNewFile();
        }

        os = new FileOutputStream("./backup/doctors/data.iot");
        osw = new OutputStreamWriter(os);
        bw = new BufferedWriter(osw);

        it = listDoctor.iterator();
        bw.write(listDoctor.size());
        bw.newLine();
        while (it.hasNext()) {
            doctor = it.next();
            bw.write(doctor.getCPF());
            bw.newLine();
            bw.write(doctor.getCrm());
            bw.newLine();
            bw.write(doctor.getNome());
            bw.newLine();
            bw.write(doctor.getNumero());
            bw.newLine();
            bw.write(doctor.getSenha());
            bw.newLine();
            bw.write(doctor.getX());
            bw.newLine();
            bw.write(doctor.getY());
            bw.newLine();
        }
        bw.close();
        osw.close();
        os.close();
    }

    private void saveDataPatient() throws IOException {

        OutputStream os;
        OutputStreamWriter osw;
        BufferedWriter bw;

        File filePatients = new File("./backup/patients");
        if (!filePatients.exists()) {
            filePatients.mkdirs();
        }

        filePatients = new File("./backup/patients/data.iot");
        if (filePatients.exists()) {
            filePatients.createNewFile();
        }

        os = new FileOutputStream("./backup/patients/data.iot");
        osw = new OutputStreamWriter(os);
        bw = new BufferedWriter(osw);

        Iterator<Paciente> it = listaPacientes.iterator();
        Iterator<String> itData;
        Paciente patient;
        String data;
        bw.write(listaPacientes.size());
        bw.newLine();
        while (it.hasNext()) {
            patient = it.next();
            bw.write(patient.getCPF());
            bw.newLine();
            bw.write(patient.getNome());
            bw.newLine();
            bw.write(patient.getNumero());
            bw.newLine();
            bw.write(patient.getSenha());
            bw.newLine();
            bw.write(patient.getX());
            bw.newLine();
            bw.write(patient.getY());
            bw.newLine();
            bw.write(patient.getListBtCardiacos().size());
            bw.newLine();
            itData = patient.getListBtCardiacos().iterator();
            while (itData.hasNext()) {
                data = itData.next();
                bw.write(data);
                bw.newLine();
            }
            bw.write(TOKENSEPARATOR);
            bw.newLine();
            itData = patient.getListPressaoSanguinea().iterator();
            while (itData.hasNext()) {
                data = itData.next();
                bw.write(data);
                bw.newLine();
            }
            bw.write(TOKENSEPARATOR);
            bw.newLine();
            itData = patient.getListRepouso().iterator();
            while (itData.hasNext()) {
                data = itData.next();
                bw.write(data);
                bw.newLine();
            }
            bw.write(TOKENSEPARATOR);
            bw.newLine();
            itData = patient.getListAcEspecial().iterator();
            while (itData.hasNext()) {
                data = itData.next();
                bw.write(data);
                bw.newLine();
            }
        }
        bw.close();
        osw.close();
        os.close();
    }

    public void readData() throws IOException {
        readDataPatient();
    }

    private void readDataPatient() throws IOException {

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        int sizeList = 0;

        File filePatients = new File("./backup/patients/data.iot");
        if (!filePatients.exists()) {
            return;
        }
        fileReader = new FileReader(filePatients);
        bufferedReader = new BufferedReader(fileReader);

        if (!bufferedReader.ready()) {
            sizeList = Integer.parseInt(bufferedReader.readLine());
            filePatients.delete();
            return;
        }
        while (bufferedReader.ready()) {
            Paciente patient;
            String cpf, nome, numero, senha, posX, posY, data;

            cpf = bufferedReader.readLine();
            nome = bufferedReader.readLine();
            numero = bufferedReader.readLine();
            senha = bufferedReader.readLine();
            posX = bufferedReader.readLine();
            posY = bufferedReader.readLine();
            patient = new Paciente(cpf, nome, numero, senha);
            patient.setX(Integer.parseInt(posX));
            patient.setY(Integer.parseInt(posY));
            data = bufferedReader.readLine();
            while (!data.equals(TOKENSEPARATOR)) {
                data = bufferedReader.readLine();
                patient.getListBtCardiacos().add(data);
            }
            data = bufferedReader.readLine();
            while (!data.equals(TOKENSEPARATOR)) {
                data = bufferedReader.readLine();
                patient.getListPressaoSanguinea().add(data);
            }
            data = bufferedReader.readLine();
            while (!data.equals(TOKENSEPARATOR)) {
                data = bufferedReader.readLine();
                patient.getListRepouso().add(data);
            }
            data = bufferedReader.readLine();
            while (!data.equals(TOKENSEPARATOR)) {
                data = bufferedReader.readLine();
                patient.getListAcEspecial().add(data);
            }
            System.out.println(patient.getNome());
            listaPacientes.add(patient);
        }
        bufferedReader.close();
        fileReader.close();
    }
}
