import java.util.List;
import java.util.function.Consumer;

public class Main {
  /**
   * Adicionar:
   * Classe funcionário pra fazer login
   * Funcionário que adiciona produto no estoque
   * Melhorar a função venda
   * 
   * Alterações no modelo:
   * Nome do produto
   * quantidade atual
   * 
   * alterações no código:
   * Adicionar ID do produto, get
   */

  private static final String[] DESCRICOES_OPCOES = new String[] {
      "Cadastrar produto", "Vender produto", "Adicionar quantidade ao produto", "Remover produto",
      "Resumir estoque", "Ver produtos em falta", "Mostrar lucro/prejuízo", "Sair do programa"
  };

  private static final List<Consumer<Main>> FUNCS_OPCOES = List.of(
      Main::cadastrarProduto, Main::venderProduto, Main::adicionarQtd, Main::removerProduto,
      Main::resumirEstoque, Main::verProdutosEmFalta, Main::mostrarLucroPrejuizo, Main::sairDoPrograma);

  private final Estoque estoque;
  private final Entrada entrada;

  public static void main(String[] args) {
    try (var entrada = new Entrada()) {
      new Main(entrada).rodar();
    }
  }

  private Main(Entrada entrada) {
    this.estoque = new Estoque();
    this.entrada = entrada;
  }

  private void rodar() {
    System.out.println("Iniciando...");

    while (true) {
      System.out.println();
      System.out.println("== Opções disponíveis:");

      var qtdOpcoes = DESCRICOES_OPCOES.length;
      for (var i = 0; i < qtdOpcoes; i++) {
        System.out.printf("%d. %s\n", i + 1, DESCRICOES_OPCOES[i]);
      }

      var opcao = entrada.lerIndice("Escolha uma: ", qtdOpcoes);
      var func = FUNCS_OPCOES.get(opcao);

      System.out.println();
      func.accept(this);
      entrada.lerEnter("Aperte Enter para continuar...");
    }
  }

  private boolean checarQtd(List<Produto> produtos) {
    if (produtos.size() > 0) {
      return true;
    } else {
      System.out.println("Não há produtos cadastrados no estoque!");
      return false;
    }
  }

  private void imprimirProdutosI(List<Produto> produtos) {
    System.out.println("== Produtos:");

    for (var i = 0; i < produtos.size(); i++) {
      System.out.printf("%d. ", i + 1);
      imprimirProduto(produtos.get(i));
    }
  }

  private void imprimirProduto(Produto produto) {
    var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d\n";
    var nome = produto.getNome();
    var descricao = produto.getDescricao();
    var qtdAtual = produto.getQtdAtual();
    var precoVenda = produto.getPrecoVenda();
    System.out.printf(msg, nome, descricao, precoVenda, qtdAtual);
  }

  private void cadastrarProduto() {
    var nome = entrada.lerString("Digite o nome do produto: ");
    var descricao = entrada.lerString("Digite a descrição do produto: ");
    var precoCompra = entrada.lerDoubleValidar("Digite o preço de compra do produto: ");
    var estoqueMinimo = entrada.lerInt("Digite o estoque mínimo do produto: ");

    double precoVenda, precoVendaMin = precoCompra * 1.1;
    while ((precoVenda = entrada.lerDoubleValidar("Digite o preço de venda do produto: ")) < precoVendaMin) {
      System.out.println("O preço da venda precisa ser maior que o preço da compra!");
    }

    var qtdComprada = entrada.lerInt("Digite a quantidade comprada do produto: ");
    var produto = new Produto(nome, descricao, precoVenda, precoCompra, qtdComprada, estoqueMinimo);
    System.out.println();

    try {
      estoque.add(produto);
      System.out.println("O produto foi cadastrado com sucesso");
    } catch (ProdutoInvalidoException e) {
      System.out.println("Não foi possível cadastrar o produto: " + e.getMessage() + "!");
    }
  }

  private void venderProduto() {
    var produtos = estoque.getProdutos();

    if (!checarQtd(produtos)) {
      return;
    }

    imprimirProdutosI(produtos);
    Produto produto;

    while (true) {
      var escolha = entrada.lerIndice("Escolha um: ", produtos.size());
      produto = produtos.get(escolha);

      if (produto.getQtdAtual() > 0) {
        break;
      } else {
        System.out.println("O produto está sem itens disponíveis!");
      }
    }

    var msg = "Qual a quantidade a ser vendida? ";
    var qtd = entrada.lerIntValidar(msg, 1, produto.getQtdComprada());

    System.out.println();
    System.out.printf("Vendendo %d %s\n", qtd, produto.getNome());

    try {
      produto.venderQtd(qtd);
    } catch (VendaInvalidaException e) {
      System.out.println("Não foi possível vender o produto: " + e.getMessage() + "!");
    }
  }

  private void adicionarQtd() {
    var produtos = estoque.getProdutos();

    if (!checarQtd(produtos)) {
      return;
    }

    imprimirProdutosI(produtos);
    var escolha = entrada.lerIndice("Escolha um: ", produtos.size());
    var produto = produtos.get(escolha);

    var msg = "Qual a quantidade a ser adicionada? ";
    var qtd = entrada.lerInt(msg);
    produto.adicionarQtd(qtd);
  }

  private void removerProduto() {
    var produtos = estoque.getProdutos();

    if (!checarQtd(produtos)) {
      return;
    }

    imprimirProdutosI(produtos);
    var escolha = entrada.lerIndice("Escolha um: ", produtos.size());
    estoque.remove(produtos.get(escolha));
  }

  private void resumirEstoque() {
    System.out.println("== Opcões disponíveis:");
    System.out.println("1. Ordenar pelo nome");
    System.out.println("2. Ordenar pelo descrição");
    System.out.println("3. Ordenar pela quantidade (descrescente)");
    var escolha = entrada.lerIndice("Escolha uma: ", 3);

    var propriedade = escolha == 1 ? "descrição" : "quantidadeDesc";
    var produtos = estoque.getProdutosOrdenado(propriedade);

    if (!checarQtd(produtos)) {
      return;
    }

    System.out.println();
    for (var produto : produtos) {
      imprimirProduto(produto);
    }
  }

  private void verProdutosEmFalta() {
    var produtos = estoque.getProdutos();

    if (!checarQtd(produtos)) {
      return;
    }

    for (var produto : produtos) {
      if (produto.getQtdAtual() < produto.getEstoqueMinimo()) {
        imprimirProduto(produto);
      }
    }
  }

  private void mostrarLucroPrejuizo() {
    var produtos = estoque.getProdutos();

    if (!checarQtd(produtos)) {
      return;
    }

    var receitaTotal = 0d;
    var custoTotal = 0d;

    for (var produto : estoque.getProdutos()) {
      var receita = produto.getQtdVendida() * produto.getPrecoVenda();
      var custo = produto.getQtdComprada() * produto.getPrecoCompra();
      custoTotal += custo;
      receitaTotal += receita;

      var lucro = receita - custo;
      var msg = lucro > 0 ? "lucro" : "prejuízo";
      System.out.printf("%s deu um %s de R$ %.2f\n", produto.getNome(), msg, Math.abs(lucro));
    }

    var lucroTotal = receitaTotal - custoTotal;
    var msg = lucroTotal > 0 ? "lucro" : "prejuízo";
    System.out.printf("Houve um %s total de R$ %.2f\n", msg, Math.abs(lucroTotal));
  }

  private void sairDoPrograma() {
    System.out.println("Saindo...");
    System.exit(0);
  }
}