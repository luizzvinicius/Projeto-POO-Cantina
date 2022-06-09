CREATE DATABASE IF NOT EXISTS cantina_flm;
USE cantina_flm;
CREATE TABLE IF NOT EXISTS Funcionario (
  email VARCHAR(30),
  nome VARCHAR(30),
  senha VARCHAR(30),
  PRIMARY KEY (email)
);
CREATE TABLE IF NOT EXISTS Produto (
  codigo INT(8) AUTO_INCREMENT,
  nome VARCHAR(30),
  descricao VARCHAR(30),
  preco_venda DECIMAL(10, 2),
  preco_compra DECIMAL(10, 2),
  qtd_atual INT(8),
  qtd_vendida INT(8),
  qtd_comprada INT(8),
  estoque_minimo INT(8),
  PRIMARY KEY (codigo)
);
CREATE TABLE IF NOT EXISTS Cadastra (
  email_func VARCHAR(30),
  cod_produto INT(8),
  PRIMARY KEY (email_func, cod_produto),
  FOREIGN KEY (email_func) REFERENCES Funcionario (email),
  FOREIGN KEY (cod_produto) REFERENCES Produto (codigo)
);
CREATE TABLE IF NOT EXISTS Venda (
  codigo INT(8),
  data_venda date,
  email_func VARCHAR(30),
  forma_pagamento VARCHAR(30),
  total_venda DECIMAL(10, 2),
  desconto DECIMAL(10, 2),
  PRIMARY KEY (codigo),
  FOREIGN KEY (email_func) REFERENCES Funcionario (email)
);
CREATE TABLE IF NOT EXISTS Item (
  codigo INT(8),
  cod_venda INT(8),
  cod_produto INT(8),
  qtdidade INT(8),
  preco DECIMAL(10, 2),
  PRIMARY KEY (codigo),
  FOREIGN KEY (cod_venda) REFERENCES Venda (codigo),
  FOREIGN KEY (cod_produto) REFERENCES Produto (codigo)
);
