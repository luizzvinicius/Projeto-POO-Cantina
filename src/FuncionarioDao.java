import exceptions.FuncionarioOpException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class FuncionarioDao {
  private static final String COMANDO_CADASTRAR = "INSERT INTO Funcionario (email, nome, senha) VALUES (?, ?, ?)";
  private static final String COMANDO_LOGIN = "SELECT * FROM Funcionario WHERE email = ? AND senha = ?";

  private final Connection conexao;

  public FuncionarioDao(Connection conexao) {
    this.conexao = conexao;
  }

  public void cadastrar(Funcionario funcionario) throws FuncionarioOpException {
    try (var stmt = this.conexao.prepareStatement(COMANDO_CADASTRAR)) {
      stmt.setString(1, funcionario.getEmail());
      stmt.setString(2, funcionario.getNome());
      stmt.setString(3, funcionario.getSenha());
      stmt.execute();
    } catch (SQLIntegrityConstraintViolationException e) {
      throw new FuncionarioOpException("Email já cadastrado");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Funcionario entrar(String email, String senha) throws FuncionarioOpException {
    try (var stmt = this.conexao.prepareStatement(COMANDO_LOGIN)) {
      stmt.setString(1, email);
      stmt.setString(2, senha);
      var result = stmt.executeQuery();

      if (result.next()) {
        return new Funcionario(result.getString("nome"), email, senha);
      }

      throw new FuncionarioOpException("Email/ou senha incorretos");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}