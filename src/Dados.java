public class Dados {
  public final ProdutoDao estoque;
  public final FuncionarioDao funcionarios;
  public String nomeFuncionario;

  public Dados() {
    var conexao = CriadorDeConexao.criar();
    this.estoque = new ProdutoDao(conexao);
    this.funcionarios = new FuncionarioDao(conexao);
  }
}