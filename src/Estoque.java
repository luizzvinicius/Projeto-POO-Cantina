import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import exceptions.ProdutoInvalidoException;

public class Estoque implements Iterable<Produto> {
  private final List<Produto> estoque;
  private final List<Produto> estoqueImodificavel;

  public Estoque() {
    this.estoque = new ArrayList<>();
    this.estoqueImodificavel = Collections.unmodifiableList(this.estoque);
  }

  public void add(Produto produto) throws ProdutoInvalidoException {
    produto.validar();
    var i = Collections.binarySearch(this.estoque, produto, Produto::compararPeloNome);
    this.estoque.add(i < 0 ? ~i : i, produto);
  }

  public Produto get(int i) {
    return this.estoque.get(i);
  }

  public void remove(int i) {
    this.estoque.remove(i);
  }

  public int size() {
    return this.estoque.size();
  }

  @Override
  public Iterator<Produto> iterator() {
    return this.estoqueImodificavel.iterator();
  }
}