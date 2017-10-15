/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.model.doctor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author gustavo
 */
public class Pessoa {

    StringProperty  nome = null;
    StringProperty  CPF = null;
    StringProperty  numero = null;

    public Pessoa(){
        
    }
    
    public Pessoa(String  nome, String  CPF, String  numero) {
        this.nome = new SimpleStringProperty(nome);
        this.CPF = new SimpleStringProperty(CPF);
        this.numero = new SimpleStringProperty(numero);
    }
    
    public StringProperty  getNome() {
        return nome;
    }

    public void setNome(StringProperty  nome) {
        this.nome = nome;
    }

    public StringProperty  getCPF() {
        return CPF;
    }

    public void setCPF(StringProperty  CPF) {
        this.CPF = CPF;
    }

    public StringProperty  getNumero() {
        return numero;
    }

    public void setNumero(StringProperty  numero) {
        this.numero = numero;
    }
}
