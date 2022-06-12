import exceptions.VendaInvalidaException;

public class Produto {
  private final String nome, descricao;
  private final int estoqueMinimo;
  private final double precoVenda, precoCompra;
  private int codigo, qtdAtual, qtdVendida, qtdComprada;

  public Produto(int codigo, String nome, String descricao, double precoVenda, double precoCompra, int qtdAtual,
      int qtdVendida, int qtdComprada, int estoqueMinimo) {
    this.codigo = codigo;
    this.nome = nome;
    this.descricao = descricao;
    this.precoVenda = precoVenda;
    this.precoCompra = precoCompra;
    this.qtdAtual = qtdAtual;
    this.qtdVendida = qtdVendida;
    this.qtdComprada = qtdComprada;
    this.estoqueMinimo = estoqueMinimo;
  }

  public Produto(String nome, String descricao, double precoVenda, double precoCompra, int qtdComprada,
      int estoqueMinimo) {
    this.codigo = -1;
    this.nome = nome;
    this.descricao = descricao;
    this.precoVenda = precoVenda;
    this.precoCompra = precoCompra;
    this.qtdAtual = qtdComprada;
    this.qtdComprada = qtdComprada;
    this.estoqueMinimo = estoqueMinimo;
  }

  public int getCodigo() {
    return this.codigo;
  }

  public String getNome() {
    return this.nome;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public double getPrecoVenda() {
    return this.precoVenda;
  }

  public double getPrecoCompra() {
    return this.precoCompra;
  }

  public int getQtdAtual() {
    return this.qtdAtual;
  }

  public int getQtdVendida() {
    return this.qtdVendida;
  }

  public int getQtdComprada() {
    return this.qtdComprada;
  }

  public int getEstoqueMinimo() {
    return this.estoqueMinimo;
  }

  public void setCodigo(int codigo) {
    this.codigo = codigo;
  }

  public void adicionarQtd(int qtd) {
    this.qtdAtual += qtd;
    this.qtdComprada += qtd;
  }

  public void venderQtd(int qtd) throws VendaInvalidaException {
    if (qtd > qtdAtual) {
      throw new VendaInvalidaException("Sem itens disponíveis");
    }

    this.qtdAtual -= qtd;
    this.qtdVendida += qtd;
  }
}