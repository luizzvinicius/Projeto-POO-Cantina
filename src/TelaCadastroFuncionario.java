import exceptions.FuncionarioOpException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaCadastroFuncionario {
  private final JDialog dialog;
  private final TelaOpcoes dono;
  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoCadastrar;
  private final JTextField campoEmail, campoNome, campoSenha;
  private final JLabel labelErro;

  public TelaCadastroFuncionario(TelaOpcoes dono, JDialog dialog, Dados dados) {
    this.dados = dados;
    this.dialog = dialog;
    this.dialog.setTitle("Cadastrar funcionário");
    this.dono = dono;

    this.container = this.dialog.getContentPane();
    this.containerCampos = new Container();
    this.botaoCadastrar = new JButton("Cadastrar");
    this.botaoCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoCadastrar.addActionListener(this::handleAction);
    this.campoEmail = new JTextField();
    this.campoNome = new JTextField();
    this.campoSenha = new JTextField();
    this.labelErro = new JLabel();
    this.labelErro.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.containerCampos.setLayout(new GridLayout(0, 2));
    this.containerCampos.add(new JLabel("Nome:"));
    this.containerCampos.add(this.campoNome);
    this.containerCampos.add(new JLabel("Email:"));
    this.containerCampos.add(this.campoEmail);
    this.containerCampos.add(new JLabel("Senha:"));
    this.containerCampos.add(this.campoSenha);

    this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));
    this.container.add(this.containerCampos);
    this.container.add(this.botaoCadastrar);
    this.container.add(this.labelErro);

    this.dialog.setMinimumSize(new Dimension(300, this.container.getMinimumSize().height));
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