import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class FuncionarioDao {
  private static final String COMANDO_CADASTRAR = "INSERT INTO Funcionario " +
      "(email, nome, senha) VALUES (?, ?, ?)";

  private static final String COMANDO_LOGIN = "SELECT * FROM Funcionario WHERE email = ? AND senha = ?";

  private final Connection conexao;

  public FuncionarioDao(Connection conexao) {
    this.conexao = conexao;
  }

  public void cadastrar(Funcionario funcionario) throws FuncionarioJaCadastradoException {
    try {
      var stmt = this.conexao.prepareStatement(COMANDO_CADASTRAR);
      stmt.setString(1, funcionario.getEmail());
      stmt.setString(2, funcionario.getNome());
      stmt.setString(3, funcionario.getSenha());
      stmt.execute();
    } catch (SQLIntegrityConstraintViolationException e) {
      throw new FuncionarioJaCadastradoException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String login(String email, String senha) {
    try {
      var stmt = this.conexao.prepareStatement(COMANDO_LOGIN);
      stmt.setString(1, email);
      stmt.setString(2, senha);
      var result = stmt.executeQuery();

      if (!result.next()) {
        return null;
      }

      return result.getString("nome");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
