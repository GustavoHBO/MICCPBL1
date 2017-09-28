package miccpbl1.client.device.view;

import java.awt.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ControllerView implements Initializable {

    private Stage primaryStage = null;
    private AnchorPane mainLayout = null;

    /* Declaração do drop down */
    @FXML
    private SplitMenuButton listPacientes = null;

    /* Declaração dos textos */
    @FXML
    private TextField textBatimentos = null;
    @FXML
    private TextField textPressao = null;
    @FXML
    private TextField textMovimento = null;
    @FXML
    private TextField setTextoBatimentos = null;
    @FXML
    private TextField setTextoPressao = null;

    /* Declaração dos botões */
    @FXML
    private Button aumentarBatimentos = null;
    @FXML
    private Button diminuirBatimentos = null;
    @FXML
    private Button aumentarPressao = null;
    @FXML
    private Button diminuirPressao = null;
    @FXML
    private Button trocarMovimento = null;
    @FXML
    private Button btnEnviar = null;

    /* Declaração do checkbox */
    @FXML
    private CheckBox checkPacienteRisco = null;

    @FXML
    public void showMainScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("controller/Interface.fxml"));
        mainLayout = loader.load();
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void prepararBtnEnviar() {
        //System.out.println("Botao Funcionando!");
        JOptionPane.showMessageDialog(null, "Olá");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
