package miccpbl1.client.device.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static Stage principalStage;
    private static HaveStage haveStage;
    private static int v = 0;

    @Override
    public void start(Stage stage) throws Exception {
        this.principalStage = stage;
        this.haveStage = HaveStage.instance(this, stage);
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

    public static void sw() {

        Runnable run;
        run = new Runnable() {
            @Override
            public void run() {
                while (v == 0) {

                }
                Parent o;
                try {
                    o = FXMLLoader.load(getClass().getResource("controller/Login.fxml"));
                    Scene s = new Scene(o);
                    principalStage.setScene(s);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        Thread th;
        th = new Thread(run);
        th.start();
    }
    
    public static void setV(int valor){
        v = valor;
    }
}
