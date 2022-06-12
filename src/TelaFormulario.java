import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;

public abstract class TelaFormulario extends TelaBase {
  protected final Dados dados;
  protected final JLabel labelErro = new JLabel();
  protected final JButton botaoContinuar = new JButton();

  private final Container containerCampos = new Container();
  private final GridBagConstraints constraintsLabel = new GridBagConstraints();
  private final GridBagConstraints constraintsCampo = new GridBagConstraints();

  protected TelaFormulario(Dados dados, JDialog dialog) {
    super(dialog);
    this.dados = dados;

    this.constraintsLabel.gridx = 0;
    this.constraintsLabel.fill = GridBagConstraints.HORIZONTAL;
    this.constraintsCampo.gridx = 1;
    this.constraintsCampo.weightx = 1;
    this.constraintsCampo.fill = GridBagConstraints.HORIZONTAL;

    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoContinuar.addActionListener(this::apertouBotao);

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.containerCampos.setLayout(new GridBagLayout());
    this.container.add(this.containerCampos);
    this.container.add(this.botaoContinuar);
    this.container.add(this.labelErro);
  }

  protected void adicionarCampo(String nome, JComponent campo) {
    this.containerCampos.add(new JLabel(nome), this.constraintsLabel);
    this.containerCampos.add(campo, this.constraintsCampo);
  }

  protected abstract void apertouBotao(ActionEvent event);
}
