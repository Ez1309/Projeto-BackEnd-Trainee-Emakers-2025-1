-- Inserindo 5 livros clássicos na tabela 'livros'
INSERT INTO livros (nome, autor, data_lancamento, disponivel) VALUES
('O Senhor dos Anéis', 'J.R.R. Tolkien', '1954-07-29', true),
('O Guia do Mochileiro das Galáxias', 'Douglas Adams', '1979-10-12', true),
('1984', 'George Orwell', '1949-06-08', true),
('Duna', 'Frank Herbert', '1965-08-01', true),
('Fundação', 'Isaac Asimov', '1951-06-01', true);

-- Inserindo um usuário ADMIN padrão
-- IMPORTANTE: Substitua o placeholder pelo hash da senha do admin
INSERT INTO pessoas (nome, cpf, cep, email, senha, role) VALUES
(
    'admin',
    '0',
    '0',
    'admin',
    '$2a$10$iFx7cr04Jii64TCev19eBOCfFfdYIzS.hUEeVGS1RYY/hyn7OZTw.', -- <<< SUBSTITUA AQUI
    'ADMIN'
);

-- Inserindo um usuário USER padrão
-- IMPORTANTE: Substitua o placeholder pelo hash da senha do usuário
INSERT INTO pessoas (nome, cpf, cep, email, senha, role) VALUES
(
    'user',
    '1',
    '1',
    'user',
    '$2a$10$IdTm82j3WU9A5tjei4R5s.jrWal3rgpVOcC1Lutqoh/Jn5BZnUuEO', -- <<< SUBSTITUA AQUI
    'USER'
);