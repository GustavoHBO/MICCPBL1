/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.view.controller;

import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import miccpbl1.client.device.controller.Controller;
import miccpbl1.model.Pessoa;

/**
 * FXML Controller class
 *
 * @author gustavo
 */
public class LoginController implements Initializable {

    /* Statement the Controller */
    private Controller controller = null;

    /* Statement the Pane */
    @FXML
    private Pane paneRegister = null;
    @FXML
    private Pane paneConfig = null;

    /* Statement the TextField */
    @FXML
    private TextField textFieldCpf = null;
    @FXML
    private TextField textFieldPassword = null;
    @FXML
    private TextField textFieldName = null;
    @FXML
    private TextField textFieldCpfRegister = null;
    @FXML
    private TextField textFieldNumber = null;
    @FXML
    private TextField textFieldPass = null;
    @FXML
    private TextField textFieldPassAgain = null;
    @FXML
    private TextField textFieldPortServer = null;
    @FXML
    private TextField textFieldIpServer = null;

    /* Statement the Button */
    @FXML
    private Button buttonConnect = null;
    @FXML
    private Button buttonRegister = null;
    @FXML
    private Button buttonRegisterAccount = null;
    @FXML
    private Button buttonCancel = null;
    @FXML
    private Button buttonConfig = null;
    @FXML
    private Button buttonConnectServer = null;

    /* Statement the Label */
    @FXML
    private Label labelErrorRegister = null;
    @FXML
    private Label labelError = null;
    @FXML
    private Label labelStatusConnection = null;
    
    /* Statement class Pessoa */
    private Pessoa pessoa = null;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = Controller.getController();
    }

    /**
     * Event for the button register on pane Login.
     */
    public void eventButtonRegisterLogin() {
        paneConfig.setVisible(false);
        paneRegister.setVisible(true);
    }

    /**
     * Event for the button cancel on pane Login. Close the pane register and
     * back to pane login.
     */
    public void eventButtonCancel() {
        paneRegister.setVisible(false);
    }

    /**
     * Event for the button register on pane register. Verify the fields and
     * show the possible error. Case the fields are correct, the register is
     * done.
     */
    public void eventButtonRegister() {

        if (textFieldName.getText().trim().isEmpty()) {
            labelErrorRegister.setText("ERROR: Digite o campo nome corretamente!");
            labelErrorRegister.setVisible(true);
        } else if (textFieldCpfRegister.getText().trim().isEmpty()) {
            labelErrorRegister.setText("ERROR: Digite o campo CPF corretamente!");
            labelErrorRegister.setVisible(true);
        } else if (textFieldNumber.getText().trim().isEmpty()) {
            labelErrorRegister.setText("ERROR: Digite o campo número corretamente!");
            labelErrorRegister.setVisible(true);
        } else if (textFieldPass.getText().trim().isEmpty()) {
            labelErrorRegister.setText("ERROR: Digite o campo senha corretamente!");
            labelErrorRegister.setVisible(true);
        } else if (!textFieldPass.getText().equals(textFieldPassAgain.getText())) {
            labelErrorRegister.setText("ERROR: As senhas não conferem!");
            labelErrorRegister.setVisible(true);
        } else {
            try {
                labelErrorRegister.setVisible(false);
                controller.cadastrarPaciente(textFieldName.getText(), textFieldCpf.getText(), textFieldNumber.getText(), textFieldPass.getText());
            } catch (SocketException ex) {
                labelErrorRegister.setText("ERROR: Não foi possivel estabelecer a conexão ao servidor!");
                labelErrorRegister.setVisible(true);
            }
        }
    }

    /**
     * Event for the setting button. Show the pane of config connection.
     */
    public void eventButtonConfig() {
        if (paneConfig.isVisible()) {
            paneConfig.setVisible(false);
        } else {
            paneConfig.setVisible(true);
        }
    }

    /**
     * Event for test the connection with server.
     */
    public void eventButtonConnectPaneConfig() {
        boolean connect = controller.testConnection();
        if (connect) {
            labelStatusConnection.setText("Conectado");
            labelStatusConnection.setDisable(false);
        }else{
            labelStatusConnection.setText("Desconectado");
            labelStatusConnection.setDisable(true);
        }
    }
    
    /**
     * Event for obtains
     */
    public void eventButtonConnect(){
        String cpf, senha;
        cpf = textFieldCpf.getText();
        senha = textFieldPassword.getText();
        if(cpf.trim().isEmpty() || senha.trim().isEmpty()){
            return;
        }
        
        Runnable run = new Runnable() {
            @Override
            public void run() {
                String reply;
                controller.connectAccount(cpf, senha);
                reply = controller.replyServer();
                System.out.println(reply);
                if (reply.trim().isEmpty()) {
                    labelError.setText("ERROR: Usuário ou Senha inválido!");
                    labelError.setVisible(true);
                } else {
                    String[] person = reply.split(controller.getCHARSPLIT());
                    if (person.length < 3) {// If the reply is wrong.
                        return;
                    }
                    pessoa = new Pessoa();
                    pessoa.setCPF(person[0]);
                    pessoa.setNome(person[1]);
                    pessoa.setNumero(person[2]);
                }
            }
        };
        Thread thread = new Thread(run);
        thread.start();
    }
}
