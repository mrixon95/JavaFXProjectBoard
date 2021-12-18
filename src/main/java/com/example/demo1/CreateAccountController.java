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
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {

    @FXML
    private ImageView myAvatarImageView;


    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginPageButton;

    @FXML
    private Button signInPageButton;

    @FXML
    private AnchorPane anchorPane;

    Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image image = new Image(getClass().getResourceAsStream("/images/projectmanagement2.jpg"));
        myAvatarImageView.setImage(image);
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

    }


    @FXML
    public void signInPage(ActionEvent event) throws SQLException, IOException {
        Parent pane = FXMLLoader.load(
                getClass().getResource("loginin.fxml"));
        Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void createAccount(ActionEvent event) throws SQLException {

        String username = usernameField.getText();
        String password = passwordField.getText();

        stage = (Stage) anchorPane.getScene().getWindow();


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

            ResultSet tables = conn.getMetaData().getTables(null, null, "users", null);
            if (!tables.next()) {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE Users " +
                        "(username String not NULL, " +
                        " password VARCHAR(255), " +
                        " PRIMARY KEY ( username ))";
                stmt.executeQuery(sql);
            }


            PreparedStatement pSt = conn.prepareStatement("Select username from Users where username=?");

            pSt.setString(1, username);

            ResultSet rs = pSt.executeQuery();
            rs.next();

            int numRows = rs.getRow();

            pSt.close();

            if (numRows == 0) {
                // crete new user
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Users " +
                                "VALUES (?,?)");
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();

                stmt.close();
                conn.close();

                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("Successfully created new user");
                a.setX(stage.getX());
                a.setY(stage.getY());
                a.show();

                Parent pane = FXMLLoader.load(
                        getClass().getResource("loginin.fxml"));
                Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();

                return;

            } else if (numRows == 1) {
                System.out.println("Username already exists");

                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Username '" + username + "' already exists");


                a.setX(stage.getX());
                a.setY(stage.getY());
                a.show();
                conn.close();
                return;
            } else {
                conn.close();
                throw new DuplicateDetailsException();

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (DuplicateDetailsException e) {
            System.out.println("Duplicate login details in Database");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }






}
