/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.model.doctor;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author gustavo
 */
public class Medico extends Pessoa {

    private String crm;
    private ArrayList<String> listPatient;

    public Medico(String nome, String cpf, String numero, String crm) {
        this.nome = new SimpleStringProperty(nome);
        this.CPF = new SimpleStringProperty(cpf);
        this.numero = new SimpleStringProperty(numero);
        this.crm = crm;
    }

    /**
     * @return the crm
     */
    public String getCrm() {
        return crm;
    }

    /**
     * @param crm the crm to set
     */
    public void setCrm(String crm) {
        this.crm = crm;
    }

    /**
     * @return the listPatient
     */
    public ArrayList<String> getListPatient() {
        return listPatient;
    }

    /**
     * @param listPatient the listPatient to set
     */
    public void setListPatient(ArrayList<String> listPatient) {
        this.listPatient = listPatient;
    }
}
