import exceptions.FuncionarioOpException;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JTextField;

public class TelaCadastrarFuncionario extends TelaFormulario {
  private final JTextField campoEmail = new JTextField();
  private final JTextField campoNome = new JTextField();
  private final JTextField campoSenha = new JTextField();

  public TelaCadastrarFuncionario(Dados dados, JDialog dialog, TelaOpcoes telaOpcoes) {
    super(dados, dialog);

    this.dialog.setTitle("Cadastrar funcionário");
    this.botaoContinuar.setText("Cadastrar");
    this.adicionarCampo("Nome:", this.campoNome);
    this.adicionarCampo("Email:", campoEmail);
    this.adicionarCampo("Senha:", campoSenha);

    this.mostrar(300);
  }

  @Override
  protected void apertouBotao(ActionEvent event) {
    String email = this.campoEmail.getText();
    String nome = this.campoSenha.getText();
    String senha = this.campoSenha.getText();

    try {
      this.dados.funcionarios.cadastrar(new Funcionario(nome, email, senha));
      this.dialog.setVisible(false);
    } catch (FuncionarioOpException e) {
      this.labelErro.setText(e.getMessage());
      this.dialog.pack();
    }
  }
}