/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.server;

import java.util.ArrayList;
import miccpbl1.server.model.Paciente;

/**
 *
 * @author gustavo
 */
public class Server {
    
    private ArrayList<Paciente> listaPacientes = null;
    
    /**
     * Cadastra o paciente no servidor.
     * @param paciente 
     */
    public void cadastrarPaciente(Paciente paciente) {
        if(listaPacientes == null) {
            listaPacientes = new ArrayList<Paciente>();
        }
        if(listaPacientes.contains(paciente)){
            return;
        }
        else{
            listaPacientes.add(paciente);
        }
    }

    /**
     * @return the listaPacientes
     */
    public ArrayList getListaPacientes() {
        return listaPacientes;
    }

    /**
     * @param listaPacientes the listaPacientes to set
     */
    public void setListaPacientes(ArrayList listaPacientes) {
        this.listaPacientes = listaPacientes;
    }
    
    
}
