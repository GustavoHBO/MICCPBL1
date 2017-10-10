/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe Paciente e seus atributos
 *
 * @author Gustavo Henrique
 * @since 19/09/2017
 *
 */
public class Paciente extends Pessoa implements Serializable{

    private ArrayList<String> listBtCardiacos; // Batimentos cardiacos da pessoa por minuto.
    private ArrayList<String> listRepouso; // Identifica o estado de atividade do paciente.
    private ArrayList<String> listPressaoSanguinea; // Pressão sanguínea do paciente.
    private ArrayList<String> listAcEspecial; // Determina se o paciente necessita de um acompanhamento especial

    public Paciente( String cpf, String nome, String numeroCelular) {
        this.nome = nome;
        this.CPF= cpf;
        this.numero = numeroCelular;
        
        listBtCardiacos = new ArrayList<String>();
        listRepouso = new ArrayList<String>();
        listPressaoSanguinea = new ArrayList<String>();
        listAcEspecial = new ArrayList<String>();
    }

    /**
     * @return the listBtCardiacos
     */
    public ArrayList<String> getListBtCardiacos() {
        return listBtCardiacos;
    }

    /**
     * @param listBtCardiacos the listBtCardiacos to set
     */
    public void setListBtCardiacos(ArrayList<String> listBtCardiacos) {
        this.listBtCardiacos = listBtCardiacos;
    }

    /**
     * @return the listRepouso
     */
    public ArrayList<String> getListRepouso() {
        return listRepouso;
    }

    /**
     * @param listRepouso the listRepouso to set
     */
    public void setListRepouso(ArrayList<String> listRepouso) {
        this.listRepouso = listRepouso;
    }

    /**
     * @return the listPressaoSanguinea
     */
    public ArrayList<String> getListPressaoSanguinea() {
        return listPressaoSanguinea;
    }

    /**
     * @param listPressaoSanguinea the listPressaoSanguinea to set
     */
    public void setListPressaoSanguinea(ArrayList<String> listPressaoSanguinea) {
        this.listPressaoSanguinea = listPressaoSanguinea;
    }

    /**
     * @return the listAcEspecial
     */
    public ArrayList<String> getListAcEspecial() {
        return listAcEspecial;
    }

    /**
     * @param listAcEspecial the listAcEspecial to set
     */
    public void setListAcEspecial(ArrayList<String> listAcEspecial) {
        this.listAcEspecial = listAcEspecial;
    }
}
