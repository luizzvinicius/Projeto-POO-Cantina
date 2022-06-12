import exceptions.FuncionarioOpException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TelaCadastroFuncionario {
  private final Dados dados;
  private final JDialog dialog;
  private final TelaOpcoes dono;

  private final Container containerCampos = new Container();
  private final JButton botaoContinuar = new JButton("Continuar");
  private final JTextField campoEmail = new JTextField();
  private final JTextField campoNome = new JTextField();
  private final JTextField campoSenha = new JTextField();
  private final JLabel labelErro = new JLabel();

  public TelaCadastroFuncionario(Dados dados, JDialog dialog, TelaOpcoes dono) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Cadastrar funcionário");
    this.dono = dono;

    this.botaoContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoContinuar.addActionListener(this::handleAction);
    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.containerCampos.setLayout(new GridLayout(0, 2));
    this.containerCampos.add(new JLabel("Nome:"));
    this.containerCampos.add(this.campoNome);
    this.containerCampos.add(new JLabel("Email:"));
    this.containerCampos.add(this.campoEmail);
    this.containerCampos.add(new JLabel("Senha:"));
    this.containerCampos.add(this.campoSenha);

    var container = this.dialog.getContentPane();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.add(this.containerCampos);
    container.add(this.botaoContinuar);
    container.add(this.labelErro);

    this.dialog.setMinimumSize(new Dimension(300, container.getMinimumSize().height));
    this.dialog.pack();
    this.dialog.setVisible(true);
  }

  private void handleAction(ActionEvent event) {
    String email = this.campoEmail.getText();
    String nome = this.campoSenha.getText();
    String senha = this.campoSenha.getText();

    try {
      var funcionario = new Funcionario(nome, email, senha);
      this.dados.funcionarios.cadastrar(funcionario);
      this.dados.funcionario = funcionario;
      this.dono.atualizarBotoes();
      this.dialog.setVisible(false);
    } catch (FuncionarioOpException e) {
      this.labelErro.setText(e.getMessage());
      this.dialog.pack();
    }
  }
}