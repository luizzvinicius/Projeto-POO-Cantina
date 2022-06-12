import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaAdicionarQtdProduto extends JDialog implements ActionListener, ListCellRenderer<Produto> {
  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoContinuar;
  private final JTextField campoQtd;
  private final JComboBox<Produto> seletorProduto;
  private final JLabel labelErro;

  public TelaAdicionarQtdProduto(TelaOpcoes dono, Dados dados) {
    super(dono, "Adicionar quantidade ao produto", true);
    this.dados = dados;

    this.container = this.getContentPane();
    this.containerCampos = new Container();
    this.botaoContinuar = new JButton("Continuar");
    this.botaoContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoContinuar.addActionListener(this);
    this.campoQtd = new JTextField();
    this.seletorProduto = new JComboBox<>();
    this.seletorProduto.setRenderer(this);
    this.labelErro = new JLabel();
    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);

    for (var produto : this.dados.estoque.getProdutos()) {
      this.seletorProduto.addItem(produto);
    }

    this.containerCampos.setLayout(new GridLayout(0, 2));
    this.containerCampos.add(new JLabel("Produto:"));
    this.containerCampos.add(this.seletorProduto);
    this.containerCampos.add(new JLabel("Quantidade a adicionar:"));
    this.containerCampos.add(this.campoQtd);

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.containerCampos);
    this.container.add(this.botaoContinuar);
    this.container.add(this.labelErro);

    this.setMinimumSize(new Dimension(600, this.container.getMinimumSize().height));
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    var produto = (Produto) this.seletorProduto.getSelectedItem();
    var qtd = this.campoQtd.getText();

    try {
      var qtdC = Integer.parseUnsignedInt(qtd);
      produto.adicionarQtd(qtdC);
      this.dados.estoque.atualizar(produto);
      this.setVisible(false);
      this.dispose();
    } catch (NumberFormatException e) {
      this.labelErro.setText(e.getMessage());
      this.pack();
    }
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends Produto> list, Produto value, int index,
      boolean isSelected, boolean cellHasFocus) {
    var msg = "%s, descrição: %s, preço de venda: R$ %.2f, quantidade: %d";
    var nome = value.getNome();
    var descricao = value.getDescricao();
    var qtdAtual = value.getQtdAtual();
    var precoVenda = value.getPrecoVenda();
    return new JLabel(String.format(msg, nome, descricao, precoVenda, qtdAtual));
  }
}