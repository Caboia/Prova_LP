package logica;

import java.io.IOException;

import Controllers.LoginController;
import Controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{

    private Stage primaryStage;
@Override

public void start(Stage stage) throws Exception {
    this.primaryStage = stage;
    showLoginScreen();
    }
public void showLoginScreen() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/login.fxml"));
    Parent root = loader.load();

    LoginController loginController = loader.getController();
    loginController.setApp(this);


    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
}
public void showMainScreen() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/main.fxml"));
    Parent root = loader.load();

    MainController mainController = loader.getController();
    mainController.setApp(this);

    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
}
public static void main(String[]args) {
    launch(args);
    }
}