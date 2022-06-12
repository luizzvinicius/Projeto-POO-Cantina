import exceptions.ProdutoInvalidoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao {
  private static final String COMANDO_ADICIONAR = "INSERT INTO Produto " +
      "(nome, descricao, preco_venda, preco_compra, qtd_atual, qtd_comprada, estoque_minimo)" +
      "VALUES (?, ?, ?, ?, ?, ?, ?)";

  private static final String COMANDO_ATUALIZAR = "UPDATE Produto SET " +
      "qtd_atual = ?, qtd_vendida = ?, qtd_comprada = ? WHERE codigo = ?";

  private static final String COMANDO_GET_PRODUTOS = "SELECT * FROM Produto ORDER BY ? ?";
  private static final String COMANDO_REMOVER = "DELETE FROM Produto WHERE codigo = ?";

  private final Connection conexao;

  public ProdutoDao(Connection conexao) {
    this.conexao = conexao;
  }

  public void adicionar(Produto produto) throws ProdutoInvalidoException {
    produto.validarParaAdicionar();

    try (var stmt = this.conexao.prepareStatement(COMANDO_ADICIONAR, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, produto.getNome());
      stmt.setString(2, produto.getDescricao());
      stmt.setDouble(3, produto.getPrecoVenda());
      stmt.setDouble(4, produto.getPrecoCompra());
      stmt.setInt(5, produto.getQtdAtual());
      stmt.setInt(6, produto.getQtdAtual());
      stmt.setInt(7, produto.getEstoqueMinimo());
      stmt.execute();

      var result = stmt.getGeneratedKeys();
      result.next();
      produto.setCodigo(result.getInt(1));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Produto> getProdutos() {
    return this.getProdutos("nome", false);
  }

  public List<Produto> getProdutos(String propriedade, boolean decrescente) {
    var produtos = new ArrayList<Produto>();

    try (var stmt = this.conexao.prepareStatement(COMANDO_GET_PRODUTOS)) {
      stmt.setString(1, propriedade);
      stmt.setString(2, decrescente ? "desc" : "asc");
      var result = stmt.executeQuery();

      while (result.next()) {
        var codigo = result.getInt("codigo");
        var nome = result.getString("nome");
        var descricao = result.getString("descricao");
        var precoVenda = result.getDouble("preco_venda");
        var precoCompra = result.getDouble("preco_compra");
        var qtdAtual = result.getInt("qtd_atual");
        var qtdVendida = result.getInt("qtd_vendida");
        var qtdComprada = result.getInt("qtd_comprada");
        var estoqueMinimo = result.getInt("estoque_minimo");

        var produto = new Produto(codigo, nome, descricao, precoVenda, precoCompra, qtdAtual, qtdVendida, qtdComprada,
            estoqueMinimo);
        produtos.add(produto);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return produtos;
  }

  public void atualizar(Produto produto) {
    try (var stmt = this.conexao.prepareStatement(COMANDO_ATUALIZAR)) {
      stmt.setInt(1, produto.getQtdAtual());
      stmt.setInt(2, produto.getQtdVendida());
      stmt.setInt(3, produto.getQtdComprada());
      stmt.setInt(4, produto.getCodigo());
      stmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void remover(Produto produto) {
    try (var stmt = this.conexao.prepareStatement(COMANDO_REMOVER)) {
      stmt.setInt(1, produto.getCodigo());
      stmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}