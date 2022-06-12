public class Dados {
  public final ProdutoDao estoque;
  public final FuncionarioDao funcionarios;
  public final CadastraDao cadastraDao;
  public final VendaDao vendas;
  public final ItemDao itens;
  public Funcionario funcionario;

  public Dados() {
    var conexao = CriadorDeConexao.criar();
    this.estoque = new ProdutoDao(conexao);
    this.funcionarios = new FuncionarioDao(conexao);
    this.cadastraDao = new CadastraDao(conexao);
    this.vendas = new VendaDao(conexao);
    this.itens = new ItemDao(conexao);
  }
}