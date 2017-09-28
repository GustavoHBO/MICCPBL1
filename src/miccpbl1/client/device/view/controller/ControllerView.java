/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.view.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author gustavo
 */
public class ControllerView implements Initializable {

    /* Declaração dos botões da interface */
    
    @FXML
    private Button btnConect = null;
    @FXML
    private Button btnIncreasePressure = null;
    @FXML
    private Button btnDecreasePressure = null;
    @FXML
    private Button btnIncreaseBeats = null;
    @FXML
    private Button btnDecreaseBeats = null;
    @FXML
    private Button btnSwitch = null;
    @FXML
    private Button btnSend = null;
    
    /* Declaração dos CheckBox */
    
    @FXML
    private CheckBox checkBoxPatientRisk = null;
    
    /* Declaração dos TextField's */
    
    @FXML 
    private TextField textFieldIpServer = null;
    @FXML
    private TextField textFieldPortServer = null;
    @FXML
    private TextField textFieldRangeRefresh = null;
    @FXML
    private TextField textFieldPressure = null;
    @FXML
    private TextField textFieldBeats = null;
    
    /* Declaração dos Label */

    @FXML
    private Label labelStatusConection = null;
    @FXML
    private Label labelPressure = null;
    @FXML
    private Label labelBeats = null;
    @FXML
    private Label labelMovement = null;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
