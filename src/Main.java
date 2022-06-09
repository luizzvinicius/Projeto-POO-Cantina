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
      "Sair da conta"
  };

  public static final Opcao[] FUNCOES_OPCOES = new Opcao[] {
      Main::cadastrarProduto, Main::venderProduto, Main::adicionarQtd, Main::removerProduto,
      Main::resumirEstoque, Main::verProdutosEmFalta, Main::mostrarLucroPrejuizo, Main::sairDaConta
  };

  private final Entrada entrada;
  private final Dados dados;
  private final ProdutoDao estoque;
  private TelaOpcoes tela;

  public Main(Entrada entrada, Dados dados, TelaOpcoes tela) {
    this.entrada = entrada;
    this.tela = tela;
    this.dados = dados;
    this.estoque = dados.estoque;
  }

  private boolean checarQtd(List<Produto> produtos) {
    if (produtos.size() > 0) {
      return true;
    } else {
      this.mostrarAviso("Não há produtos disponíveis no estoque!");
      return false;
    }
  }

  private String[] toStringProdutos(List<Produto> produtos) {
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

    var escolha = this.escolherOpcoesDinamico("Vender produto", "Escolha um", toStringProdutos(produtos));
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

    var escolha = this.escolherOpcoesDinamico("Remover produto", "Escolha um", toStringProdutos(produtos));
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

    var escolha = this.escolherOpcoesDinamico("Remover produto", "Escolha um", toStringProdutos(produtos));
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

    this.mostrarMensagem(String.join("\n", toStringProdutos(produtos)));
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

  public void sairDaConta() {
    this.dados.nomeFuncionario = null;
    this.tela.atualizarBotoes();
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
    JOptionPane.showMessageDialog(this.tela, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
  }

  private void mostrarErro(String msg) {
    JOptionPane.showMessageDialog(this.tela, msg, "Erro", JOptionPane.ERROR_MESSAGE);
  }

  @FunctionalInterface
  public interface Opcao {
    void accept(Main main);
  }
}