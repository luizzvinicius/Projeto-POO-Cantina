import exceptions.ProdutoInvalidoException;
import exceptions.VendaInvalidaException;

public class Produto {
  private int codigo;
  private String nome, descricao;
  private double precoVenda, precoCompra;
  private int qtdAtual, qtdVendida, qtdComprada, estoqueMinimo;

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
    this.nome = nome;
    this.descricao = descricao;
    this.precoVenda = precoVenda;
    this.precoCompra = precoCompra;
    this.qtdAtual = qtdComprada;
    this.qtdComprada = qtdComprada;
    this.estoqueMinimo = estoqueMinimo;
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

  public void validarParaAdicionar() throws ProdutoInvalidoException {
    if (this.nome.isBlank()) {
      throw new ProdutoInvalidoException("Nome não pode estar vazio");
    } else if (this.descricao.isBlank()) {
      throw new ProdutoInvalidoException("Descrição não pode estar vazia");
    } else if (this.precoCompra <= 0) {
      throw new ProdutoInvalidoException("Preço de compra deve ser maior que zero");
    } else if (this.precoVenda < this.precoCompra * 1.1) {
      throw new ProdutoInvalidoException("Preço de venda não pode ser menor que preço de compra");
    } else if (this.qtdComprada <= 0) {
      throw new ProdutoInvalidoException("Quantidade inicial deve ser maior que zero");
    }
  }

  public static int compararPeloNome(Produto p1, Produto p2) {
    return p1.getNome().compareToIgnoreCase(p2.getNome());
  }

  public static int compararPelaDescricao(Produto p1, Produto p2) {
    return p1.getDescricao().compareToIgnoreCase(p2.getDescricao());
  }

  public static int compararPelaQtdDecrescente(Produto p1, Produto p2) {
    return p2.getQtdAtual() - p1.getQtdAtual();
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
}
