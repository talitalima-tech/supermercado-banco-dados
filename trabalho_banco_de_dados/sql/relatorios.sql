-- Seleciona o banco de dados 
USE supermercado;

-- Relatório 1: Produtos mais vendidos
SELECT 
    p.nome AS Produto,
    SUM(iv.quantidade) AS Total_Vendido
FROM item_venda iv
JOIN produto p ON iv.produto_id_produto = p.id_produto
GROUP BY p.nome
ORDER BY Total_Vendido DESC;

-- Relatório 2: Clientes cujo nome começa com 'A'
SELECT 
    id_cliente, 
    nome, 
    cpf 
FROM cliente 
WHERE nome 
LIKE 'A%';

-- Relatório 3: Produtos pagos com Pix
SELECT 
    v.id_venda AS Pedido,
    v.data_hora AS DataHora_Venda,
    c.nome AS Cliente,
    f.nome AS Funcionario,
    pag.tipo_pagamento AS Forma_Pagamento,
    pag.valor_pago AS Valor_Pago
FROM pagamento pag
JOIN venda v ON pag.venda_id_venda = v.id_venda
JOIN cliente c ON v.cliente_id_cliente = c.id_cliente
JOIN funcionario f ON v.funcionario_id_funcionario = f.id_funcionario
WHERE pag.tipo_pagamento = 'Pix';

