import java.util.ArrayList;
import java.util.List;
import java.io.PrintStream;

import exceptions.ProdutoInvalidoException;
import exceptions.VendaInvalidaException;

public class Main {
  public static final String[] DESCRICOES_OPCOES = new String[] {
      "Cadastrar produto", "Vender produto", "Adicionar quantidade ao produto",
      "Remover produto", "Resumir estoque", "Ver produtos em falta", "Mostrar lucro/prejuízo",
      "Sair do programa"
  };

  public static final Opcao[] FUNCS_OPCOES = new Opcao[] {
      Main::cadastrarProduto, Main::venderProduto, Main::adicionarQtd, Main::removerProduto,
      Main::resumirEstoque, Main::verProdutosEmFalta, Main::mostrarLucroPrejuizo, Main::sairDoPrograma
  };

  private static final String[] DESCRICOES_OPCOES_FUNCIONARIOS = new String[] {
      "Entrar", "Cadastrar", "Sair do programa"
  };

  private static final Opcao[] FUNCS_OPCOES_FUNCIONARIOS = new Opcao[] {
      Main::entrar, Main::cadastrar, Main::sairDoPrograma
  };

  private final Entrada entrada;
  private final PrintStream out;
  private final ProdutoDao estoque;
  private final FuncionarioDao funcionarios;
  private String nomeFuncionario;

  public static void main(String[] args) {
    try (var entrada = new Entrada()) {
      new Tela();
      //new Main(entrada, this.out).rodar();
    }
  }

  public Main(Entrada entrada, PrintStream out) {
    this.entrada = entrada;
    this.out = out;
    var conexao = CriadorDeConexao.criar();
    this.estoque = new ProdutoDao(conexao);
    this.funcionarios = new FuncionarioDao(conexao);
  }

  private void rodar() {
    this.out.println("Bem-vindo a Cantina do IFAL!");

    while (this.nomeFuncionario == null) {
      rodarUmaOpcao(DESCRICOES_OPCOES_FUNCIONARIOS, FUNCS_OPCOES_FUNCIONARIOS);
    }

    this.out.printf("\nLogado como: %s\n", this.nomeFuncionario);

    while (true) {
      rodarUmaOpcao(DESCRICOES_OPCOES, FUNCS_OPCOES);
    }
  }

  private void rodarUmaOpcao(String[] descricoes, Opcao[] funcoes) {
    this.out.println("== Opções disponíveis:");

    var qtdOpcoes = descricoes.length;
    for (var i = 0; i < qtdOpcoes; i++) {
      this.out.printf("%d. %s\n", i + 1, descricoes[i]);
    }

    var opcao = entrada.lerIndice("Escolha uma: ", qtdOpcoes);
    var func = funcoes[opcao];

    this.out.println();
    func.accept(this);
    this.out.println();
    entrada.lerEnter("Aperte Enter para continuar...");
  }

  private boolean checarQtd(List<Produto> produtos) {
    if (produtos.size() > 0) {
      return true;
    } else {
      this.out.println("Não há produtos disponíveis no estoque!");
      return false;
    }
  }

  private void imprimirProdutosI(List<Produto> produtos) {
    this.out.println("== Produtos:");

    for (var i = 0; i < produtos.size(); i++) {
      this.out.printf("%d. ", i + 1);
      imprimirProduto(produtos.get(i));
    }
  }

  private void imprimirProduto(Produto produto) {
    var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d\n";
    var nome = produto.getNome();
    var descricao = produto.getDescricao();
    var qtdAtual = produto.getQtdAtual();
    var precoVenda = produto.getPrecoVenda();
    this.out.printf(msg, nome, descricao, precoVenda, qtdAtual);
  }

  private void cadastrar() {
    var nome = this.entrada.lerString("Digite o nome do usuário: ");
    var email = this.entrada.lerString("Digite o email do usuário: ");
    var senha = this.entrada.lerString("Digite a senha: ");
    var funcionario = new Funcionario(nome, email, senha);

    try {
      this.funcionarios.cadastrar(funcionario);
    } catch (FuncionarioJaCadastradoException e) {
      this.out.println("Esse email já está cadastrado");
    }
  }

  private void entrar() {
    var email = this.entrada.lerString("Digite o email do funcionário: ");
    var senha = this.entrada.lerString("Digite a senha: ");
    var nome = this.funcionarios.login(email, senha);

    if (nome != null) {
      this.nomeFuncionario = nome;
    } else {
      this.out.println("Email ou senha inexistentes ou incorretos");
    }
  }

  private void cadastrarProduto() {
    var nome = entrada.lerString("Digite o nome do produto: ");
    var descricao = entrada.lerString("Digite a descrição do produto: ");
    var precoCompra = entrada.lerDoubleValidar("Digite o preço de compra do produto: ");

    double precoVenda, precoVendaMin = precoCompra * 1.1;
    while ((precoVenda = entrada.lerDoubleValidar("Digite o preço de venda do produto: ")) < precoVendaMin) {
      this.out.println("O preço da venda precisa ser maior que o preço da compra!");
    }

    var qtdComprada = entrada.lerInt("Digite a quantidade comprada do produto: ");
    var estoqueMinimo = entrada.lerInt("Digite o estoque mínimo do produto: ");
    var produto = new Produto(nome, descricao, precoVenda, precoCompra, qtdComprada, estoqueMinimo);
    this.out.println();

    try {
      estoque.adicionar(produto);
      this.out.println("O produto foi cadastrado com sucesso");
    } catch (ProdutoInvalidoException e) {
      this.out.printf("Não foi possível cadastrar o produto: %s!\n", e.getMessage());
    }
  }

  private void venderProduto() {
    var produtos = new ArrayList<Produto>();

    for (var produto : this.estoque.getProdutos()) {
      if (produto.getQtdAtual() > 0) {
        produtos.add(produto);
      }
    }

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
        this.out.println("O produto está sem itens disponíveis!");
      }
    }

    var msg = "Qual a quantidade a ser vendida? ";
    var qtd = entrada.lerIntValidar(msg, 1, produto.getQtdComprada());

    try {
      var pagamento = formaPagamento();

      if (pagamento.equals("Dinheiro")) {
        var dinheiro = 0d;

        while (true) {
          dinheiro = entrada.lerDoubleValidar("Digite quanto vai ser pago: ");

          if (dinheiro < produto.getPrecoVenda()) {
            this.out.println("Valor inválido!");
          } else {
            break;
          }
        }

        if (dinheiro > produto.getPrecoVenda()) {
          var troco = dinheiro - produto.getPrecoVenda();
          this.out.printf("Troco de R$ %2f\n", troco);
        }
      } else {
        this.out.println("Pagamento realizado com sucesso!");
      }

      this.out.printf("\nVendendo %d %s\n", qtd, produto.getNome());
      produto.venderQtd(qtd);
      estoque.atualizar(produto);
    } catch (VendaInvalidaException e) {
      this.out.printf("Não foi possível vender o produto: %s!", e.getMessage());
    }
  }

  private String formaPagamento() {
    var pagamentos = new String[] { "Dinheiro", "Cartão de crédito", "Cartão de débito", "Pix" };

    for (var i = 0; i < pagamentos.length; i++) {
      this.out.printf("%d. %s.\n", i + 1, pagamentos[i]);
    }

    var escolha = entrada.lerIndice("Escolha uma forma de pagamento: ", pagamentos.length);
    return pagamentos[escolha];
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
    estoque.atualizar(produto);
  }

  private void removerProduto() {
    var produtos = estoque.getProdutos();

    if (!checarQtd(produtos)) {
      return;
    }

    imprimirProdutosI(produtos);
    var escolha = entrada.lerIndice("Escolha um: ", produtos.size());
    estoque.remover(produtos.get(escolha));
    this.out.println("Produto removido com sucesso");
  }

  private void resumirEstoque() {
    this.out.println("== Opcões disponíveis:");
    this.out.println("1. Ordenar pelo nome");
    this.out.println("2. Ordenar pelo descrição");
    this.out.println("3. Ordenar pela quantidade (descrescente)");
    var escolha = entrada.lerIndice("Escolha uma: ", 3);

    String propriedade = null;
    var decrescente = false;

    if (escolha == 0) {
      propriedade = "nome";
    } else if (escolha == 1) {
      propriedade = "descricao";
    } else if (escolha == 2) {
      propriedade = "qtd_atual";
      decrescente = true;
    }

    var produtos = estoque.getProdutos(propriedade, decrescente);

    if (!checarQtd(produtos)) {
      return;
    }

    this.out.println();
    produtos.forEach(produto -> imprimirProduto(produto));
  }

  private void verProdutosEmFalta() {
    var produtos = estoque.getProdutos();

    if (!checarQtd(produtos)) {
      return;
    }

    produtos.forEach(produto -> {
      if (produto.getQtdAtual() < produto.getEstoqueMinimo()) {
        imprimirProduto(produto);
      }
    });
  }

  public void mostrarLucroPrejuizo() {
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
      this.out.printf("%s deu um %s de R$ %.2f\n", produto.getNome(), msg, Math.abs(lucro));
    }

    var lucroTotal = receitaTotal - custoTotal;
    var msg = lucroTotal > 0 ? "lucro" : "prejuízo";
    this.out.printf("Houve um %s total de R$ %.2f\n", msg, Math.abs(lucroTotal));
  }

  public void sairDoPrograma() {
    this.out.println("Saindo...");
    // System.exit(0);
  }

  @FunctionalInterface
  public interface Opcao {
    void accept(Main main);
  }
}