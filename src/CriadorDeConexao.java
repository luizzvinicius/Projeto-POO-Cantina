import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CriadorDeConexao {
  public static Connection criar() {
    try {
      var conexao = DriverManager.getConnection("jdbc:mysql://localhost", "root", "root");
      conexao.setAutoCommit(false);
      criarTabelas(conexao);
      return conexao;
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void criarTabelas(Connection conexao) throws IOException, SQLException {
    try (var stmt = conexao.createStatement()) {
      stmt.execute("USE cantina_flm;");
      return;
    } catch (SQLException ignored) {
    }

    try (var stmt = conexao.createStatement()) {
      var script = Files.readString(Paths.get("Criar-tabelas.sql"));
      script = script.replaceAll(",\r\n\\s*", ", ");
      script = script.replaceAll("\\(\r\n\\s+", "(");
      script = script.replaceAll("\\)\r\n", ")");

      for (var linha : script.split("\r\n")) {
        stmt.addBatch(linha);
      }

      stmt.executeBatch();
      conexao.commit();
    }
  }
}