package mvc.cadastroViagens.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import mvc.cadastroViagens.StartApplication;
import mvc.cadastroViagens.model.Usuario;
import mvc.cadastroViagens.model.Viagem;
import java.io.IOException;
import java.sql.*;

public class ViagensController {

    private static Usuario agencia;

    @FXML
    private TableView<Viagem> tb_viagens;

    @FXML
    private TableColumn<Viagem, String> cl_destino;

    @FXML
    private TableColumn<Viagem, String> cl_dtChegada;

    @FXML
    private TableColumn<Viagem, String> cl_DtSaida;

    @FXML
    private TableColumn<Viagem, Double> cl_valor;

    @FXML
    private TableColumn<Viagem, String> cl_descricao;

    @FXML
    private Button btn_excluir;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/cadastroviagens";
    private static final String JDBC_USER = "borges";
    private static final String JDBC_PASSWORD = "12345";

    @FXML
    void excluirViagemPorDestino() {
        Viagem viagemSelecionada = tb_viagens.getSelectionModel().getSelectedItem();

        if (viagemSelecionada != null) {
            String destino = viagemSelecionada.getDestino();

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText(null);
            alert.setContentText("Você tem certeza de que deseja excluir a viagem para " + destino + "?");

            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            ButtonType resultado = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (resultado == ButtonType.OK) {
                try {
                    Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Viagem WHERE destino = ?");
                    preparedStatement.setString(1, destino);
                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                    connection.close();

                    viagens.remove(viagemSelecionada);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private ObservableList<Viagem> viagens = FXCollections.observableArrayList();

    public void initialize() {
        cl_destino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        cl_DtSaida.setCellValueFactory(new PropertyValueFactory<>("datasaida"));
        cl_dtChegada.setCellValueFactory(new PropertyValueFactory<>("datachegada"));
        cl_valor.setCellValueFactory(new PropertyValueFactory<>("preco"));
        cl_descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Viagem");

            while (resultSet.next()) {
                String destino = resultSet.getString("destino");
                String dataChegada = resultSet.getString("datachegada");
                String dataSaida = resultSet.getString("datasaida");
                double preco = resultSet.getDouble("preco");
                String descricao = resultSet.getString("descricao");

                Viagem viagem = new Viagem(destino, dataChegada, dataSaida, descricao, preco);
                viagens.add(viagem);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tb_viagens.setItems(viagens);
    }

    @FXML
    void voltar_cadastro() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/view/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
            Stage viagens = (Stage) tb_viagens.getScene().getWindow();
            viagens.close();
        } catch (IOException e) {
            System.out.println("Erro ao abrir o FXML");
        }
    }
}
