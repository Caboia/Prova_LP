package Conexao;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class Conexao {
    public static Connection ConnectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/academia", "root", "1234");
            return con;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, null, 0);
            return null;
        }
    }
}
