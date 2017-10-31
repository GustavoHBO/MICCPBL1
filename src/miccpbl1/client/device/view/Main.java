package miccpbl1.client.device.view;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static Stage principalStage;
    private static HaveStage haveStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.principalStage = stage;
        this.haveStage = HaveStage.instance(this,stage);
        Parent root = FXMLLoader.load(getClass().getResource("controller/Login.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Pulseira Inteligente");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> stage.close());
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void sw(Parent o){
        Scene s = new Scene(o);
        principalStage.setScene(s);
    }
}
