import java.sql.Connection;
import java.sql.SQLException;

public class ItemDao {
    private static final String COMANDO_CADASTRAR = "INSERT INTO Item (cod_venda, cod_produto, qtdidade, preco)" + "VALUES (?, ?, ?, ?)";
    
    private final Connection conexao;

    public ItemDao(Connection conexao) {
      this.conexao = conexao;
    }
    
    public void adicionar(Item item) {
      try {
        var stmt = this.conexao.prepareStatement(COMANDO_CADASTRAR);;
        stmt.setInt(1, item.getCodVenda());
        stmt.setInt(2, item.getCodProduto());
        stmt.setInt(3, item.getQuantidade());
        stmt.setDouble(4, item.getPreco());
        stmt.execute();
      } catch (SQLException e) {
        throw new RuntimeException(e);   
      }
  }
}