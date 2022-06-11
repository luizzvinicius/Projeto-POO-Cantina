import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.sql.SQLException;

public class VendaDao {
  private static final String COMANDO_CADASTRAR = "INSERT INTO Venda "
      + "(data_venda, email_func, forma_pagamento, total_venda, desconto)"
      + "VALUES (?, ?, ?, ?, ?)";

  private final Connection conexao;

  public VendaDao(Connection conexao) {
    this.conexao = conexao;
  }

  public int adicionar(Venda venda) {
    try {
      var stmt = this.conexao.prepareStatement(COMANDO_CADASTRAR, Statement.RETURN_GENERATED_KEYS);
      stmt.setDate(1, Date.valueOf(venda.getDataVenda()));
      stmt.setString(2, venda.getEmailFunc());
      stmt.setString(3, venda.getFormaPagamento());
      stmt.setDouble(4, venda.getTotVenda());
      stmt.setDouble(5, venda.getDesconto());
      stmt.execute();

      var rs = stmt.getGeneratedKeys();
      rs.next();
      return rs.getInt(1);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}