CREATE TABLE IF NOT EXISTS pessoas (
    id_pessoa BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    rua VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(100),
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS livros (
    id_livro BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    data_lancamento DATE NOT NULL,
    disponivel BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS emprestimos (
    id_emprestimo BIGSERIAL PRIMARY KEY,
    id_pessoa BIGINT NOT NULL,
    id_livro BIGINT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao_agendada DATE NOT NULL,
    data_devolucao_real DATE NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoas (id_pessoa),
    CONSTRAINT fk_livro FOREIGN KEY (id_livro) REFERENCES livros (id_livro)
);