import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

public class VendaDao {
  private static final String COMANDO_CADASTRAR = "INSERT INTO Venda "
      + "(email_func, data_venda, total_venda, desconto, forma_pagamento)"
      + "VALUES (?, ?, ?, ?, ?)";

  private final Connection conexao;

  public VendaDao(Connection conexao) {
    this.conexao = conexao;
  }

  public void adicionar(Venda venda) {
    try (var stmt = this.conexao.prepareStatement(COMANDO_CADASTRAR, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, venda.getEmailFunc());
      stmt.setDate(2, Date.valueOf(venda.getDataVenda()));
      stmt.setDouble(3, venda.getTotalVenda());
      stmt.setDouble(4, venda.getDesconto());
      stmt.setString(5, venda.getFormaPagamento());
      stmt.execute();

      var result = stmt.getGeneratedKeys();
      result.next();
      venda.setCodigo(result.getInt(1));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}