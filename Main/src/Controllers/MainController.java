package Controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import Conexao.Conexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import logica.App;
import logica.Cliente;

public class MainController implements Initializable {
    @FXML
    private TableView<Cliente> tvClientes;
    
    @FXML
    private TableColumn<Cliente, String> colNome;
    
    @FXML
    private TableColumn<Cliente, Float> colAltura;
    
    @FXML
    private TableColumn<Cliente, Float> colPeso;
    
    @FXML
    private TableColumn<Cliente, Float> colIMC;
    
    @FXML
    private TextField userId;
    
    @FXML
    private TextField cpfId;
    
    @FXML
    private TextField alturaId;
    
    @FXML
    private TextField pesoId;
    
    @FXML
    private DatePicker dateId;
    
    @FXML
    private Button btnSend;
    
    @FXML
    private Button btnUpdate;
    
    @FXML
    private Button btnDelete;
    
    private ObservableList<Cliente> clientesList;
    
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    public void setApp(App app) {
    }
    
    @FXML
    void OnClickedOk(ActionEvent event) {
        String nome = userId.getText();
        String cpf = cpfId.getText();
        float altura = Float.parseFloat(alturaId.getText());
        float peso = Float.parseFloat(pesoId.getText());
        java.sql.Date dataNascimento = java.sql.Date.valueOf(dateId.getValue());
        float imc = calcularIMC(altura, peso);
        
        Cliente cliente = new Cliente(0, nome, cpf, dataNascimento, altura, peso, imc);
        
        if (inserirCliente(cliente)) {
            clientesList.add(cliente);
            limparCampos();
            exibirAlerta(AlertType.INFORMATION, "Sucesso", "Cliente adicionado com sucesso!");
        } else {
            exibirAlerta(AlertType.ERROR, "Erro", "Não foi possível adicionar o cliente.");
        }
        gerarRelatorioCliente(cliente);
    }
    
    @FXML
void OnClickedUpdate(ActionEvent event) {
    Cliente clienteSelecionado = tvClientes.getSelectionModel().getSelectedItem();
    
    if (clienteSelecionado != null) {
        String nome = userId.getText();
        String cpf = cpfId.getText();
        float altura = Float.parseFloat(alturaId.getText());
        float peso = Float.parseFloat(pesoId.getText());
        java.sql.Date dataNascimento = java.sql.Date.valueOf(dateId.getValue());
        float imc = calcularIMC(altura, peso);
        
        Cliente clienteAtualizado = new Cliente(clienteSelecionado.getId(), nome, cpf, dataNascimento, altura, peso, imc);
        
        if (atualizarCliente(clienteAtualizado)) {
            int indice = clientesList.indexOf(clienteSelecionado);
            clientesList.set(indice, clienteAtualizado);
            limparCampos();
            exibirAlerta(AlertType.INFORMATION, "Sucesso", "Cliente atualizado com sucesso!");
        } else {
            exibirAlerta(AlertType.ERROR, "Erro", "Não foi possível atualizar o cliente. Verifique o console para mais informações.");
        }
    } else {
        exibirAlerta(AlertType.WARNING, "Atenção", "Nenhum cliente selecionado.");
    }
}

    @FXML
    void OnClickedDelete(ActionEvent event) {
        Cliente clienteSelecionado = tvClientes.getSelectionModel().getSelectedItem();
        
        if (clienteSelecionado != null) {
            if (excluirCliente(clienteSelecionado)) {
                clientesList.remove(clienteSelecionado);
                limparCampos();
                exibirAlerta(AlertType.INFORMATION, "Sucesso", "Cliente excluído com sucesso!");
            } else {
                exibirAlerta(AlertType.ERROR, "Erro", "Não foi possível excluir o cliente.");
            }
        } else {
            exibirAlerta(AlertType.WARNING, "Atenção", "Nenhum cliente selecionado.");
        }
    }
    
    private float calcularIMC(float altura, float peso) {
        return peso / (altura * altura);
    }
    
    private void limparCampos() {
        userId.setText("");
        cpfId.setText("");
        alturaId.setText("");
        pesoId.setText("");
        dateId.setValue(null);
    }
    
    private void exibirAlerta(AlertType alertType, String titulo, String mensagem) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    private boolean inserirCliente(Cliente cliente) {
        try {
            con = Conexao.ConnectDB();
            String query = "INSERT INTO clientes (nome, cpf, data_nascimento, altura, peso, IMC) VALUES (?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(query);
            pst.setString(1, cliente.getNome());
            pst.setString(2, cliente.getCpf());
            pst.setDate(3, cliente.getDataNascimento());
            pst.setFloat(4, cliente.getAltura());
            pst.setFloat(5, cliente.getPeso());
            pst.setFloat(6, cliente.getImc());
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            exibirAlerta(AlertType.ERROR, "Erro", "Erro ao inserir o cliente: " + e.getMessage());
            return false;
        } finally {
            fecharConexao();
        }
    }
    
    private boolean atualizarCliente(Cliente cliente) {
        try {
            con = Conexao.ConnectDB();
            String query = "UPDATE clientes SET nome = ?, cpf = ?, data_nascimento = ?, altura = ?, peso = ?, IMC = ? WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, cliente.getNome());
            pst.setString(2, cliente.getCpf());
            pst.setDate(3, cliente.getDataNascimento());
            pst.setFloat(4, cliente.getAltura());
            pst.setFloat(5, cliente.getPeso());
            pst.setFloat(6, cliente.getImc());
            pst.setInt(7, cliente.getId());
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            exibirAlerta(AlertType.ERROR, "Erro", "Erro ao atualizar o cliente: " + e.getMessage());
            return false;
        } finally {
            fecharConexao();
        }
    }
    
    private boolean excluirCliente(Cliente cliente) {
        try {
            con = Conexao.ConnectDB();
            String query = "DELETE FROM clientes WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, cliente.getId());
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            exibirAlerta(AlertType.ERROR, "Erro", "Erro ao excluir o cliente: " + e.getMessage());
            return false;
        } finally {
            fecharConexao();
        }
    }
    
    private void fecharConexao() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            exibirAlerta(AlertType.ERROR, "Erro", "Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
        }
    }
    private void gerarRelatorioCliente(Cliente cliente) {
        String nomeArquivo = cliente.getNome() + "_" + cliente.getCpf() + ".txt";
    
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            LocalDate dataAtual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
            writer.write("Relatório do Cliente\n");
            writer.write("====================\n");
            writer.write("Nome: " + cliente.getNome() + "\n");
            writer.write("CPF: " + cliente.getCpf() + "\n");
            writer.write("Data de Nascimento: " + cliente.getDataNascimento().toString() + "\n");
            writer.write("Data do Relatório: " + dataAtual.format(formatter) + "\n");
            writer.write("IMC: " + cliente.getImc() + "\n");
    
            if (cliente.getImc() < 16.9) {
                writer.write("Status: Muito abaixo do peso\n");
            } else if (cliente.getImc() < 18.4) {
                writer.write("Status: Abaixo do peso\n");
            } else if (cliente.getImc() < 24.9) {
                writer.write("Status: Peso na média\n");
            } else if (cliente.getImc() < 29.9) {
                writer.write("Status: Acima do peso\n");
            } else if (cliente.getImc() < 34.9) {
                writer.write("Status: Obesidade grau I\n");
            } else if (cliente.getImc() < 40) {
                writer.write("Status: Obesidade grau II\n");
            } else {
                writer.write("Status: Obesidade grau III\n");
            }
    
            writer.write("\n");
    
            System.out.println("Arquivo " + nomeArquivo + " gerado com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao gerar o arquivo: " + e.getMessage());
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colAltura.setCellValueFactory(cellData -> cellData.getValue().alturaProperty().asObject());
        colPeso.setCellValueFactory(cellData -> cellData.getValue().pesoProperty().asObject());
        colIMC.setCellValueFactory(cellData -> cellData.getValue().imcProperty().asObject());
        
        clientesList = FXCollections.observableArrayList();
        tvClientes.setItems(clientesList);
        
        carregarClientes();
    }
    
    private void carregarClientes() {
        try {
            con = Conexao.ConnectDB();
            String query = "SELECT * FROM clientes";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                java.sql.Date dataNascimento = rs.getDate("data_nascimento");
                float altura = rs.getFloat("altura");
                float peso = rs.getFloat("peso");
                float imc = rs.getFloat("IMC");
                
                Cliente cliente = new Cliente(id, nome, cpf, dataNascimento, altura, peso, imc);
                clientesList.add(cliente);
            }
        } catch (SQLException e) {
            exibirAlerta(AlertType.ERROR, "Erro", "Erro ao carregar os clientes: " + e.getMessage());
        } finally {
            fecharConexao();
        }
    }


    
}