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
    @FXML
    private CheckBox checkBoxRandomPressure = null;
    @FXML
    private CheckBox checkBoxRandomBeats = null;
    @FXML
    private CheckBox checkBoxRandomMovement = null;

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
        setController(Controller.getController());
    }

    @FXML
    private void sendData() {

        try {
            String pressure = getLabelPressure().getText();
            String btmBeats = getLabelBeats().getText();
            String movement = getLabelMovement().getText();
            String patientRisk = getCheckBoxPatientRisk().selectedProperty().toString();
            getController().updateStatusPatient(btmBeats, movement, pressure, patientRisk);
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

        String rangeRefresh = getTextFieldRangeRefresh().getText();
        getController().updateRangeRefresh(rangeRefresh);
    }

    @FXML
    private void eventIncreasePressure() {

        if (getLabelPressure() == null) {
            return;
        } else if ("Sem Paciente".equals(getLabelPressure().getText())) {
            return;
        }
        int pressure = Integer.parseInt(getLabelPressure().getText());
        pressure++;
        getLabelPressure().setText(Integer.toString(pressure));
    }

    @FXML
    private void eventSetPressure() {
        if (getTextFieldPressure() == null) {
            return;
        } else if (getTextFieldPressure().getText().trim().isEmpty()) {
            return;
        } else {
            try {
                int pressure = Integer.parseInt(getTextFieldPressure().getText());
                if (pressure < 0) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
            getLabelPressure().setText(getTextFieldPressure().getText());
        }
    }

    @FXML
    private void eventDecreasePressure() {

        if (getLabelPressure() == null) {
            return;
        } else if ("Sem Paciente".equals(getLabelPressure().getText())) {
            return;
        }
        int pressure = Integer.parseInt(getLabelPressure().getText());
        if (pressure > 0) {
            pressure--;
        }
        getLabelPressure().setText(Integer.toString(pressure));
    }

    @FXML
    private void eventIncreaseBeats() {

        if (getLabelBeats() == null) {
            return;
        } else if ("Sem Paciente".equals(getLabelBeats().getText())) {
            return;
        }
        int beats = Integer.parseInt(getLabelBeats().getText());
        beats++;
        getLabelBeats().setText(Integer.toString(beats));
    }

    @FXML
    private void eventSetBeats() {
        if (getTextFieldBeats() == null) {
            return;
        } else if (getTextFieldBeats().getText().trim().isEmpty()) {
            return;
        } else {
            try {
                int beats = Integer.parseInt(getTextFieldBeats().getText());
                if (beats < 0) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
            getLabelBeats().setText(getTextFieldBeats().getText());
        }
    }

    @FXML
    private void eventDecreaseBeats() {

        if (getLabelBeats() == null) {
            return;
        } else if ("Sem Paciente".equals(getLabelBeats().getText())) {
            return;
        }
        int beats = Integer.parseInt(getLabelBeats().getText());
        if (beats > 0) {
            beats--;
        }
        getLabelBeats().setText(Integer.toString(beats));
    }
    
    @FXML
    private void eventSwitchStatusMovement() {
        
        if (getLabelMovement() == null) {
            return;
        } else if ("Sem Paciente".equals(getLabelMovement().getText())) {
            getLabelMovement().setText("Em Movimento");
            return;
        } else {
            if (getLabelMovement().getText().equals("Em Movimento")) {
                getLabelMovement().setText("Em Repouso");
            }
            else{
                getLabelMovement().setText("Em Movimento");
            }
        }
    }

    /**
     * @return the btnRangeRefresh
     */
    public Button getBtnRangeRefresh() {
        return btnRangeRefresh;
    }

    /**
     * @param btnRangeRefresh the btnRangeRefresh to set
     */
    public void setBtnRangeRefresh(Button btnRangeRefresh) {
        this.btnRangeRefresh = btnRangeRefresh;
    }

    /**
     * @return the btnConect
     */
    public Button getBtnConect() {
        return btnConect;
    }

    /**
     * @param btnConect the btnConect to set
     */
    public void setBtnConect(Button btnConect) {
        this.btnConect = btnConect;
    }

    /**
     * @return the btnIncreasePressure
     */
    public Button getBtnIncreasePressure() {
        return btnIncreasePressure;
    }

    /**
     * @param btnIncreasePressure the btnIncreasePressure to set
     */
    public void setBtnIncreasePressure(Button btnIncreasePressure) {
        this.btnIncreasePressure = btnIncreasePressure;
    }

    /**
     * @return the btnDecreasePressure
     */
    public Button getBtnDecreasePressure() {
        return btnDecreasePressure;
    }

    /**
     * @param btnDecreasePressure the btnDecreasePressure to set
     */
    public void setBtnDecreasePressure(Button btnDecreasePressure) {
        this.btnDecreasePressure = btnDecreasePressure;
    }

    /**
     * @return the btnIncreaseBeats
     */
    public Button getBtnIncreaseBeats() {
        return btnIncreaseBeats;
    }

    /**
     * @param btnIncreaseBeats the btnIncreaseBeats to set
     */
    public void setBtnIncreaseBeats(Button btnIncreaseBeats) {
        this.btnIncreaseBeats = btnIncreaseBeats;
    }

    /**
     * @return the btnDecreaseBeats
     */
    public Button getBtnDecreaseBeats() {
        return btnDecreaseBeats;
    }

    /**
     * @param btnDecreaseBeats the btnDecreaseBeats to set
     */
    public void setBtnDecreaseBeats(Button btnDecreaseBeats) {
        this.btnDecreaseBeats = btnDecreaseBeats;
    }

    /**
     * @return the btnSwitch
     */
    public Button getBtnSwitch() {
        return btnSwitch;
    }

    /**
     * @param btnSwitch the btnSwitch to set
     */
    public void setBtnSwitch(Button btnSwitch) {
        this.btnSwitch = btnSwitch;
    }

    /**
     * @return the btnSend
     */
    public Button getBtnSend() {
        return btnSend;
    }

    /**
     * @param btnSend the btnSend to set
     */
    public void setBtnSend(Button btnSend) {
        this.btnSend = btnSend;
    }

    /**
     * @return the checkBoxPatientRisk
     */
    public CheckBox getCheckBoxPatientRisk() {
        return checkBoxPatientRisk;
    }

    /**
     * @param checkBoxPatientRisk the checkBoxPatientRisk to set
     */
    public void setCheckBoxPatientRisk(CheckBox checkBoxPatientRisk) {
        this.checkBoxPatientRisk = checkBoxPatientRisk;
    }

    /**
     * @return the checkBoxRandomPressure
     */
    public CheckBox getCheckBoxRandomPressure() {
        return checkBoxRandomPressure;
    }

    /**
     * @param checkBoxRandomPressure the checkBoxRandomPressure to set
     */
    public void setCheckBoxRandomPressure(CheckBox checkBoxRandomPressure) {
        this.checkBoxRandomPressure = checkBoxRandomPressure;
    }

    /**
     * @return the checkBoxRandomBeats
     */
    public CheckBox getCheckBoxRandomBeats() {
        return checkBoxRandomBeats;
    }

    /**
     * @param checkBoxRandomBeats the checkBoxRandomBeats to set
     */
    public void setCheckBoxRandomBeats(CheckBox checkBoxRandomBeats) {
        this.checkBoxRandomBeats = checkBoxRandomBeats;
    }

    /**
     * @return the checkBoxRandomMovement
     */
    public CheckBox getCheckBoxRandomMovement() {
        return checkBoxRandomMovement;
    }

    /**
     * @param checkBoxRandomMovement the checkBoxRandomMovement to set
     */
    public void setCheckBoxRandomMovement(CheckBox checkBoxRandomMovement) {
        this.checkBoxRandomMovement = checkBoxRandomMovement;
    }

    /**
     * @return the textFieldIpServer
     */
    public TextField getTextFieldIpServer() {
        return textFieldIpServer;
    }

    /**
     * @param textFieldIpServer the textFieldIpServer to set
     */
    public void setTextFieldIpServer(TextField textFieldIpServer) {
        this.textFieldIpServer = textFieldIpServer;
    }

    /**
     * @return the textFieldPortServer
     */
    public TextField getTextFieldPortServer() {
        return textFieldPortServer;
    }

    /**
     * @param textFieldPortServer the textFieldPortServer to set
     */
    public void setTextFieldPortServer(TextField textFieldPortServer) {
        this.textFieldPortServer = textFieldPortServer;
    }

    /**
     * @return the textFieldRangeRefresh
     */
    public TextField getTextFieldRangeRefresh() {
        return textFieldRangeRefresh;
    }

    /**
     * @param textFieldRangeRefresh the textFieldRangeRefresh to set
     */
    public void setTextFieldRangeRefresh(TextField textFieldRangeRefresh) {
        this.textFieldRangeRefresh = textFieldRangeRefresh;
    }

    /**
     * @return the textFieldPressure
     */
    public TextField getTextFieldPressure() {
        return textFieldPressure;
    }

    /**
     * @param textFieldPressure the textFieldPressure to set
     */
    public void setTextFieldPressure(TextField textFieldPressure) {
        this.textFieldPressure = textFieldPressure;
    }

    /**
     * @return the textFieldBeats
     */
    public TextField getTextFieldBeats() {
        return textFieldBeats;
    }

    /**
     * @param textFieldBeats the textFieldBeats to set
     */
    public void setTextFieldBeats(TextField textFieldBeats) {
        this.textFieldBeats = textFieldBeats;
    }

    /**
     * @return the labelStatusConection
     */
    public Label getLabelStatusConection() {
        return labelStatusConection;
    }

    /**
     * @param labelStatusConection the labelStatusConection to set
     */
    public void setLabelStatusConection(Label labelStatusConection) {
        this.labelStatusConection = labelStatusConection;
    }

    /**
     * @return the labelPressure
     */
    public Label getLabelPressure() {
        return labelPressure;
    }

    /**
     * @param labelPressure the labelPressure to set
     */
    public void setLabelPressure(Label labelPressure) {
        this.labelPressure = labelPressure;
    }

    /**
     * @return the labelBeats
     */
    public Label getLabelBeats() {
        return labelBeats;
    }

    /**
     * @param labelBeats the labelBeats to set
     */
    public void setLabelBeats(Label labelBeats) {
        this.labelBeats = labelBeats;
    }

    /**
     * @return the labelMovement
     */
    public Label getLabelMovement() {
        return labelMovement;
    }

    /**
     * @param labelMovement the labelMovement to set
     */
    public void setLabelMovement(Label labelMovement) {
        this.labelMovement = labelMovement;
    }

    /**
     * @return the controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
