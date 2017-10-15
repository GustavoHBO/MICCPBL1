/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.program.view.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import miccpbl1.client.exceptions.IpServerInvalidException;
import miccpbl1.client.exceptions.PortServerInvalidException;
import miccpbl1.client.program.controller.Controller;
import miccpbl1.model.doctor.Paciente;
import miccpbl1.model.doctor.Pessoa;

/**
 *
 * @author gustavo
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TableView tableRisk = null;
    @FXML
    private TableView tablePatient = null;
    @FXML
    private TableView tablePatients = null;
    @FXML
    private TableColumn<Pessoa, String> tableColumnNamePatients = null;
    @FXML
    private TableColumn<Pessoa, String> tableColumnCpfPatients = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnNameRisk = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnCpfRisk = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnBeatsRisk = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnPressureRisk = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnMovementRisk = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnNamePatientsDoctor = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnCpfPatientsDoctor = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnBeatsPatientsDoctor = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnPressurePatientsDoctor = null;
    @FXML
    private TableColumn<Paciente, String> tableColumnMovementPatientsDoctor = null;
    @FXML
    private TextField textFieldNome = null;
    @FXML
    private TextField textFieldCpf = null;
    @FXML
    private TextField textFieldNumero = null;
    @FXML
    private TextField textFieldCrm = null;
    @FXML
    private TextField textFieldIp = null;
    @FXML
    private TextField textFieldPort = null;

    @FXML
    private Button btnConnect = null;

    @FXML
    private Label labelPressure = null;
    @FXML
    private Label labelBeats = null;
    @FXML
    private Label labelMovement = null;
    @FXML
    private Label labelStatusConnection = null;

    private ObservableList<Pessoa> personData = FXCollections.observableArrayList();

    private Controller controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = Controller.getInstance();
        povoarTabelaPacientes();

    }

    @FXML
    private void eventBtnConnect() {

        try {
            controller.connectionServer(textFieldIp.getText(), textFieldPort.getText(), textFieldNome.getText(), textFieldCpf.getText(), textFieldNumero.getText(), textFieldCrm.getText());
            if (controller.isConnected()) {
                labelStatusConnection.setText("Conectado");
                labelStatusConnection.setDisable(false);
            } else {
                labelStatusConnection.setText("Desconectado");
                labelStatusConnection.setDisable(true);
            }
        } catch (IpServerInvalidException | PortServerInvalidException | IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void povoarTabelaPacientes() {
        
        tableColumnNamePatients.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnCpfPatients.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        ArrayList<Paciente> list;
        list = controller.getListPatient();
        Pessoa p;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            p = (Pessoa)it.next();
            personData.add(p);
        }
        tableColumnNamePatients.setCellValueFactory(cellData -> cellData.getValue().getNome());
        tableColumnCpfPatients.setCellValueFactory(cellData -> cellData.getValue().getCPF());
        tablePatients.setItems(personData);
    }

}
