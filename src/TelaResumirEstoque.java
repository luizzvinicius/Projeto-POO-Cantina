import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class TelaResumirEstoque extends TelaBase {
  private final String[] FORMAS = new String[] { "Pelo nome", "Pela descrição", "Pela quantidade (decrescente)" };

  private final Dados dados;
  private final Container containerProdutos = new Container();
  private final JButton botaoFechar = new JButton("Fechar");
  private final JButton botaoResumir = new JButton("Resumir");
  private final JComboBox<String> seletorFormasOrdenacao = new JComboBox<>(FORMAS);

  public TelaResumirEstoque(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    super(dialog);
    this.dados = dados;
    this.dialog.setTitle("Resumir estoque");

    this.botaoFechar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoFechar.addActionListener(this::apertouBotaoFechar);
    this.botaoResumir.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoResumir.addActionListener(this::apertouBotaoResumir);

    this.container.setLayout(new GridBagLayout());
    this.containerProdutos.setLayout(new BoxLayout(this.containerProdutos, BoxLayout.Y_AXIS));
    var constraints = new GridBagConstraints();

    constraints.gridx = 0;
    constraints.gridy = 0;
    this.container.add(new JLabel("Forma de ordenação"), constraints);

    constraints.gridx++;
    constraints.weightx = 1;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    this.container.add(this.seletorFormasOrdenacao, constraints);

    constraints.gridx++;
    constraints.weightx = 0;
    constraints.fill = GridBagConstraints.NONE;
    this.container.add(this.botaoResumir, constraints);

    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy++;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    this.container.add(this.containerProdutos, constraints);

    constraints.gridy++;
    constraints.fill = GridBagConstraints.NONE;
    this.container.add(this.botaoFechar, constraints);

    this.mostrar(600);
  }

  private void apertouBotaoFechar(ActionEvent event) {
    this.dialog.setVisible(false);
  }

  private void apertouBotaoResumir(ActionEvent event) {
    var escolha = this.seletorFormasOrdenacao.getSelectedIndex();

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

    var produtos = this.dados.estoque.getProdutos(propriedade, decrescente);
    this.containerProdutos.removeAll();

    for (var produto : produtos) {
      var msg = "Nome: %s, descrição: %s, quantidade: %d, preço de compra: %.2f, preço de venda: R$ %.2f";
      var nome = produto.getNome();
      var descricao = produto.getDescricao();
      var qtdAtual = produto.getQtdAtual();
      var precoVenda = produto.getPrecoVenda();
      var precoCompra = produto.getPrecoCompra();
      this.containerProdutos.add(new JLabel(String.format(msg, nome, descricao, qtdAtual, precoVenda, precoCompra)));
    }

    this.dialog.pack();
  }
}