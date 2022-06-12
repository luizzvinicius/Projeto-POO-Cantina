import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

public abstract class TelaInformacoesProdutos extends TelaBase {
  protected final Container containerProdutos = new Container();
  private final JButton botaoFechar = new JButton("Fechar");

  protected TelaInformacoesProdutos(JDialog dialog) {
    super(dialog);

    this.botaoFechar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoFechar.addActionListener(this::apertouBotao);

    this.container.setLayout(new GridBagLayout());
    this.containerProdutos.setLayout(new BoxLayout(this.containerProdutos, BoxLayout.Y_AXIS));
    var constraints = new GridBagConstraints();

    constraints.gridy = 0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    this.container.add(this.containerProdutos, constraints);

    constraints.gridy++;
    constraints.fill = GridBagConstraints.NONE;
    this.container.add(this.botaoFechar, constraints);
  }

  private void apertouBotao(ActionEvent event) {
    this.dialog.setVisible(false);
  }
}