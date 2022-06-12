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

public class TelaLoginFuncionario {
  private final JDialog dialog;
  private final TelaOpcoes dono;
  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoEntrar;
  private final JTextField campoEmail, campoSenha;
  private final JLabel labelErro;

  public TelaLoginFuncionario(TelaOpcoes dono, JDialog dialog, Dados dados) {
    this.dados = dados;
    this.dono = dono;
    this.dialog = dialog;
    this.dialog.setTitle("Entrar como funcionário");

    this.container = this.dialog.getContentPane();
    this.containerCampos = new Container();
    this.botaoEntrar = new JButton("Entrar");
    this.botaoEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoEntrar.addActionListener(this::handleAction);
    this.campoEmail = new JTextField();
    this.campoSenha = new JTextField();
    this.labelErro = new JLabel();
    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.containerCampos.setLayout(new GridLayout(0, 2));
    this.containerCampos.add(new JLabel("Email:"));
    this.containerCampos.add(this.campoEmail);
    this.containerCampos.add(new JLabel("Senha:"));
    this.containerCampos.add(this.campoSenha);

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.containerCampos);
    this.container.add(this.botaoEntrar);
    this.container.add(this.labelErro);

    this.dialog.setMinimumSize(new Dimension(300, this.container.getMinimumSize().height));
    this.dialog.pack();
    this.dialog.setVisible(true);
  }

  private void handleAction(ActionEvent event) {
    String email = this.campoEmail.getText();
    String senha = this.campoSenha.getText();

    try {
      var funcionario = this.dados.funcionarios.entrar(email, senha);
      this.dados.funcionario = funcionario;
      this.dono.atualizarBotoes();
      this.dialog.setVisible(false);
    } catch (FuncionarioOpException e) {
      this.labelErro.setText(e.getMessage());
      this.dialog.pack();
    }
  }
}