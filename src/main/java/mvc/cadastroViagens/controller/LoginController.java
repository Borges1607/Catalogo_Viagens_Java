package mvc.cadastroViagens.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mvc.cadastroViagens.StartApplication;
import mvc.cadastroViagens.model.Usuario;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private Button btn_entrar;

    @FXML
    private Button btn_cadastro;

    @FXML
    private Label lbl_resultado;

    @FXML
    private PasswordField txt_senha;

    @FXML
    private TextField txt_usuario;

    @FXML
    public void efetuar_login() {
        String email = txt_usuario.getText();
        String senha = txt_senha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            lbl_resultado.setText("Preencha todos os campos!");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cadastroviagens", "borges", "12345")) {
            String senhaHash = DigestUtils.md5Hex(senha);

            String sql = "SELECT * FROM Usuario WHERE email = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senhaHash);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("idUsuario");
                String nome = rs.getString("nome");
                abrirTelaInicial();
                Usuario usuario = new Usuario(idUsuario, nome, email, senhaHash);
            } else {
                lbl_resultado.setText("Credenciais inválidas!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirTelaInicial() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/view/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
            Stage login = (Stage) btn_entrar.getScene().getWindow();
            login.close();
        } catch (IOException e) {
            System.out.println("Erro ao abrir o FXML");
        }
    }

    @FXML
    public void Nao_tenho_Cadastro() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/view/cadastro.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Cadastro");
            stage.setScene(scene);
            stage.show();
            Stage login = (Stage) btn_cadastro.getScene().getWindow();
            login.close();
        } catch (IOException e) {
            System.out.println("Erro ao abrir o FXML");
        }
    }
}
