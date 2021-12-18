package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainPageController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TabPane myProjects;

    @FXML
    private ImageView myAvatarImageView;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }



    public void newProject() {

        // create a text input dialog
        TextInputDialog td = new TextInputDialog("Enter project name");

        td.showAndWait();

        String projectName = td.getEditor().getText();
        Tab tab1 = new Tab(projectName);
        myProjects.getTabs().add(tab1);
    }

    public void logout(ActionEvent event) throws IOException {
        Parent pane = FXMLLoader.load(
                getClass().getResource("loginin.fxml"));

        Scene scene = new Scene(pane);

        Stage stage = (Stage) (anchorPane.getScene().getWindow());

        stage.setScene(scene);
        stage.show();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        myAvatarImageView.setOnMouseClicked(e -> {
            File selectedDirectory = directoryChooser.showDialog(stage);
            System.out.println(selectedDirectory.getAbsolutePath());
        });
    }
}
