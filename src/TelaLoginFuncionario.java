import exceptions.FuncionarioOpException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaLoginFuncionario extends JDialog implements ActionListener {
  private final TelaOpcoes dono;
  private final Dados dados;
  private final Container container, containerCampos;
  private final JButton botaoEntrar;
  private final JTextField campoEmail, campoSenha;
  private final JLabel labelErro;

  public TelaLoginFuncionario(TelaOpcoes dono, Dados dados) {
    super(dono, "Entrar como funcionário", true);
    this.dono = dono;
    this.dados = dados;

    this.container = this.getContentPane();
    this.containerCampos = new Container();
    this.botaoEntrar = new JButton("Entrar");
    this.botaoEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.botaoEntrar.addActionListener(this);
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

    this.setMinimumSize(new Dimension(300, this.container.getMinimumSize().height));
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    String email = this.campoEmail.getText();
    String senha = this.campoSenha.getText();

    try {
      var funcionario = this.dados.funcionarios.entrar(email, senha);
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