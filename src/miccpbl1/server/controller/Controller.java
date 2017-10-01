/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.server.controller;

import java.util.ArrayList;
import java.util.Iterator;
import miccpbl1.client.device.view.exceptions.PacientRegisteredException;
import miccpbl1.model.Paciente;

/**
 *
 * @author gustavo
 */
public class Controller {

    private final String TOKENSEPARATOR = "!=";
    private static Controller controller = null;
    private ArrayList<Paciente> listaPacientes = null;

    public static Controller getController() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public static void resetController() {
        controller = null;
    }

    /**
     * Cadastra o paciente no servidor.
     *
     * @param data
     * @throws PacientRegisteredException
     */
    public void registerPacient(String data) throws PacientRegisteredException {

        String nome;
        String cpf;
        String numero;

        Paciente paciente;
        String[] splitData = data.split(TOKENSEPARATOR);

        nome = splitData[0];
        cpf = splitData[1];
        numero = splitData[2];
        paciente = new Paciente(nome, cpf, numero);

        if (listaPacientes == null) {
            listaPacientes = new ArrayList<>();
        }
        if (findPacient(cpf) == null) {
            throw new PacientRegisteredException();
        } else {
            listaPacientes.add(paciente);
        }

    }
    
    public void refreshStatusPacient(String data){
        
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
}
