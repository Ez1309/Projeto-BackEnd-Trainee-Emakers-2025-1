CREATE TABLE IF NOT EXISTS pessoas (
    id_pessoa SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS livros (
    id_livro SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    data_lancamento DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS emprestimos (
    id_emprestimo SERIAL PRIMARY KEY,
    id_pessoa INT NOT NULL,
    id_livro INT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao DATE NOT NULL,
    
    CONSTRAINT fk_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoas(id_pessoa),
    CONSTRAINT fk_livro FOREIGN KEY (id_livro) REFERENCES livros(id_livro)
);
