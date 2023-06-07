package logica;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class Cliente {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nome;
    private SimpleStringProperty cpf;
    private SimpleObjectProperty<Date> dataNascimento;
    private SimpleFloatProperty altura;
    private SimpleFloatProperty peso;
    private SimpleFloatProperty imc;

    public Cliente(int id, String nome, String cpf, Date dataNascimento, float altura, float peso, float imc) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.cpf = new SimpleStringProperty(cpf);
        this.dataNascimento = new SimpleObjectProperty<>(dataNascimento);
        this.altura = new SimpleFloatProperty(altura);
        this.peso = new SimpleFloatProperty(peso);
        this.imc = new SimpleFloatProperty(imc);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNome() {
        return nome.get();
    }

    public SimpleStringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getCpf() {
        return cpf.get();
    }

    public SimpleStringProperty cpfProperty() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf.set(cpf);
    }

    public Date getDataNascimento() {
        return dataNascimento.get();
    }

    public SimpleObjectProperty<Date> dataNascimentoProperty() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento.set(dataNascimento);
    }

    public float getAltura() {
        return altura.get();
    }

    public SimpleFloatProperty alturaProperty() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura.set(altura);
    }

    public float getPeso() {
        return peso.get();
    }

    public SimpleFloatProperty pesoProperty() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso.set(peso);
    }

    public float getImc() {
        return imc.get();
    }

    public SimpleFloatProperty imcProperty() {
        return imc;
    }

    public void setImc(float imc) {
        this.imc.set(imc);
    }
}
