/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.server.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import miccpbl1.model.Medico;
import miccpbl1.model.Paciente;

/**
 * Classe de controle das conexoes do servidor
 *
 * @author gustavo
 */
public class Controller implements Serializable {

    private final String TOKENSEPARATOR = "!=";
    private final int LENGTHSERVERPROTOCOL = 4;
    private final int LENGTHPROTOCOL = 2;

    private int posX;
    private int posY;

    private static Controller controller = null;
    private final ArrayList<Paciente> listaPacientes = new ArrayList<>();
    private final ArrayList<Medico> listDoctor = new ArrayList<>();

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

    /**
     * Cadastra o paciente no servidor.
     *
     * @param data
     */
    public void registerPacient(String data) {
        String nome;
        String cpf;
        String numero;
        String senha;

        Paciente paciente;
        String[] splitData = data.split(getTOKENSEPARATOR());

        cpf = splitData[0];
        nome = splitData[1];
        numero = splitData[2];
        senha = splitData[3];
        paciente = new Paciente(cpf, nome, numero, senha);

        if (findPacient(cpf) == null) {
            System.out.println("Cadastrando Paciente");
            listaPacientes.add(paciente);
        } else {
            System.out.println("Não Cadastrando Paciente");
            return;
        }

    }

    public void refreshStatusPacient(String data) {
        String[] splitedData = splitString(data, getTOKENSEPARATOR());
        Paciente patient = findPacient(splitedData[0]);
        if (patient == null) {
            return;// Aqui eu poderia lançar uma exceção.
        } else {
            System.out.println("Atualizando dados do paciente");
            patient.getListBtCardiacos().add(splitedData[1]);
            patient.getListRepouso().add(splitedData[2]);
            patient.getListPressaoSanguinea().add(splitedData[3]);
            String[] str = splitedData[4].split("/");
            if (Integer.parseInt(splitedData[1]) < 120 && Integer.parseInt(splitedData[1]) > 40) {
                patient.getListAcEspecial().add("Not Risk");
            } else if (Integer.parseInt(splitedData[1]) > 120) {
                if (splitedData[2].equals("Em Repouso")) {
                    patient.getListAcEspecial().add("Not Risk");
                }
            } else {
                if (splitedData[2].equals("Em Repouso") && (Integer.parseInt(str[0]) < 120 && Integer.parseInt(str[0]) > 40)) {
                    patient.getListAcEspecial().add("Not Risk");
                } else {
                    patient.getListAcEspecial().add("Risk");
                }
            }

        }
    }

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

    private String[] splitString(String string, String splitString) {
        return string.split(splitString);
    }

    private String[] getIdPatients() {
        String[] ids = new String[listaPacientes.size()];
        Iterator<Paciente> it = listaPacientes.iterator();
        int i = 0;
        while (it.hasNext()) {
            ids[i++] = it.next().getCPF();
        }
        return ids;
    }

    private String getDataPatients(String[] patients) {
        String data = new String();
        int i = 0;
        Iterator<Paciente> it = listaPacientes.iterator();
        Paciente patient;
        while (it.hasNext()) {
            patient = it.next();
            if (patients[i].equals(patient.getCPF())) {
                data += patient.getCPF();
                data += getTOKENSEPARATOR();
                data += patient.getListBtCardiacos().get(0);
                data += getTOKENSEPARATOR();
                data += patient.getListPressaoSanguinea().get(0);
                data += getTOKENSEPARATOR();
                data += patient.getListRepouso().get(0);
                data += getTOKENSEPARATOR();
                data += patient.getListAcEspecial().get(0);
                if (it.hasNext()) {
                    data += getTOKENSEPARATOR();
                }
            }
        }
        return data;
    }

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

    public String mountListPatient() {
        String listPat;
        listPat = "0x08";
        Paciente pat;
        Iterator<Paciente> it = listaPacientes.iterator();
        while (it.hasNext()) {
            pat = it.next();
            listPat += pat.getCPF();
            listPat += getTOKENSEPARATOR();
            listPat += pat.getNome();
            listPat += getTOKENSEPARATOR();
            listPat += pat.getNumero();
            if (it.hasNext()) {
                listPat += getTOKENSEPARATOR();
            }
        }
        listPat += "0x08";
        return listPat;
    }

    /**
     * @return the posX
     */
    public int getPosX() {
        return posX;
    }

    /**
     * @param posX the posX to set
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * @return the posY
     */
    public int getPosY() {
        return posY;
    }

    /**
     * @param posY the posY to set
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * @return the TOKENSEPARATOR
     */
    public String getTOKENSEPARATOR() {
        return TOKENSEPARATOR;
    }
}
