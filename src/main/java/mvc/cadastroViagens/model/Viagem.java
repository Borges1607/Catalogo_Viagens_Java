package mvc.cadastroViagens.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Viagem {
    private static Agencia agencia;
    private String destino;
    private String datasaida;
    private String datachegada;
    private String descricao;
    private double preco;

    public Viagem(String destino, String datasaida, String datachegada, String descricao, double preco) {
        this.destino = destino;
        this.datasaida = datasaida;
        this.datachegada = datachegada;
        this.preco = preco;
        this.descricao = descricao;
    }






    public Viagem(String string, String string1, String descricaoDigitada, double v) {
    }

    public static Viagem verViagem(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cadastroviagens", "borges", "12345")) {
            String sql = "SELECT * FROM Viagem WHERE destino= ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String destino = rs.getString("destino");
                String datasaida = rs.getString("datasaida");
                String datachegada = rs.getString("datachegada");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");

                return new Viagem(destino, datasaida, datachegada, descricao, preco);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean adicionarViagem(Viagem viagem) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cadastroviagens", "borges", "12345")) {
            String sql = "INSERT INTO Viagem (destino, datasaida, datachegada, descricao, preco) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, viagem.destino);
            stmt.setString(2, viagem.datasaida);
            stmt.setString(3, viagem.datachegada);
            stmt.setString(4, viagem.descricao);
            stmt.setDouble(5, viagem.preco);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getDestino() {
        return destino;
    }

    public String getDatasaida() {
        return datasaida;
    }

    public String getDatachegada() {
        return datachegada;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public Agencia getAgencia() {
        return agencia;
    }


    public static class Agencia {
        private int idAgencia;
        private String nome;

        public Agencia(int idAgencia, String nome) {
            this.idAgencia = idAgencia;
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }
}
