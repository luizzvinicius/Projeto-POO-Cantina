import exceptions.ProdutoInvalidoException;
import exceptions.VendaInvalidaException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

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
  private final ProdutoDao estoque;
  private final FuncionarioDao funcionarios;
  private String nomeFuncionario;
  private Tela tela;

  public static void main(String[] args) {
    new Main(new Entrada(null), null).rodar();
  }

  public Main(Entrada entrada, Tela tela) {
    this.entrada = entrada;
    this.tela = tela;
    var conexao = CriadorDeConexao.criar();
    this.estoque = new ProdutoDao(conexao);
    this.funcionarios = new FuncionarioDao(conexao);
  }

  public void rodar() {
    this.mostrarMensagem("Iniciando...");

    while (this.nomeFuncionario == null) {
      rodarUmaOpcao(DESCRICOES_OPCOES_FUNCIONARIOS, FUNCS_OPCOES_FUNCIONARIOS);
    }

    this.mostrarMensagem("Logado como: " + this.nomeFuncionario);

    while (true) {
      rodarUmaOpcao(DESCRICOES_OPCOES, FUNCS_OPCOES);
    }
  }

  private void rodarUmaOpcao(String[] descricoes, Opcao[] funcoes) {
    var opcao = this.escolherOpcoesDinamico("Opções", "Escolha uma", descricoes);
    funcoes[opcao].accept(this);
  }

  private boolean checarQtd(List<Produto> produtos) {
    if (produtos.size() > 0) {
      return true;
    } else {
      this.mostrarAviso("Não há produtos disponíveis no estoque!");
      return false;
    }
  }

  private String[] toStringProdutosI(List<Produto> produtos) {
    var produtosStrings = new String[produtos.size()];

    for (var i = 0; i < produtosStrings.length; i++) {
      produtosStrings[i] = toStringProduto(produtos.get(i));
    }

    return produtosStrings;
  }

  private String toStringProduto(Produto produto) {
    var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d";
    var nome = produto.getNome();
    var descricao = produto.getDescricao();
    var qtdAtual = produto.getQtdAtual();
    var precoVenda = produto.getPrecoVenda();
    return String.format(msg, nome, descricao, precoVenda, qtdAtual);
  }

  private void cadastrar() {
    var nome = this.entrada.lerString("Digite o nome do usuário: ");
    var email = this.entrada.lerString("Digite o email do usuário: ");
    var senha = this.entrada.lerString("Digite a senha: ");
    var funcionario = new Funcionario(nome, email, senha);

    try {
      this.funcionarios.cadastrar(funcionario);
    } catch (FuncionarioJaCadastradoException e) {
      this.mostrarErro("Esse email já está cadastrado");
    }
  }

  private void entrar() {
    var email = this.entrada.lerString("Digite o email do funcionário: ");
    var senha = this.entrada.lerString("Digite a senha: ");
    var nome = this.funcionarios.login(email, senha);

    if (nome != null) {
      this.nomeFuncionario = nome;
    } else {
      this.mostrarErro("Email ou senha inexistentes ou incorretos");
    }
  }

  private void cadastrarProduto() {
    var nome = entrada.lerString("Digite o nome do produto: ");
    var descricao = entrada.lerString("Digite a descrição do produto: ");
    var precoCompra = entrada.lerDoubleValidar("Digite o preço de compra do produto: ");

    double precoVenda, precoVendaMin = precoCompra * 1.1;
    while ((precoVenda = entrada.lerDoubleValidar("Digite o preço de venda do produto: ")) < precoVendaMin) {
      this.mostrarErro("O preço da venda precisa ser maior que o preço da compra!");
    }

    var qtdComprada = entrada.lerInt("Digite a quantidade comprada do produto: ");
    var estoqueMinimo = entrada.lerInt("Digite o estoque mínimo do produto: ");
    var produto = new Produto(nome, descricao, precoVenda, precoCompra, qtdComprada, estoqueMinimo);

    try {
      estoque.adicionar(produto);
      this.mostrarMensagem("O produto foi cadastrado com sucesso");
    } catch (ProdutoInvalidoException e) {
      this.mostrarErro("Não foi possível cadastrar o produto: " + e.getMessage());
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

    Produto produto;

    var escolha = this.escolherOpcoesDinamico("Vender produto", "Escolha um", toStringProdutosI(produtos));
    produto = produtos.get(escolha);
    var msg = "Qual a quantidade a ser vendida? ";
    var qtd = entrada.lerIntValidar(msg, 1, produto.getQtdComprada());

    try {
      var pagamento = formaPagamento();

      if (pagamento.equals("Dinheiro")) {
        var dinheiro = 0d;

        while (true) {
          dinheiro = entrada.lerDoubleValidar("Digite quanto vai ser pago: ");

          if (dinheiro < produto.getPrecoVenda()) {
            this.mostrarErro("Valor inválido!");
          } else {
            break;
          }
        }

        if (dinheiro > produto.getPrecoVenda()) {
          var troco = dinheiro - produto.getPrecoVenda();
          this.mostrarMensagem(String.format("Troco de R$ %.2f\n", troco));
        }
      } else {
        this.mostrarMensagem("Pagamento realizado com sucesso!");
      }

      this.mostrarMensagem(String.format("Vendendo %d %s", qtd, produto.getNome()));
      produto.venderQtd(qtd);
      estoque.atualizar(produto);
    } catch (VendaInvalidaException e) {
      this.mostrarErro("Não foi possível vender o produto: " + e.getMessage());
    }
  }

  private String formaPagamento() {
    var opcoes = new String[] { "Dinheiro", "Cartão de crédito", "Cartão de débito", "Pix" };
    return opcoes[this.escolherOpcoes("Vender produto", "Forma de pagamento", opcoes)];
  }

  private void adicionarQtd() {
    var produtos = estoque.getProdutos();

    if (!checarQtd(produtos)) {
      return;
    }
    
    var escolha = this.escolherOpcoesDinamico("Remover produto", "Escolha um", toStringProdutosI(produtos));
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

    var escolha = this.escolherOpcoesDinamico("Remover produto", "Escolha um", toStringProdutosI(produtos));
    estoque.remover(produtos.get(escolha));
    this.mostrarMensagem("Produto removido com sucesso");
  }

  private void resumirEstoque() {
    var opcoes = new String[] { "Pelo nome", "Pela descrição", "Pela quantidade (decrescente)" };
    var escolha = this.escolherOpcoes("Remover produto", "Ordenar por:", opcoes);

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

    this.mostrarMensagem(String.join("\n", toStringProdutosI(produtos)));
  }

  private void verProdutosEmFalta() {
    var msg = new ArrayList<String>();

    for (var produto : this.estoque.getProdutos()) {
      if (produto.getQtdAtual() > 0) {
        msg.add(toStringProduto(produto));
      }
    }

    this.mostrarMensagem(String.join("\n", msg));
  }

  public void mostrarLucroPrejuizo() {
    var produtos = estoque.getProdutos();
    var msgs = new ArrayList<String>();

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
      msgs.add(String.format("%s deu um %s de R$ %.2f\n", produto.getNome(), msg, Math.abs(lucro)));
    }

    var lucroTotal = receitaTotal - custoTotal;
    var msg = lucroTotal > 0 ? "lucro" : "prejuízo";
    msgs.add(String.format("Houve um %s total de R$ %.2f\n", msg, Math.abs(lucroTotal)));
  }

  public void sairDoPrograma() {
    this.mostrarAviso("Saindo...");
    System.exit(0);
  }

  private int escolherOpcoes(String titulo, String msg, String[] opcoes) {
    var opcao = JOptionPane.showOptionDialog(
        this.tela, titulo, msg, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, null);

    return opcao;
  }

  private int escolherOpcoesDinamico(String titulo, String msg, String[] opcoes) {
    var opcao = JOptionPane.showInputDialog(
        this.tela, msg, titulo, JOptionPane.QUESTION_MESSAGE, null, opcoes, null);

    return Arrays.asList(opcoes).indexOf(opcao);
  }

  private void mostrarMensagem(String msg) {
    JOptionPane.showMessageDialog(this.tela, msg);
  }

  private void mostrarAviso(String msg) {
    JOptionPane.showMessageDialog(this.tela, "Erro", msg, JOptionPane.WARNING_MESSAGE);
  }

  private void mostrarErro(String msg) {
    JOptionPane.showMessageDialog(this.tela, "Erro", msg, JOptionPane.ERROR_MESSAGE);
  }

  @FunctionalInterface
  public interface Opcao {
    void accept(Main main);
  }
}
