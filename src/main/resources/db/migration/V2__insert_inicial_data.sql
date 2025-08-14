
INSERT INTO
    livros (
        nome,
        autor,
        data_lancamento,
        quantidade_total,
        quantidade_disponivel
    )
VALUES
    ('O Senhor dos Anéis', 'J.R.R. Tolkien', '1954-07-29', 2, 2),
    ('O Guia do Mochileiro das Galáxias', 'Douglas Adams', '1979-10-12', 3, 3),
    ('1984', 'George Orwell', '1949-06-08', 1, 1),
    ('Duna', 'Frank Herbert', '1965-08-01', 4, 4),
    ('Fundação', 'Isaac Asimov', '1951-06-01', 2, 2);


INSERT INTO
    pessoas (nome, cpf, cep, email, senha, role)
VALUES
    (
        'admin', '00000000000', '00000000', 'admin',
        '$2a$10$iFx7cr04Jii64TCev19eBOCfFfdYIzS.hUEeVGS1RYY/hyn7OZTw.', 'ADMIN'
    );

INSERT INTO
    pessoas (nome, cpf, cep, email, senha, role)
VALUES
    (
        'user', '11111111111', '11111111', 'user',
        '$2a$10$IdTm82j3WU9A5tjei4R5s.jrWal3rgpVOcC1Lutqoh/Jn5BZnUuEO', 'USER'
    );