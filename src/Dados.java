public class Dados {
  public final ProdutoDao estoque;
  public final FuncionarioDao funcionarios;
  public final CadastraDao cadastraDao;
  public final VendaDao vendaDao;
  public final ItemDao itemDao;
  public Funcionario funcionario;

  public Dados() {
    var conexao = CriadorDeConexao.criar();
    this.estoque = new ProdutoDao(conexao);
    this.funcionarios = new FuncionarioDao(conexao);
    this.cadastraDao = new CadastraDao(conexao);
    this.vendaDao = new VendaDao(conexao);
    this.itemDao = new ItemDao(conexao);
  }
}