import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemDao {
  private static final String COMANDO_CADASTRAR = "INSERT INTO Item " +
      "(cod_venda, cod_produto, qtd, preco) VALUES (?, ?, ?, ?)";

  private final Connection conexao;

  public ItemDao(Connection conexao) {
    this.conexao = conexao;
  }

  public void adicionar(Item item) {
    try (var stmt = this.conexao.prepareStatement(COMANDO_CADASTRAR, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, item.getCodVenda());
      stmt.setInt(2, item.getCodProduto());
      stmt.setInt(3, item.getQtd());
      stmt.setDouble(4, item.getPreco());
      stmt.execute();

      var result = stmt.getGeneratedKeys();
      result.next();
      item.setCodigo(result.getInt(1));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}