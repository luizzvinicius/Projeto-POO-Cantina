import java.sql.Connection;
import java.sql.SQLException;

public class CadastraDao {
  private static final String COMANDO_ADICIONAR = "INSERT INTO Cadastra (email_func, cod_produto) VALUES (?, ?)";

  private final Connection conexao;

  public CadastraDao(Connection conexao) {
    this.conexao = conexao;
  }

  public void adicionar(Funcionario funcionario, int codProduto) {
    try {
      var stmt = this.conexao.prepareStatement(COMANDO_ADICIONAR);
      stmt.setString(1, funcionario.getEmail());
      stmt.setInt(2, codProduto);
      stmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}