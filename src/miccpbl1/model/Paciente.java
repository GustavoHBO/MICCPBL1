/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.model;

/**
 * Classe Paciente e seus atributos
 *
 * @author Gustavo Henrique
 * @since 19/09/2017
 *
 */
public class Paciente extends Pessoa {

    private String btCardiacos; // Batimentos cardiacos da pessoa por minuto.
    private boolean repouso = true; // Identifica o estado de atividade do paciente.
    private String pressaoSanguinea = null; // Pressão sanguínea do paciente.
    private boolean acEspecial = false; // Determina se o paciente necessita de um acompanhamento especial

    public Paciente( String nome, String cpf, String numeroCelular) {
        this.nome = nome;
        this.CPF= cpf;
        this.numero = numeroCelular;
    }

    /**
     * Retorna true para paciente que necessita de acompanhamento especial,
     * false para caso não precise.
     *
     * @return the acEspecial
     */
    public boolean isAcEspecial() {
        return acEspecial;
    }

    /**
     * Altera a condição de acompanhamento do paciente, true para acompanhamento
     * especial, false para acompanhamento normal.
     *
     * @param acEspecial the acEspecial to set
     */
    public void setAcEspecial(boolean acEspecial) {
        this.acEspecial = acEspecial;
    }

    /**
     * Retorna a quantidade de batimentos cardíacos do paciente.
     *
     * @return the btCardiacos
     */
    public String getBtCardiacos() {
        return btCardiacos;
    }

    /**
     * Altera a quantidade de batimentos cardíacos do paciente.
     *
     * @param btCardiacos the btCardiacos to set
     */
    public void setBtCardiacos(String btCardiacos) {
        this.btCardiacos = btCardiacos;
    }

    /**
     * Retorna o estado do paciente
     *
     * @return the repouso, true para repouso, false para exercendo alguma
     * atividade
     */
    public boolean isRepouso() {
        return repouso;
    }

    /**
     * Altera o estado de repouso do paciente
     *
     * @param repouso the repouso to set
     */
    public void setRepouso(boolean repouso) {
        this.repouso = repouso;
    }

    /**
     * Retorna a pressão sanguínea do paciente
     *
     * @return the pressaoSanguinea
     */
    public String getPressaoSanguinea() {
        return pressaoSanguinea;
    }

    /**
     * Altera a pressão sanguínea do paciente.
     *
     * @param pressaoSanguinea the pressaoSanguinea to set
     */
    public void setPressaoSanguinea(String pressaoSanguinea) {
        this.pressaoSanguinea = pressaoSanguinea;
    }
}
