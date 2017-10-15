/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.view.controller;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import miccpbl1.client.device.controller.Controller;
import miccpbl1.client.exceptions.DataInvalidException;
import miccpbl1.client.exceptions.InvalidHeartBeatsException;
import miccpbl1.client.exceptions.IpServerInvalidException;
import miccpbl1.client.exceptions.NullHeartBeatsException;
import miccpbl1.client.exceptions.NullStatusMovementException;
import miccpbl1.client.exceptions.PortServerInvalidException;

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
    @FXML
    private Button btnRandomPressure = null;
    @FXML
    private Button btnRandomBeats = null;
    @FXML
    private Button btnRandomMovement = null;


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
    private TextField textFieldPressureDia = null;
    @FXML
    private TextField textFieldBeats = null;
    @FXML
    private TextField textFieldNome = null;
    @FXML
    private TextField textFieldCpf = null;
    @FXML
    private TextField textFieldNumero = null;

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
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.controller = Controller.getController();
    }

    @FXML
    private void eventUpdateRangeRefresh() {

        try {
            String rangeRefresh = getTextFieldRangeRefresh().getText();
            getController().updateRangeRefresh(rangeRefresh);
        } catch (SocketException | DataInvalidException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void eventIncreasePressure() {

        if (getLabelPressure() == null) {
            return;
        } else if ("Sem Paciente".equals(getLabelPressure().getText())) {
            return;
        }
        String pressureString = getLabelPressure().getText();
        int pressure = Integer.parseInt(pressureString.substring(0, pressureString.indexOf("/")));
        pressure++;
        pressureString = pressure + pressureString.substring(pressureString.indexOf("/"));
        getLabelPressure().setText(pressureString);
        controller.setRandomData(false);
    }

    @FXML
    private void eventSetPressure() {
        if (getTextFieldPressure() == null) {
            return;
        } else if (getTextFieldPressure().getText().trim().isEmpty()) {
            return;
        } else if ("Sem Paciente".equals(getLabelPressure().getText())) {
            return;
        } else {
            String pressureString = getLabelPressure().getText();
            pressureString = getTextFieldPressure().getText() + pressureString.substring(pressureString.indexOf("/"));
            getLabelPressure().setText(pressureString);
            controller.setRandomData(false);
        }
    }

    @FXML
    private void eventDecreasePressure() {

        if (getLabelPressure() == null) {
            return;
        } else if ("Sem Paciente".equals(getLabelPressure().getText())) {
            return;
        }
        String pressureString = getLabelPressure().getText();
        int pressure = Integer.parseInt(pressureString.substring(0, pressureString.indexOf("/")));
        if (pressure > 0) {
            pressure--;
        }
        pressureString = pressure + pressureString.substring(pressureString.indexOf("/"));
        getLabelPressure().setText(pressureString);
        controller.setRandomData(false);
    }

    @FXML
    private void eventIncreasePressureDia() {

        if (getLabelPressure() == null) {
            return;
        } else if ("Sem Paciente".equals(getLabelPressure().getText())) {
            return;
        }
        String pressureString = getLabelPressure().getText();
        int pressure = Integer.parseInt(pressureString.substring(pressureString.indexOf("/") + 1));
        pressure++;
        pressureString = pressureString.substring(0, pressureString.indexOf("/") + 1) + pressure;
        getLabelPressure().setText(pressureString);
        controller.setRandomData(false);
    }

    @FXML
    private void eventSetPressureDia() {
        if (getLabelPressure() == null) {
            return;
        } else if (getLabelPressure().getText().trim().isEmpty()) {
            return;
        } else if ("Sem Paciente".equals(getLabelPressure().getText())) {
            return;
        } else {
            String pressureString = getLabelPressure().getText();
            pressureString = pressureString.substring(0, pressureString.indexOf("/") + 1) + textFieldPressureDia.getText();
            getLabelPressure().setText(pressureString);
            controller.setRandomData(false);
        }
    }

    @FXML
    private void eventDecreasePressureDia() {

        if (getLabelPressure() == null) {
            return;
        } else if ("Sem Paciente".equals(getLabelPressure().getText())) {
            return;
        }
        String pressureString = getLabelPressure().getText();
        int pressure = Integer.parseInt(pressureString.substring(pressureString.indexOf("/") + 1));
        if (pressure > 0) {
            pressure--;
        }
        pressureString = pressureString.substring(0, pressureString.indexOf("/") + 1) + pressure;
        getLabelPressure().setText(pressureString);
        controller.setRandomData(false);
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
        controller.setRandomData(false);
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
            } catch (NumberFormatException e) {
                return;
            }
            getLabelBeats().setText(getTextFieldBeats().getText());
            controller.setRandomData(false);
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
        controller.setRandomData(false);
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
            } else {
                getLabelMovement().setText("Em Movimento");
            }
            controller.setRandomData(false);
        }
    }

    @FXML
    private void eventBtnRandomPressure() {
        Random random = new Random();
        getLabelPressure().setText(Integer.toString(random.nextInt(120) + 80) + "/" + Integer.toString(random.nextInt(70) + 60));
    }

    @FXML
    private void eventBtnRandomBeats() {
        Random random = new Random();
        getLabelBeats().setText(Integer.toString(random.nextInt(201)));
    }

    @FXML
    private void eventBtnRandomMovement() {
        Random random = new Random();
        if (random.nextInt(2) == 0) {
            eventSwitchStatusMovement();
        }
    }

    @FXML
    private void eventBtnConnect() {
        try {
            if(textFieldNome.getText().trim().isEmpty() || textFieldCpf.getText().trim().isEmpty() || textFieldNumero.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(null, "Insira os dados corretamente!");
                return;
            }
            controller.connectionServer(textFieldIpServer.getText(), textFieldPortServer.getText(), textFieldNome.getText(), textFieldCpf.getText(), textFieldNumero.getText());
            if (controller.isConnected()) {
                labelStatusConection.setText("Conectado");
                labelStatusConection.setDisable(false);
            } else {
                labelStatusConection.setText("Desconectado");
                labelStatusConection.setDisable(true);
            }
        } catch (IpServerInvalidException | PortServerInvalidException | IOException ex) {
            JOptionPane.showMessageDialog(null, "Ip ou Porta Inválida!");
        }
    }

    @FXML
    private void eventBtnSend() {

        try {
            controller.updateStatusPatient(labelBeats.getText(), labelMovement.getText(), labelPressure.getText());
        } catch (NullHeartBeatsException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidHeartBeatsException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullStatusMovementException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataInvalidException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void eventBtnAlterar() {
        String time = textFieldRangeRefresh.getText();
        if (time == null) {
            return;
        } else if (time.trim().isEmpty() || !time.matches("\\d+")) {
            return;
        } else {
            controller.setRangeRefresh(Integer.parseInt(time));
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

    /**
     * @return the btnRandomPressure
     */
    public Button getBtnRandomPressure() {
        return btnRandomPressure;
    }

    /**
     * @param btnRandomPressure the btnRandomPressure to set
     */
    public void setBtnRandomPressure(Button btnRandomPressure) {
        this.btnRandomPressure = btnRandomPressure;
    }

    /**
     * @return the btnRandomBeats
     */
    public Button getBtnRandomBeats() {
        return btnRandomBeats;
    }

    /**
     * @param btnRandomBeats the btnRandomBeats to set
     */
    public void setBtnRandomBeats(Button btnRandomBeats) {
        this.btnRandomBeats = btnRandomBeats;
    }

    /**
     * @return the btnRandomMovement
     */
    public Button getBtnRandomMovement() {
        return btnRandomMovement;
    }

    /**
     * @param btnRandomMovement the btnRandomMovement to set
     */
    public void setBtnRandomMovement(Button btnRandomMovement) {
        this.btnRandomMovement = btnRandomMovement;
    }

    /**
     * @return the textFieldPressureDia
     */
    public TextField getTextFieldPressureDia() {
        return textFieldPressureDia;
    }

    /**
     * @param textFieldPressureDia the textFieldPressureDia to set
     */
    public void setTextFieldPressureDia(TextField textFieldPressureDia) {
        this.textFieldPressureDia = textFieldPressureDia;
    }
}
