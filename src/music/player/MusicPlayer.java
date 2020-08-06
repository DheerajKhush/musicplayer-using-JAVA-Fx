/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.player;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author user
 */
public class MusicPlayer extends Application {

    String path;
    public static Stage stage = null;

    @Override
    public void start(Stage stage) throws Exception {
//      
//        stage.initStyle(StageStyle.TRANSPARENT);
        Pane root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        //Pane root= rootLoader.load();
//        root.getChildren().add(mediaView);  
        this.stage = stage;
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.initStyle(StageStyle.UNIFIED);
        stage.setScene(scene);
        stage.show();

    }

    //
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
