import java.time.LocalDate;

public class Venda {
  private int codigo;
  private String formaPagamento, emailFunc;
  private double desconto, totalVenda;
  private LocalDate dataVenda;

  public Venda(String emailFunc, String formaPagamento, double desconto, double totalVenda, LocalDate dataVenda) {
    this.emailFunc = emailFunc;
    this.formaPagamento = formaPagamento;
    this.desconto = desconto;
    this.totalVenda = totalVenda;
    this.dataVenda = dataVenda;
  }

  public String getFormaPagamento() {
    return formaPagamento;
  }

  public int getCodigo() {
    return codigo;
  }

  public String getEmailFunc() {
    return emailFunc;
  }

  public double getDesconto() {
    return desconto;
  }

  public double getTotalVenda() {
    return totalVenda;
  }

  public LocalDate getDataVenda() {
    return dataVenda;
  }

  public void setCodigo(int codigo) {
    this.codigo = codigo;
  }
}