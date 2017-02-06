package jp.toastkid.rtfx_verification;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class.
 *
 * @author Toast kid
 *
 */
public class Main extends Application {

    private Stage primaryStage;

    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        load();
        primaryStage.show();
    }


    /**
     * Load controller and scene from FXML.
     * @return Parent オブジェクト
     */
    private final void load() {
        final FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Main.fxml"));
        try {
            if (scene == null) {
                scene = new Scene(loader.load());
            }
        } catch (final IOException e) {
            e.printStackTrace();;
        }
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("RichTextFX verification");
    }

    public static void main(String[] args) {
        Application.launch(Main.class);
    }

}
