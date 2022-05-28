import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estoque {
  private final List<Produto> estoque;

  public Estoque() {
    this.estoque = new ArrayList<>();
  }

  public void add(Produto produto) throws ProdutoInvalidoException {
    produto.validarParaAdicionar();
    var i = Collections.binarySearch(this.estoque, produto, Produto::compararPeloNome);
    this.estoque.add(i < 0 ? ~i : i, produto);
  }

  public List<Produto> getProdutos() {
    return this.getProdutosOrdenado("nome");
  }

  public List<Produto> getProdutosOrdenado(String propriedade) {
    var produtos = new ArrayList<Produto>(estoque.size());
    estoque.iterator().forEachRemaining(produtos::add);

    if ("nome".equals(propriedade)) {
      produtos.sort(Produto::compararPeloNome);
    } else if ("descrição".equals(propriedade)) {
      produtos.sort(Produto::compararPelaDescricao);
    } else if ("quantidadeDesc".equals(propriedade)) {
      produtos.sort(Produto::compararPelaQtdDecrescente);
    }

    return Collections.unmodifiableList(produtos);
  }

  public void remove(Produto produto) {
    this.estoque.remove(produto);
  }
}