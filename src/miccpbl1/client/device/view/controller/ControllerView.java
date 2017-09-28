/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.view.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import miccpbl1.client.device.controller.Controller;
import miccpbl1.client.device.view.exceptions.ErrorUnknown;
import miccpbl1.client.device.view.exceptions.InvalidHeartBeatsException;
import miccpbl1.client.device.view.exceptions.NullHeartBeatsException;
import miccpbl1.client.device.view.exceptions.NullStatusMovementException;

/**
 * FXML Controller class
 *
 * @author gustavo
 */
public class ControllerView implements Initializable {

    /* Declaração dos botões da interface */
    @FXML
    private Button btnRangeRefresh = null;
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

    /* Declaração do controller */
    private Controller controller = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = Controller.getController();
    }

    @FXML
    private void sendData() {

        try {
            String pressure = labelPressure.getText();
            String btmBeats = labelBeats.getText();
            String movement = labelMovement.getText();
            String patientRisk = checkBoxPatientRisk.selectedProperty().toString();
            controller.updateStatusPatient(btmBeats, movement, pressure, patientRisk);
        } catch (NullHeartBeatsException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidHeartBeatsException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullStatusMovementException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void eventUpdateRangeRefresh() {

        String rangeRefresh = textFieldRangeRefresh.getText();
        controller.updateRangeRefresh(rangeRefresh);
    }

    @FXML
    private void eventIncreasePressure() {

        if (labelPressure == null) {
            return;
        } else if ("Sem Paciente".equals(labelPressure.getText())) {
            return;
        }
        int pressure = Integer.parseInt(labelPressure.getText());
        pressure++;
        labelPressure.setText(Integer.toString(pressure));
    }

    @FXML
    private void eventSetPressure() {
        if (textFieldPressure == null) {
            return;
        } else if (textFieldPressure.getText().trim().isEmpty()) {
            return;
        } else {
            try {
                int pressure = Integer.parseInt(textFieldPressure.getText());
                if (pressure < 0) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
            labelPressure.setText(textFieldPressure.getText());
        }
    }

    @FXML
    private void eventDecreasePressure() {

        if (labelPressure == null) {
            return;
        } else if ("Sem Paciente".equals(labelPressure.getText())) {
            return;
        }
        int pressure = Integer.parseInt(labelPressure.getText());
        if (pressure > 0) {
            pressure--;
        }
        labelPressure.setText(Integer.toString(pressure));
    }

    @FXML
    private void eventIncreaseBeats() {

        if (labelBeats == null) {
            return;
        } else if ("Sem Paciente".equals(labelBeats.getText())) {
            return;
        }
        int beats = Integer.parseInt(labelBeats.getText());
        beats++;
        labelBeats.setText(Integer.toString(beats));
    }

    @FXML
    private void eventSetBeats() {
        if (textFieldBeats == null) {
            return;
        } else if (textFieldBeats.getText().trim().isEmpty()) {
            return;
        } else {
            try {
                int beats = Integer.parseInt(textFieldBeats.getText());
                if (beats < 0) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
            labelBeats.setText(textFieldBeats.getText());
        }
    }

    @FXML
    private void eventDecreaseBeats() {

        if (labelBeats == null) {
            return;
        } else if ("Sem Paciente".equals(labelBeats.getText())) {
            return;
        }
        int beats = Integer.parseInt(labelBeats.getText());
        if (beats > 0) {
            beats--;
        }
        labelBeats.setText(Integer.toString(beats));
    }
    
    @FXML
    private void eventSwitchStatusMovement() {
        
        if (labelMovement == null) {
            return;
        } else if ("Sem Paciente".equals(labelMovement.getText())) {
            labelMovement.setText("Em Movimento");
            return;
        } else {
            if (labelMovement.getText().equals("Em Movimento")) {
                labelMovement.setText("Em Repouso");
            }
            else{
                labelMovement.setText("Em Movimento");
            }
        }
    }
}
