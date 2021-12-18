package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ImageView myAvatarImageView;


    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private AnchorPane anchorPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image image = new Image(getClass().getResourceAsStream("/images/projectmanagement2.jpg"));
        myAvatarImageView.setImage(image);
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

    }

    @FXML
    public void login(ActionEvent event) throws SQLException {

        String username = usernameField.getText();
        String password = passwordField.getText();

        Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Either the username or password is empty");
            a.setX(stage.getX());
            a.setY(stage.getY());
            a.showAndWait();
            return;
        }

        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\mrixo\\demo1\\testdb.db");

            ResultSet tables = conn.getMetaData().getTables(null, null, "Users", null);
            if (!tables.next()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setX(stage.getX());
                a.setY(stage.getY());
                a.setContentText("The username and password combination is invalid");
                a.show();
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE Users " +
                        "(username String not NULL, " +
                        " password VARCHAR(255), " +
                        " PRIMARY KEY ( username ))";
                stmt.executeQuery(sql);
                return;
            }

            PreparedStatement pSt = conn.prepareStatement("Select username, password from Users where username=? and password=?");

            pSt.setString(1, username);
            pSt.setString(2, password);
            ResultSet rs = pSt.executeQuery();

            rs.next();
            int numRows = rs.getRow();

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            conn.close();

            if (numRows == 0) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("The username and password combination is invalid");
                a.setX(stage.getX());
                a.setY(stage.getY());
                a.showAndWait();
            } else if (numRows == 1) {
                System.out.println("Successful login");

                Parent pane = FXMLLoader.load(
                        getClass().getResource("mainpage.fxml"));

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                MainPageController controller = (MainPageController) fxmlLoader.getController();

                stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                controller.setStage(stage);

                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();


            } else {
                throw new DuplicateDetailsException();
            }
            System.out.println("Table contains "+rs.getRow()+" rows");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (DuplicateDetailsException e) {
            System.out.println("Duplicate login details in Database");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void createAccount(ActionEvent event) throws IOException {
        System.out.println("Create account");
        FXMLLoader loader = new FXMLLoader();
        Parent pane = FXMLLoader.load(
                getClass().getResource("createAccount.fxml"));

        Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

    }

}
