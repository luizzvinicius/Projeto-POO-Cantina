public class Funcionario {
  private final String nome, email, senha;

  public Funcionario(String nome, String email, String senha) {
    this.nome = nome;
    this.email = email;
    this.senha = senha;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public String getSenha() {
    return senha;
  }
}