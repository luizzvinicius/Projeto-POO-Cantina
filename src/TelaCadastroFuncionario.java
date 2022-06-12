import exceptions.FuncionarioOpException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaCadastroFuncionario extends JDialog {
  private final TelaOpcoes dono;
  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoCadastrar;
  private final JTextField campoEmail, campoNome, campoSenha;
  private final JLabel labelErro;

  public TelaCadastroFuncionario(TelaOpcoes dono, Dados dados) {
    super(dono.getFrame(), "Cadastrar funcionário", true);
    this.dono = dono;
    this.dados = dados;

    this.container = this.getContentPane();
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

    this.setMinimumSize(new Dimension(300, this.container.getMinimumSize().height));
    this.pack();
    this.setVisible(true);
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
      this.setVisible(false);
      this.dispose();
    } catch (FuncionarioOpException e) {
      this.labelErro.setText(e.getMessage());
      this.pack();
    }
  }
}