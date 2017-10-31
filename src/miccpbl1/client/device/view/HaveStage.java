/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author gustavo
 */
public class HaveStage {
    private static HaveStage haveStage = null;
    private static Main main;
    private Stage stage;

    private HaveStage(Stage stage) {
        this.stage = stage;
    }

    public static HaveStage instance(Stage stage) {
        if (haveStage == null) {
            haveStage = new HaveStage(stage);
        }
        return haveStage;
    }
    
    public static HaveStage instance(Main main2, Stage stage) {
        if (haveStage == null) {
            main = main2;
            haveStage = new HaveStage(stage);
        }
        return haveStage;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void loadNewStage(Parent fxmlLoad) {
        if (stage != null) {
            Parent root = fxmlLoad;
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //stage.show();
        }
    }
}
