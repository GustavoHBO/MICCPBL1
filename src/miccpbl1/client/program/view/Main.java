package miccpbl1.client.program.view;


import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import miccpbl1.client.program.controller.Controller;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("controller/Interface.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Pulseira Inteligente");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(Controller.getInstance().receiveData());
        launch(args);
    }
}
