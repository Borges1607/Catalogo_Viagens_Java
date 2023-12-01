package mvc.cadastroViagens.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mvc.cadastroViagens.StartApplication;
import mvc.cadastroViagens.model.Viagem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class HomeController {

    @FXML
    private ComboBox<Viagem.Agencia> Combobox_agencia;

    @FXML
    private Button btn_adicionarViagem;

    @FXML
    private Button btn_viagensCadastradas;

    @FXML
    private DatePicker date_chegada;

    @FXML
    private DatePicker date_saida;

    @FXML
    private Label lbl_resultado;

    @FXML
    private TextField txt_descricao;

    @FXML
    private TextField txt_destino;

    @FXML
    private TextField txt_passagem;
    private Viagem viagem;

    public class DatabaseConnection {
        private static final String JDBC_URL = "jdbc:mysql://localhost:3306/cadastroviagens";
        private static final String USERNAME = "borges";
        private static final String PASSWORD = "12345";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        }
    }

    @FXML
    public void initialize() {
        carregarAgencias();
    }

    private void carregarAgencias() {
        ObservableList<Viagem.Agencia> agencias = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM agencia";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idAgencia = rs.getInt("idAgencia");
                String nomeAgencia = rs.getString("nome");

                Viagem.Agencia agencia = new Viagem.Agencia(idAgencia, nomeAgencia);

                agencias.add(agencia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lbl_resultado.setText("Algo deu errado!!");
        }
        Combobox_agencia.setItems(agencias);
    }

    @FXML
    void cadastrar_viagem(MouseEvent event) {
        String destinoDigitado = txt_destino.getText();
        String descricaoDigitada = txt_descricao.getText();
        LocalDate dataSaidaInformado = date_saida.getValue();
        LocalDate dataChegadaInformado = date_chegada.getValue();
        String passagemDigitada = txt_passagem.getText();


        if (destinoDigitado != null && descricaoDigitada != null && dataSaidaInformado != null && dataChegadaInformado != null && passagemDigitada != null) {
            Viagem viagem = new Viagem(destinoDigitado, dataSaidaInformado.toString(), dataChegadaInformado.toString(), descricaoDigitada, Double.parseDouble(passagemDigitada));

            if (Viagem.adicionarViagem(viagem)) {
                lbl_resultado.setText("Cadastro de viagem efetuado com sucesso!!");
            } else {
                lbl_resultado.setText("Erro ao cadastrar a viagem no banco de dados.");
            }
        } else {
            lbl_resultado.setText("Algum campo não foi preenchido!!");
        }
    }


    public void ver_viagens() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/view/viagens.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Viagens Cadastradas");
            stage.setScene(scene);
            stage.show();
            Stage home = (Stage) btn_viagensCadastradas.getScene().getWindow();
            home.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Erro ao abrir o FXML");
        }
    }

}

