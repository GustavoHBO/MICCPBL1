/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.program.view.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import miccpbl1.client.program.controller.Controller;

/**
 * FXML Controller class
 *
 * @author gustavo
 */
public class ControllerView implements Initializable {

    @FXML
    private Button btnConectt = null;
    
    Controller controller;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = Controller.getInstance();
    }    
    
    @FXML
    private void eventBtnConect(){
        controller.testConection("127.0.0.1", 12345);
    }
}
