package Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Conexao.Conexao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import logica.App;

public class LoginController implements Initializable {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    @FXML
    private Button btnUser;

    @FXML
    private PasswordField passwordId;

    @FXML
    private TextField userId;

    private App app;

    public void setApp(App app) {
      this.app = app;
  }
  
    @FXML
    void OnClickedLogin(ActionEvent event) {
        con = Conexao.ConnectDB();
        String sql = "SELECT * FROM usuarios WHERE username=? AND password=?";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, userId.getText());
            pst.setString(2, passwordId.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
              app.showMainScreen();
            } else {
                System.out.println("Usuário ou senha incorretos.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Método de inicialização vazio
    }

}
