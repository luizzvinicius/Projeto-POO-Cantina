import java.time.LocalDate;

public class Venda {
  private String formaPagamento, emailFunc;
  private int codVenda;
  private double desconto, totVenda;
  private LocalDate dataVenda;

  public Venda(String emailFunc, String formaPagamento, double desconto, double totVenda, LocalDate dataVenda) {
    this.emailFunc = emailFunc;
    this.formaPagamento = formaPagamento;
    this.desconto = desconto;
    this.totVenda = totVenda;
    this.dataVenda = dataVenda;
  }

  public String getFormaPagamento() {
    return formaPagamento;
  }

  public int getCodVenda() {
    return codVenda;
  }

  public String getEmailFunc() {
    return emailFunc;
  }

  public double getDesconto() {
    return desconto;
  }

  public double getTotVenda() {
    return totVenda;
  }

  public LocalDate getDataVenda() {
    return dataVenda;
  }

  public void setCodVenda(int codVenda) {
    this.codVenda = codVenda;
  }

  public void setDesconto(double desconto) {
    this.desconto = desconto;
  }

  public void setTotVenda(double totVenda) {
    this.totVenda = totVenda;
  }

  public void setDataVenda(LocalDate dataVenda) {
    this.dataVenda = dataVenda;
  }
}
