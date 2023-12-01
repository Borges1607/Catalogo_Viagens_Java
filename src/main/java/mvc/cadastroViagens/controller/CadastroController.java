package mvc.cadastroViagens.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mvc.cadastroViagens.StartApplication;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CadastroController {

    @FXML
    private Button btn_cadastrar;

    @FXML
    private TextField txt_nome;

    @FXML
    private TextField txt_email;

    @FXML
    private PasswordField txt_senha;

    @FXML
    void cadastrar(MouseEvent event) {
        String nome = txt_nome.getText();
        String email = txt_email.getText();
        String senha = txt_senha.getText();

        if (nome != null && email != null && senha != null) {
            boolean cadastradoComSucesso = cadastrarUsuario(nome, email, senha);

            if (cadastradoComSucesso) {
                System.out.println("Cadastro de usuário efetuado com sucesso!");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/view/login.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Login");
                    stage.setScene(scene);
                    stage.show();
                    Stage cadastro = (Stage)btn_cadastrar.getScene().getWindow();
                    cadastro.close();
                }
                catch (IOException e){
                    System.out.println("Erro ao abrir o FXML");
                }
            } else {
                System.out.println("Erro ao cadastrar usuário.");
            }
        } else {
            System.out.println("Por favor, preencha todos os campos.");
        }
    }
    public static boolean cadastrarUsuario(String nome, String email, String senha) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cadastroviagens", "borges","12345")) {
            String senhaHash = DigestUtils.md5Hex(senha);

            String sql = "INSERT INTO Usuario (nome, email, senha) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senhaHash);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao abrir o FXML");
            return false;
        }
    }
}