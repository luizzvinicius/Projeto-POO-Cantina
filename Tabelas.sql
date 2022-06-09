CREATE DATABASE IF NOT EXISTS cantina;
USE cantina;

CREATE TABLE IF NOT EXISTS Funcionario (
    email varchar(30),
    nome varchar(30),
    senha varchar(30),
    PRIMARY KEY (email)
);
CREATE TABLE IF NOT EXISTS Produto (
    codigo int(8) AUTO_INCREMENT,
    nome varchar(30),
    descricao varchar(30),
    preco_venda decimal(10, 2),
    preco_compra decimal(10, 2),
    qtd_atual int(8),
    qtd_vendida int(8),
    qtd_comprada int(8),
    estoque_minimo int(8),
    PRIMARY KEY (codigo)
);
CREATE TABLE IF NOT EXISTS Cadastra (
    email_func varchar(30),
    cod_produto int(8),
    PRIMARY KEY (email_func, cod_produto),
    FOREIGN KEY (email_func) REFERENCES Funcionario (email),
    FOREIGN KEY (cod_produto) REFERENCES Produto (codigo)
);
CREATE TABLE IF NOT EXISTS Venda (
    codigo int(8),
    data_venda date,
    email_func varchar(30),
    forma_pagamento varchar(30),
    total_venda decimal(10, 2),
    desconto decimal(10, 2),
    PRIMARY KEY (codigo),
    FOREIGN KEY (email_func) REFERENCES Funcionario (email)
);
CREATE TABLE IF NOT EXISTS Item (
    codigo int(8),
    cod_venda int(8),
    cod_produto int(8),
    qtdidade int(8),
    preco decimal(10, 2),
    PRIMARY KEY (codigo),
    FOREIGN KEY (cod_venda) REFERENCES Venda (codigo),
    FOREIGN KEY (cod_produto) REFERENCES Produto (codigo)
);