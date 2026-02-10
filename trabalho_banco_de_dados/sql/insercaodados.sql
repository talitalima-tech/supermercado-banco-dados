USE supermercado;

-- =========================
-- CLIENTES
-- =========================
INSERT INTO cliente (nome, cpf) VALUES
('Ana Beatriz Costa Lima', '12345678909'),
('João Pedro Ribeiro Souza', '98765432100'),
('Mariana Alves Ferreira', '11122233344'),
('Lucas Henrique Rocha', '55566677788'),
('Camila Duarte Nascimento', '99988877766'),
('Felipe Moraes da Silva', '00011122233'),
('Letícia Carvalho Pinto', '32165498700'),
('Rafael Gomes Monteiro', '44455566677'),
('Bruna Oliveira Martins', '88899900011'),
('Gustavo Almeida Castro', '22233344455');

-- =========================
-- FUNÇÕES
-- =========================
INSERT INTO funcao (nome_funcao) VALUES
('Gerente'),
('Caixa'),
('Repositor');

-- =========================
-- FUNCIONÁRIOS
-- =========================
INSERT INTO funcionario (nome, funcao_id_funcao) VALUES
('Marcelo Antunes Ribeiro', 1), -- Gerente
('Juliana Rocha Cavalcante', 2), -- Caixa
('Thiago Alves Monteiro', 2), -- Caixa
('Eduardo Mendes Pereira', 3); -- Repositor

-- =========================
-- CAIXAS
-- =========================
INSERT INTO caixa (numero) VALUES
(1),
(2),
(3);

-- =========================
-- PRODUTOS
-- =========================
INSERT INTO produto (nome, descricao, preco, estoque, estoque_minimo) VALUES
('Arroz 5kg', 'Arroz branco tipo 1', 28.90, 50, 10),
('Feijão 1kg', 'Feijão carioca', 7.90, 40, 8),
('Óleo de Soja', 'Óleo 900ml', 6.50, 60, 15),
('Açúcar 1kg', 'Açúcar refinado', 4.80, 70, 20),
('Café 500g', 'Café torrado e moído', 14.90, 30, 5),
('Leite 1L', 'Leite integral', 4.50, 80, 20),
('Macarrão 500g', 'Macarrão espaguete', 3.90, 90, 25),
('Refrigerante 2L', 'Refrigerante cola', 9.50, 40, 10),
('Biscoito', 'Biscoito recheado', 2.80, 100, 30),
('Detergente', 'Detergente neutro', 2.20, 120, 40);

-- =========================
-- VENDAS
-- =========================
INSERT INTO venda (data_hora, total, cliente_id_cliente, funcionario_id_funcionario, caixa_id_caixa) VALUES
('2025-09-05 10:15:00', 41.30, 1, 2, 1),
('2025-09-05 11:30:00', 18.70, 2, 3, 2),
('2025-09-06 14:00:00', 62.40, 3, 2, 1),
('2025-09-06 16:45:00', 27.80, 4, 3, 3),
('2025-09-07 09:20:00', 33.40, 5, 2, 1);

-- =========================
-- ITENS DA VENDA (N:M)
-- =========================
INSERT INTO item_venda (venda_id_venda, produto_id_produto, quantidade, preco_unitario) VALUES
(1, 1, 1, 28.90), -- Arroz
(1, 6, 2, 4.50),  -- Leite
(2, 2, 1, 7.90),  -- Feijão
(2, 9, 2, 2.80),  -- Biscoito
(3, 5, 2, 14.90), -- Café
(3, 4, 1, 4.80),  -- Açúcar
(3, 7, 3, 3.90),  -- Macarrão
(4, 8, 2, 9.50),  -- Refrigerante
(5, 10, 4, 2.20); -- Detergente

-- =========================
-- PAGAMENTOS
-- =========================
INSERT INTO pagamento (tipo_pagamento, data_hora_pagamento, valor_pago, venda_id_venda) VALUES
('Dinheiro', '2025-09-05 10:20:00', 41.30, 1),
('Cartão', '2025-09-05 11:35:00', 18.70, 2),
('Pix', '2025-09-06 14:10:00', 62.40, 3),
('Cartão', '2025-09-06 16:50:00', 27.80, 4),
('Dinheiro', '2025-09-07 09:25:00', 33.40, 5);
