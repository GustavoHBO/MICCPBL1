/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.view.controller;

import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import miccpbl1.client.device.controller.Controller;
import miccpbl1.client.exceptions.DataInvalidException;

/**
 * FXML Controller class
 *
 * @author gustavo
 */
public class LoginController implements Initializable {

    private Controller controller = null;

    @FXML
    private Pane paneRegister = null;

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
    private Button buttonConnect = null;
    @FXML
    private Button buttonRegister = null;
    @FXML
    private Button buttonRegisterAccount = null;
    @FXML
    private Button buttonCancel = null;

    @FXML
    private Label labelErrorRegister = null;
    @FXML
    private Label labelError = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void eventButtonRegisterLogin() {
        paneRegister.setVisible(true);
    }

    public void eventButtonCancel() {
        paneRegister.setVisible(false);
    }

    public void eventButtonRegister() {
        if (textFieldName.getText().trim().isEmpty()) {
            labelErrorRegister.setText("ERROR: Digite o campo nome corretamente!");
            labelErrorRegister.setVisible(true);
        } else if (textFieldCpf.getText().trim().isEmpty()) {
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
}
