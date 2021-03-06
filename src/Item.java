public class Item {
  private final int codVenda, codProduto, qtd;
  private final double preco;
  private int codigo;

  public Item(int codVenda, int codProduto, int qtd, double preco) {
    this.codVenda = codVenda;
    this.codProduto = codProduto;
    this.qtd = qtd;
    this.preco = preco;
  }

  public int getCodigo() {
    return codigo;
  }

  public void setCodigo(int codigo) {
    this.codigo = codigo;
  }

  public int getCodVenda() {
    return codVenda;
  }

  public int getCodProduto() {
    return codProduto;
  }

  public int getQtd() {
    return qtd;
  }

  public double getPreco() {
    return preco;
  }
}