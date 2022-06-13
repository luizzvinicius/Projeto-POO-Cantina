import exceptions.FuncionarioOpException;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TelaEntrarComoFuncionario extends TelaFormulario {
  private final TelaOpcoes telaOpcoes;
  private final JTextField campoEmail = new JTextField();
  private final JPasswordField campoSenha = new JPasswordField();

  public TelaEntrarComoFuncionario(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    super(dados, dialog);
    this.telaOpcoes = telaOpcoes;

    this.dialog.setTitle("Entrar como funcionário");
    this.botaoContinuar.setText("Entrar");
    this.adicionarCampo("Email:", this.campoEmail);
    this.adicionarCampo("Senha:", this.campoSenha);

    this.mostrar(300);
  }

  @Override
  protected void apertouBotao(ActionEvent event) {
    var email = this.campoEmail.getText();
    var senha = String.valueOf(campoSenha.getPassword());

    try {
      var funcionario = this.dados.funcionarios.entrar(email, senha);
      this.dados.funcionario = funcionario;
      this.telaOpcoes.atualizarBotoes();
      this.dialog.setVisible(false);
    } catch (FuncionarioOpException e) {
      this.labelErro.setText(e.getMessage());
      this.dialog.pack();
    }
  }
}