import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CriadorDeConexao {
  public static Connection criar() {
    try {
      return DriverManager.getConnection("jdbc:mysql://localhost/cantina", "root", "root");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
