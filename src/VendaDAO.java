import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {
  private static final String COMANDO_CADASTRAR = "INSERT INTO Venda "
      + "(codigo, data_venda, email_func, forma_pagamento, total_venda, desconto)" + "VALUES (?, ?, ?, ?, ?, ?)";

  private static final String COMANDO_ATUALIZAR = "UPDATE Venda SET "
      + "desconto = ?, forma_pagamento = ?, WHERE codigo = ?";

  private static final String COMANDO_GET_VENDA = "SELECT * FROM Venda ORDER BY ? ?";

  private static final String COMANDO_REMOVER = "DELETE FROM Venda WHERE codigo = ?";

  private final Connection conexao;

  public VendaDAO(Connection conexao) {
    this.conexao = conexao;
  }

  public void adicionarVenda(Venda venda) {
    try {
      var stmt = this.conexao.prepareStatement(COMANDO_CADASTRAR);
      stmt.setInt(1, venda.getCodVenda());
      //stmt.setDate(2, venda.getDataVenda()); // ué ?
      stmt.setString(3, venda.getEmailFunc());
      stmt.setString(4, venda.getFormaPagamento());
      stmt.setDouble(5, venda.getTotVenda());
      stmt.setDouble(6, venda.getDesconto());
      stmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void removeVenda(Venda venda) {
    try {
      var stmt = this.conexao.prepareStatement(COMANDO_REMOVER);
      stmt.setInt(1, venda.getCodVenda());
      stmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void alterarVenda(Venda venda) {
    try {
      var stmt = this.conexao.prepareStatement(COMANDO_ATUALIZAR);
      stmt.setDouble(1, venda.getDesconto());
      stmt.setString(2, venda.getFormaPagamento());
      stmt.setInt(3, venda.getCodVenda());
      stmt.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Venda> getVenda(String propriedade, boolean decrescente) {
    var vendas = new ArrayList<Venda>();
    try {
      var stmt = this.conexao.prepareStatement(COMANDO_GET_VENDA);
      stmt.setString(1, propriedade);
      stmt.setString(2, decrescente ? "desc" : "asc");
      var result = stmt.executeQuery();

      while (result.next()) {
        var codVenda = result.getInt("codigo");
        var dataVenda = result.getDate("data_venda");
        var emailFunc = result.getString("email_func");
        var formaPagamento = result.getString("forma_pagamento");
        var totVenda = result.getDouble("total_venda");
        var desconto = result.getDouble("desconto");

        //var venda = new Venda(formaPagamento, codVenda, desconto, totVenda, dataVenda);

        //vendas.add(venda);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return vendas;
  }
}