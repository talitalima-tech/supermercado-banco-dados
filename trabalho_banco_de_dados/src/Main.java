import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private static Connection conexao;
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws SQLException {
        // Conexão com Banco de Dados
        conexao = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/supermercado",
            "root",
            "password"
        );
        
        menu_principal();
        
        scanner.close();
        conexao.close();
    }

    // Menu Principal
    public static void menu_principal() throws SQLException {
        while (true) {
            System.out.println("\n==== MENU PRINCIPAL ====");
            System.out.println("Selecione com o que você quer trabalhar:");
            System.out.println("1. Produtos");
            System.out.println("2. Clientes");
            System.out.println("3. Funcionarios");
            System.out.println("4. Funções");
            System.out.println("5. Vendas");
            System.out.println("6. Pagamentos");
            System.out.println("0. Sair");

            System.out.print("Digite a opção: ");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            if (escolha == 1) {
                menu_produtos();
            } else if (escolha == 2) {
                menuClientes();
            } else if (escolha == 3) {
                menuFuncionarios();
            } else if (escolha == 4) {
                menuFuncoes();
            } else if (escolha == 5) {
                menuVendas();
            } else if (escolha == 6) {
                menuPagamento();
            } else if (escolha == 0) {
                System.out.println("Bye!");
                break;
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }

    // CRUD da Entidade produto
    public static void menu_produtos() throws SQLException {
        while (true) {
            System.out.println("\n--- Menu produtos ---");
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Exibir produtos");
            System.out.println("3. Modificar produto");
            System.out.println("4. Deletar produto");
            System.out.println("0. Voltar");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                cadastrarProduto();
            } else if (opcao == 2) {
                exibirProdutos();
            } else if (opcao == 3) {
                modificarProduto();
            } else if (opcao == 4) {
                deletarProduto();
            } else if (opcao == 0) {
                break;
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }

    public static void cadastrarProduto() throws SQLException {
        while (true) {
            System.out.print("nome do produto: ");
            String nome = scanner.nextLine();
            
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();
            
            System.out.print("Preço: ");
            double preco = Double.parseDouble(scanner.nextLine());

            System.out.print("Estoque: ");
            int estoque = scanner.nextInt();

            System.out.print("Estoque minímo: ");
            int estoqueMinimo = scanner.nextInt();

            String sql = "INSERT INTO produto (nome, descricao, preco, estoque, estoque_minimo) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setDouble(3, preco);
            stmt.setInt(4, estoque);
            stmt.setInt(5, estoqueMinimo);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("produto cadastrado com sucesso!");

            System.out.print("Deseja cadastrar outro produto? (s/n): ");
            String cont = scanner.nextLine();
            if (!cont.equalsIgnoreCase("s")) {
                break;
            }
        }
    }

    public static void exibirProdutos() throws SQLException {
        System.out.println("\n--- Filtros ---");
        System.out.println("1. Todos os produtos");
        System.out.println("2. Ordem alfabética");
        System.out.println("3. Faixa de preço");
        System.out.println("4. Buscar por ID");
        
        System.out.print("Escolha o tipo de exibição: ");
        String escolha = scanner.nextLine();

        PreparedStatement stmt = null;
        if (escolha.equals("1")) {
            stmt = conexao.prepareStatement("SELECT * FROM produto");
        } else if (escolha.equals("2")) {
            stmt = conexao.prepareStatement("SELECT * FROM produto ORDER BY nome");
        } else if (escolha.equals("3")) {
            System.out.print("Preço mínimo: ");
            double minimo = Double.parseDouble(scanner.nextLine());
            System.out.print("Preço máximo: ");
            double maximo = Double.parseDouble(scanner.nextLine());
            stmt = conexao.prepareStatement("SELECT * FROM produto WHERE preco BETWEEN ? AND ?");
            stmt.setDouble(1, minimo);
            stmt.setDouble(2, maximo);
        } else if (escolha.equals("4")) {
            System.out.print("Digite o ID do produto: ");
            String id_prod = scanner.nextLine();
            stmt = conexao.prepareStatement("SELECT * FROM produto WHERE id_produto = ?");
            stmt.setString(1, id_prod);
        } else {
            System.out.println("Opção inválida.");
            return;
        }

        ResultSet resultados = stmt.executeQuery();
        if (!resultados.next()) {
            System.out.println("Nenhum produto encontrado.");
        } else {
            System.out.println("\n--- produtos ---");
            do {
                System.out.printf("ID: %s, nome: %s, Descrição: %s, Preço: R$%.2f%n",
                    resultados.getInt("id_produto"),
                    resultados.getString("nome"),
                    resultados.getString("descricao"),
                    resultados.getDouble("preco"));
            } while (resultados.next());
        }
        resultados.close();
        stmt.close();
    }

    public static void modificarProduto() throws SQLException {
        System.out.print("Digite o ID do produto que deseja modificar: ");
        String id_produto = scanner.nextLine();
        
        String sql = "SELECT * FROM produto WHERE id_produto = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_produto);
        ResultSet produto = stmt.executeQuery();

        if (!produto.next()) {
            System.out.println("produto não encontrado.");
            produto.close();
            stmt.close();
            return;
        }
        
        System.out.printf("produto atual: %s - %s - R$%.2f%n",
            produto.getString("nome"),
            produto.getString("descricao"),
            produto.getDouble("preco"));
        
        System.out.print("Novo nome (ou pressione Enter para manter): ");
        String nome = scanner.nextLine();
        if (nome.isEmpty()) {
            nome = produto.getString("nome");
        }
        
        System.out.print("Nova descrição (ou pressione Enter para manter): ");
        String descricao = scanner.nextLine();
        if (descricao.isEmpty()) {
            descricao = produto.getString("descricao");
        }
        
        System.out.print("Novo preço (ou pressione Enter para manter): ");
        String precoStr = scanner.nextLine();
        double preco;
        if (precoStr.isEmpty()) {
            preco = produto.getDouble("preco");
        } else {
            preco = Double.parseDouble(precoStr);
        }
        
        produto.close();
        stmt.close();
        
        sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ? WHERE id_produto = ?";
        PreparedStatement updateStmt = conexao.prepareStatement(sql);
        updateStmt.setString(1, nome);
        updateStmt.setString(2, descricao);
        updateStmt.setDouble(3, preco);
        updateStmt.setString(4, id_produto);
        updateStmt.executeUpdate();
        updateStmt.close();
        System.out.println("produto atualizado com sucesso!");
    }

    public static void deletarProduto() throws SQLException {
        System.out.println("\n--- Opções de Exclusão ---");
        System.out.println("1. Deletar um produto específico");
        System.out.println("2. Deletar todos os produtos");
        
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();

        if (opcao.equals("1")) {
            System.out.print("Digite o ID do produto: ");
            String id_produto = scanner.nextLine();
            
            String sql = "SELECT * FROM produto WHERE id_produto = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, id_produto);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                rs.close();
                stmt.close();
                sql = "DELETE FROM produto WHERE id_produto = ?";
                PreparedStatement deleteStmt = conexao.prepareStatement(sql);
                deleteStmt.setString(1, id_produto);
                deleteStmt.executeUpdate();
                deleteStmt.close();
                System.out.println("produto deletado com sucesso!");
            } else {
                rs.close();
                stmt.close();
                System.out.println("produto não encontrado.");
            }
        } else if (opcao.equals("2")) {
            System.out.print("Tem certeza que deseja deletar TODOS os produtos? (s/n): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("s")) {
                Statement stmt = conexao.createStatement();
                stmt.executeUpdate("DELETE FROM produto");
                stmt.close();
                System.out.println("Todos os produtos foram deletados.");
            } else {
                System.out.println("Ação cancelada.");
            }
        } else {
            System.out.println("Opção inválida.");
        }
    }

    // CRUD da Entidade clientes
    public static void menuClientes() throws SQLException {
        while (true) {
            System.out.println("\n--- Menu clientes ---");
            System.out.println("1. Cadastrar cliente");
            System.out.println("2. Exibir clientes");
            System.out.println("3. Modificar cliente");
            System.out.println("4. Deletar cliente");
            System.out.println("0. Voltar");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                cadastrarCliente();
            } else if (opcao == 2) {
                exibirClientes();
            } else if (opcao == 3) {
                modificarCliente();
            } else if (opcao == 4) {
                deletarCliente();
            } else if (opcao == 0) {
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public static void cadastrarCliente() throws SQLException {
        while (true) {
            System.out.print("cpf (apenas números): ");
            String cpf = scanner.nextLine();
            
            System.out.print("nome completo: ");
            String nome = scanner.nextLine();

            String sql = "INSERT INTO cliente (cpf, nome) VALUES (?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("cliente cadastrado com sucesso!");

            System.out.print("Deseja cadastrar outro cliente? (s/n): ");
            String cont = scanner.nextLine();
            if (!cont.equalsIgnoreCase("s")) {
                break;
            }
        }
    }

    public static void exibirClientes() throws SQLException {
        System.out.println("\n--- Filtros ---");
        System.out.println("1. Todos os clientes");
        System.out.println("2. Buscar por cpf");
        System.out.println("3. Ordem alfabética");
        
        System.out.print("Escolha o tipo de exibição: ");
        String escolha = scanner.nextLine();

        PreparedStatement stmt = null;
        if (escolha.equals("1")) {
            stmt = conexao.prepareStatement("SELECT * FROM cliente");
        } else if (escolha.equals("2")) {
            System.out.print("Digite o cpf: ");
            String cpf = scanner.nextLine();
            stmt = conexao.prepareStatement("SELECT * FROM cliente WHERE cpf = ?");
            stmt.setString(1, cpf);
        } else if (escolha.equals("3")) {
            stmt = conexao.prepareStatement("SELECT * FROM cliente ORDER BY nome");
        } else {
            System.out.println("Opção inválida.");
            return;
        }

        ResultSet resultados = stmt.executeQuery();
        if (!resultados.next()) {
            System.out.println("Nenhum cliente encontrado.");
        } else {
            System.out.println("\n--- clientes ---");
            do {
                System.out.printf("cpf: %s, nome: %s%n",
                    resultados.getString("cpf"),
                    resultados.getString("nome"));
            } while (resultados.next());
        }
        resultados.close();
        stmt.close();
    }

    public static void modificarCliente() throws SQLException {
        System.out.print("Digite o cpf do cliente que deseja modificar: ");
        String cpf = scanner.nextLine();
        
        String sql = "SELECT * FROM cliente WHERE cpf = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, cpf);
        ResultSet cliente = stmt.executeQuery();

        if (!cliente.next()) {
            System.out.println("cliente não encontrado.");
            cliente.close();
            stmt.close();
            return;
        }
        
        System.out.printf("cliente atual: %s%n", cliente.getString("nome"));
        
        System.out.print("Novo nome (ou pressione Enter para manter): ");
        String nome = scanner.nextLine();
        if (nome.isEmpty()) {
            nome = cliente.getString("nome");
        }
        
        cliente.close();
        stmt.close();
        
        sql = "UPDATE cliente SET nome = ? WHERE cpf = ?";
        PreparedStatement updateStmt = conexao.prepareStatement(sql);
        updateStmt.setString(1, nome);
        updateStmt.setString(2, cpf);
        updateStmt.executeUpdate();
        updateStmt.close();
        System.out.println("cliente atualizado com sucesso!");
    }

    public static void deletarCliente() throws SQLException {
        System.out.print("Digite o cpf do cliente a ser deletado: ");
        String cpf = scanner.nextLine();
        
        // Verifica se existe venda com esse cliente
        String sql = "SELECT * FROM venda WHERE cliente_id_cliente = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, cpf);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            rs.close();
            stmt.close();
            System.out.println("Este cliente possui compras registradas e não pode ser deletado.");
            return;
        }
        rs.close();
        stmt.close();
        
        sql = "SELECT * FROM cliente WHERE cpf = ?";
        stmt = conexao.prepareStatement(sql);
        stmt.setString(1, cpf);
        rs = stmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stmt.close();
            System.out.println("cliente não encontrado.");
            return;
        }
        rs.close();
        stmt.close();
        
        sql = "DELETE FROM cliente WHERE cpf = ?";
        PreparedStatement deleteStmt = conexao.prepareStatement(sql);
        deleteStmt.setString(1, cpf);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        System.out.println("cliente deletado com sucesso!");
    }

    // CRUD da Entidade Função
    public static void menuFuncoes() throws SQLException {
        while (true) {
            System.out.println("\n--- Menu Funções ---");
            System.out.println("1. Cadastrar Função");
            System.out.println("2. Exibir Funções");
            System.out.println("3. Modificar Função");
            System.out.println("4. Deletar Função");
            System.out.println("0. Voltar");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                cadastrarFuncao();
            } else if (opcao == 2) {
                exibirFuncoes();
            } else if (opcao == 3) {
                modificarFuncao();
            } else if (opcao == 4) {
                deletarFuncao();
            } else if (opcao == 0) {
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public static void cadastrarFuncao() throws SQLException {
        while (true) {
            System.out.print("nome da função: ");
            String nome = scanner.nextLine();

            String sql = "INSERT INTO funcao (nome_funcao) VALUES (?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("Função cadastrada com sucesso!");

            System.out.print("Deseja cadastrar outra função? (s/n): ");
            String cont = scanner.nextLine();
            if (!cont.equalsIgnoreCase("s")) {
                break;
            }
        }
    }

    public static void exibirFuncoes() throws SQLException {
        Statement stmt = conexao.createStatement();
        ResultSet funcoes = stmt.executeQuery("SELECT * FROM funcao ORDER BY id_funcao");

        if (!funcoes.next()) {
            System.out.println("Nenhuma função cadastrada.");
        } else {
            System.out.println("\n--- Lista de Funções ---");
            do {
                System.out.printf("ID: %s | nome: %s%n",
                    funcoes.getInt("id_funcao"),
                    funcoes.getString("nome_funcao"));
            } while (funcoes.next());
        }
        funcoes.close();
        stmt.close();
    }

    public static void modificarFuncao() throws SQLException {
        System.out.print("Digite o ID da função que deseja modificar: ");
        String id_funcao = scanner.nextLine();
        
        String sql = "SELECT * FROM funcao WHERE id_funcao = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_funcao);
        ResultSet funcao = stmt.executeQuery();

        if (!funcao.next()) {
            System.out.println("Função não encontrada.");
            funcao.close();
            stmt.close();
            return;
        }
        
        System.out.printf("nome atual: %s%n", funcao.getString("nome_funcao"));
        
        System.out.print("Novo nome (ou Enter para manter): ");
        String novo_nome = scanner.nextLine();
        if (novo_nome.isEmpty()) {
            novo_nome = funcao.getString("nome_funcao");
        }
        
        funcao.close();
        stmt.close();
        
        sql = "UPDATE funcao SET nome_funcao = ? WHERE id_funcao = ?";
        PreparedStatement updateStmt = conexao.prepareStatement(sql);
        updateStmt.setString(1, novo_nome);
        updateStmt.setString(2, id_funcao);
        updateStmt.executeUpdate();
        updateStmt.close();
        System.out.println("Função atualizada com sucesso!");
    }

    public static void deletarFuncao() throws SQLException {
        System.out.print("Digite o ID da função a ser deletada: ");
        String id_funcao = scanner.nextLine();

        // Verifica se existe funcionario com essa função
        String sql = "SELECT * FROM funcionario WHERE funcao_id_funcao = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_funcao);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            rs.close();
            stmt.close();
            System.out.println("Não é possível deletar: existem funcionarios usando essa função.");
            return;
        }
        rs.close();
        stmt.close();
        
        sql = "SELECT * FROM funcao WHERE id_funcao = ?";
        stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_funcao);
        rs = stmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stmt.close();
            System.out.println("Função não encontrada.");
            return;
        }
        rs.close();
        stmt.close();
        
        sql = "DELETE FROM funcao WHERE id_funcao = ?";
        PreparedStatement deleteStmt = conexao.prepareStatement(sql);
        deleteStmt.setString(1, id_funcao);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        System.out.println("Função deletada com sucesso!");
    }

    // CRUD da Entidade funcionarios
    public static void menuFuncionarios() throws SQLException {
        while (true) {
            System.out.println("\n--- Menu funcionarios ---");
            System.out.println("1. Cadastrar funcionario");
            System.out.println("2. Exibir funcionarios");
            System.out.println("3. Modificar funcionario");
            System.out.println("4. Deletar funcionario");
            System.out.println("0. Voltar");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                cadastrarFuncionario();
            } else if (opcao == 2) {
                exibirFuncionarios();
            } else if (opcao == 3) {
                modificarFuncionario();
            } else if (opcao == 4) {
                deletarFuncionario();
            } else if (opcao == 0) {
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public static void cadastrarFuncionario() throws SQLException {
        // Verifica se existem funções disponíveis
        Statement stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet funcoes = stmt.executeQuery("SELECT * FROM funcao");
        
        if (!funcoes.next()) {
            System.out.println("Nenhuma função cadastrada. Cadastre uma função primeiro.");
            funcoes.close();
            stmt.close();
            return;
        }
        
        funcoes.beforeFirst();
        
        while (true) {
            System.out.print("ID do funcionario (número): ");
            String id_func = scanner.nextLine();
            
            
            System.out.print("nome completo do funcionario: ");
            String nome = scanner.nextLine();

            System.out.println("\n--- Funções Disponíveis ---");
            while (funcoes.next()) {
                System.out.printf("%s - %s%n",
                    funcoes.getInt("id_funcao"),
                    funcoes.getString("nome_funcao"));
            }
            funcoes.beforeFirst();
            
            System.out.print("Digite o ID da função: ");
            String id_funcao = scanner.nextLine();

            String sql = "INSERT INTO funcionario (id_funcionario, nome, funcao_id_funcao) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conexao.prepareStatement(sql);
            insertStmt.setInt(1, Integer.parseInt(id_func));
            insertStmt.setString(2, nome);
            insertStmt.setString(3, id_funcao);
            insertStmt.executeUpdate();
            insertStmt.close();
            System.out.println("funcionario cadastrado com sucesso!");

            System.out.print("Deseja cadastrar outro funcionario? (s/n): ");
            String cont = scanner.nextLine();
            if (!cont.equalsIgnoreCase("s")) {
                break;
            }
        }
        funcoes.close();
        stmt.close();
    }

    public static void exibirFuncionarios() throws SQLException {
        String sql = """
            SELECT f.id_funcionario, f.nome, fn.nome_funcao AS funcao
            FROM funcionario f
            JOIN funcao fn ON f.funcao_id_funcao = fn.id_funcao
            ORDER BY f.nome
            """;
            
        Statement stmt = conexao.createStatement();
        ResultSet funcionarios = stmt.executeQuery(sql);

        if (!funcionarios.next()) {
            System.out.println("Nenhum funcionario encontrado.");
        } else {
            System.out.println("\n--- Lista de funcionarios ---");
            do {
                System.out.printf("ID: %s | nome: %s | Função: %s%n",
                    funcionarios.getInt("id_funcionario"),
                    funcionarios.getString("nome"),
                    funcionarios.getString("Funcao"));
            } while (funcionarios.next());
        }
        funcionarios.close();
        stmt.close();
    }

    public static void modificarFuncionario() throws SQLException {
        System.out.print("Digite o ID do funcionario que deseja modificar: ");
        String id_func = scanner.nextLine();
        
        String sql = "SELECT * FROM funcionario WHERE id_funcionario = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_func);
        ResultSet func = stmt.executeQuery();

        if (!func.next()) {
            System.out.println("funcionario não encontrado.");
            func.close();
            stmt.close();
            return;
        }
        
        String nome_atual = func.getString("nome");
        String id_funcao_atual = func.getString("funcao_id_funcao");
        
        // Lista funções disponíveis
        System.out.println("\n--- Funções Disponíveis ---");
        Statement stmt2 = conexao.createStatement();
        ResultSet funcoes = stmt2.executeQuery("SELECT * FROM funcao");
        
        while (funcoes.next()) {
            System.out.printf("%s - %s%n",
                funcoes.getInt("id_funcao"),
                funcoes.getString("nome_funcao"));
        }
        funcoes.close();
        stmt2.close();
        
        System.out.print("Novo nome (ou Enter para manter): ");
        String novo_nome = scanner.nextLine();
        if (novo_nome.isEmpty()) {
            novo_nome = nome_atual;
        }
        
        System.out.print("Novo ID da função (ou Enter para manter): ");
        String nova_funcao = scanner.nextLine();
        if (nova_funcao.isEmpty()) {
            nova_funcao = id_funcao_atual;
        }
        
        func.close();
        stmt.close();
        
        sql = "UPDATE funcionario SET nome = ?, funcao_id_funcao = ? WHERE id_funcionario = ?";
        PreparedStatement updateStmt = conexao.prepareStatement(sql);
        updateStmt.setString(1, novo_nome);
        updateStmt.setString(2, nova_funcao);
        updateStmt.setString(3, id_func);
        updateStmt.executeUpdate();
        updateStmt.close();
        System.out.println("funcionario atualizado com sucesso!");
    }

    public static void deletarFuncionario() throws SQLException {
        System.out.print("Digite o ID do funcionario a ser deletado: ");
        String id_func = scanner.nextLine();

        // Verifica dependência em vendas
        String sql = "SELECT * FROM venda WHERE funcionario_id_funcionario = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_func);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            rs.close();
            stmt.close();
            System.out.println("funcionario associado a vendas. Não pode ser deletado.");
            return;
        }
        rs.close();
        stmt.close();
        
        sql = "SELECT * FROM funcionario WHERE id_funcionario = ?";
        stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_func);
        rs = stmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stmt.close();
            System.out.println("funcionario não encontrado.");
            return;
        }
        rs.close();
        stmt.close();
        
        sql = "DELETE FROM funcionario WHERE id_funcionario = ?";
        PreparedStatement deleteStmt = conexao.prepareStatement(sql);
        deleteStmt.setString(1, id_func);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        System.out.println("funcionario deletado com sucesso!");
    }

    // CRUD da Entidade Vendas
    public static void menuVendas() throws SQLException {
        while (true) {
            System.out.println("\n--- Menu de Vendas ---");
            System.out.println("1. Cadastrar venda");
            System.out.println("2. Exibir vendas");
            System.out.println("3. Modificar venda");
            System.out.println("4. Deletar venda");
            System.out.println("0. Voltar");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                cadastrarVenda();
            } else if (opcao == 2) {
                exibirVenda();
            } else if (opcao == 3) {
                modificarVenda();
            } else if (opcao == 4) {
                deletarVenda();
            } else if (opcao == 0) {
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public static void cadastrarVenda() throws SQLException {
    // Verifica clientes
    Statement stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    ResultSet clientes = stmt.executeQuery("SELECT id_cliente, nome FROM cliente");
    
    if (!clientes.next()) {
        System.out.println("Nenhum cliente cadastrado.");
        clientes.close();
        stmt.close();
        return;
    }
    clientes.beforeFirst();
    
    // Verifica caixas
    String sql = """
        SELECT f.id_funcionario, f.nome FROM funcionario f
        JOIN funcao fn ON f.funcao_id_funcao = fn.id_funcao
        WHERE fn.nome_funcao = 'Caixa'
        """;
        
    Statement stmt2 = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    ResultSet funcionarios = stmt2.executeQuery(sql);
    
    if (!funcionarios.next()) {
        System.out.println("Nenhum caixa cadastrado.");
        clientes.close();
        stmt.close();
        funcionarios.close();
        stmt2.close();
        return;
    }
    funcionarios.beforeFirst();
    
    while (true) {
        // System.out.print("Número da caixa_id_caixa: ");
        int caixa_id_caixa = 1;
        // try {
        //     caixa_id_caixa = Integer.parseInt(scanner.nextLine());
        // } catch (NumberFormatException e) {
        //     System.out.println("Número inválido. Tente novamente.");
        //     continue;
        // }

        System.out.println("\n--- Clientes Disponíveis ---");
        clientes.beforeFirst(); // Volta ao início
        while (clientes.next()) {
            System.out.printf("%s - %s%n",
                clientes.getString("id_cliente"),
                clientes.getString("nome"));
        }
        
        System.out.print("Digite o CPF do cliente: ");
        String cpf_cliente = scanner.nextLine();

        System.out.println("\n--- Caixas Disponíveis ---");
        funcionarios.beforeFirst(); // Volta ao início
        while (funcionarios.next()) {
            System.out.printf("%d - %s%n",
                funcionarios.getInt("id_funcionario"),
                funcionarios.getString("nome"));
        }
        
        System.out.print("Digite o ID do caixa: ");
        int id_func;
        try {
            id_func = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Tente novamente.");
            continue;
        }

        String data_hora = LocalDateTime.now().format(formatter);

        sql = """
            INSERT INTO venda (caixa_id_caixa, data_hora, cliente_id_cliente, funcionario_id_funcionario)
            VALUES (?, ?, ?, ?)
            """;
            
        PreparedStatement insertStmt = null;
        ResultSet generatedKeys = null;
        int venda_id = 0;
        
        try {
            insertStmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setInt(1, caixa_id_caixa);
            insertStmt.setString(2, data_hora);
            insertStmt.setString(3, cpf_cliente);
            insertStmt.setInt(4, id_func);
            insertStmt.executeUpdate();
            
            generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                venda_id = generatedKeys.getInt(1);
            }
            System.out.println("Venda cadastrada com sucesso! ID: " + venda_id);
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (insertStmt != null) insertStmt.close();
        }

        // Inserir produtos no venda_has_produto
        while (true) {
            System.out.println("\n--- Produtos Disponíveis ---");
            
            // Fechar e recriar a consulta de produtos a cada iteração
            Statement stmt3 = conexao.createStatement();
            ResultSet produtos = stmt3.executeQuery("SELECT id_produto, nome FROM produto");
            
            while (produtos.next()) {
                System.out.printf("%d - %s%n",
                    produtos.getInt("id_produto"),
                    produtos.getString("nome"));
            }
            produtos.close();
            stmt3.close();
            
            System.out.print("ID do produto (0 para sair): ");
            int id_produto;
            try {
                id_produto = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Tente novamente.");
                continue;
            }
            
            if (id_produto == 0) {
                break;
            }

            // Verifica se o produto existe
            sql = "SELECT * FROM produto WHERE id_produto = ?";
            PreparedStatement checkStmt = null;
            ResultSet rs = null;
            
            try {
                checkStmt = conexao.prepareStatement(sql);
                checkStmt.setInt(1, id_produto);
                rs = checkStmt.executeQuery();
                
                if (!rs.next()) {
                    System.out.println("Produto não encontrado. Tente novamente.");
                    continue;
                }
            } finally {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
            }
            
            System.out.print("Quantidade: ");
            int qtd;
            try {
                qtd = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Quantidade inválida. Tente novamente.");
                continue;
            }
            
            sql = """
                INSERT INTO venda_has_produto (venda_id_venda, produto_id_produto, quantidade)
                VALUES (?, ?, ?)
                """;
                
            PreparedStatement produtoStmt = null;
            try {
                produtoStmt = conexao.prepareStatement(sql);
                produtoStmt.setInt(1, venda_id);
                produtoStmt.setInt(2, id_produto);
                produtoStmt.setInt(3, qtd);
                produtoStmt.executeUpdate();
                System.out.println("Produto adicionado a venda!");
            } finally {
                if (produtoStmt != null) produtoStmt.close();
            }

            System.out.print("Adicionar outro produto a venda? (s/n): ");
            String cont = scanner.nextLine();
            if (!cont.equalsIgnoreCase("s")) {
                break;
            }
        }

        System.out.print("Deseja cadastrar outra venda? (s/n): ");
        String cont = scanner.nextLine();
        if (!cont.equalsIgnoreCase("s")) {
            break;
        }
    }
    
    clientes.close();
    stmt.close();
    funcionarios.close();
    stmt2.close();
}

        public static void exibirVenda() throws SQLException {
        String sql = """
            SELECT p.id_venda, p.data_hora, p.total, p.cliente_id_cliente, p.funcionario_id_funcionario, p.caixa_id_caixa
            FROM venda p
            JOIN cliente c ON p.cliente_id_cliente = c.id_cliente
            JOIN funcionario f ON p.funcionario_id_funcionario = f.id_funcionario
            JOIN caixa x ON p.caixa_id_caixa = x.id_caixa
            ORDER BY p.data_hora DESC
            """;
            
        Statement stmt = conexao.createStatement();
        ResultSet vendas = stmt.executeQuery(sql);

        if (!vendas.next()) {
            System.out.println("Nenhuma venda encontrado.");
        } else {
            System.out.println("\n--- Lista de vendas ---");
            do {
                System.out.printf("ID: %s | data_hora: %s | caixa_id_caixa: %s | cliente: %s | funcionario: %s%n",
                    vendas.getInt("id_venda"),
                    vendas.getString("data_hora"),
                    vendas.getInt("caixa_id_caixa"),
                    vendas.getString("cliente_id_cliente"),
                    vendas.getString("funcionario_id_funcionario"));
            } while (vendas.next());
        }
        vendas.close();
        stmt.close();
    }

    public static void modificarVenda() throws SQLException {
        System.out.print("Digite o ID da venda que deseja modificar: ");
        String id_pedido = scanner.nextLine();
        
        String sql = "SELECT * FROM venda WHERE id_venda = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_pedido);
        ResultSet pedido = stmt.executeQuery();

        if (!pedido.next()) {
            System.out.println("Venda não encontrado.");
            pedido.close();
            stmt.close();
            return;
        }
        
        System.out.printf("Venda atual: Caixa %s, cliente ID %s, funcionario ID %s%n",
            pedido.getInt("caixa_id_caixa"),
            pedido.getString("cliente_id_cliente"),
            pedido.getInt("funcionario_id_funcionario"));
        
        //System.out.print("Nova caixa_id_caixa (ou Enter para manter): ");
        //String nova_caixa_id_caixa_str = scanner.nextLine();
        int nova_caixa_id_caixa = 1;
        // if (nova_caixa_id_caixa_str.isEmpty()) {
        //     nova_caixa_id_caixa = pedido.getInt("caixa_id_caixa");
        // } else {
        //     nova_caixa_id_caixa = Integer.parseInt(nova_caixa_id_caixa_str);
        // }
        
        // Lista os clientes
        System.out.println("\n--- clientes ---");
        Statement stmt2 = conexao.createStatement();
        ResultSet clientes = stmt2.executeQuery("SELECT * FROM cliente");
        
        while (clientes.next()) {
            System.out.printf("%s - %s%n",
                clientes.getString("id_cliente"),
                clientes.getString("nome"));
        }
        clientes.close();
        stmt2.close();
        
        System.out.print("Novo ID do cliente (ou Enter para manter): ");
        String novo_id = scanner.nextLine();
        if (novo_id.isEmpty()) {
            novo_id = pedido.getString("cliente_id_cliente");
        }
        
        // Lista os funcionarios
        System.out.println("\n--- funcionarios ---");
        Statement stmt3 = conexao.createStatement();
        ResultSet funcionarios = stmt3.executeQuery("SELECT * FROM funcionario WHERE funcao_id_funcao = 2");
        
        while (funcionarios.next()) {
            System.out.printf("%s - %s%n",
                funcionarios.getInt("id_funcionario"),
                funcionarios.getString("nome"));
        }
        funcionarios.close();
        stmt3.close();
        
        System.out.print("Novo ID do funcionario (ou Enter para manter): ");
        String novo_func_str = scanner.nextLine();
        int novo_func;
        if (novo_func_str.isEmpty()) {
            novo_func = pedido.getInt("funcionario_id_funcionario");
        } else {
            novo_func = Integer.parseInt(novo_func_str);
        }
        
        pedido.close();
        stmt.close();
        
        sql = """
            UPDATE venda SET caixa_id_caixa = ?, cliente_id_cliente = ?, funcionario_id_funcionario = ?
            WHERE id_venda = ?
            """;
        PreparedStatement updateStmt = conexao.prepareStatement(sql);
        updateStmt.setInt(1, nova_caixa_id_caixa);
        updateStmt.setString(2, novo_id);
        updateStmt.setInt(3, novo_func);
        updateStmt.setString(4, id_pedido);
        updateStmt.executeUpdate();
        updateStmt.close();
        System.out.println("Venda atualizada com sucesso!");
    }

    public static void deletarVenda() throws SQLException {
        System.out.print("Digite o ID do pedido a ser deletado: ");
        String id_pedido = scanner.nextLine();

        // Verifica se o pedido existe
        String sql = "SELECT * FROM venda WHERE id_venda = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_pedido);
        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stmt.close();
            System.out.println("Pedido não encontrado.");
            return;
        }
        rs.close();
        stmt.close();

        // Confirmação antes da exclusão
        System.out.print("Tem certeza que deseja deletar este pedido e todos os dados relacionados (pagamento, produtos)? (s/n): ");
        String confirmar = scanner.nextLine();
        if (!confirmar.equalsIgnoreCase("s")) {
            System.out.println("Ação cancelada.");
            return;
        }

        // Deleta o pedido
        sql = "DELETE FROM venda WHERE id_venda = ?";
        PreparedStatement deleteStmt = conexao.prepareStatement(sql);
        deleteStmt.setString(1, id_pedido);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        System.out.println("Pedido e dados relacionados deletados com sucesso!");
    }

    // CRUD da Entidade Pagamento
    public static void menuPagamento() throws SQLException {
        while (true) {
            System.out.println("\n--- Menu Pagamentos ---");
            System.out.println("1. Cadastrar Pagamento");
            System.out.println("2. Exibir Pagamentos");
            System.out.println("3. Modificar Pagamento");
            System.out.println("4. Deletar Pagamento");
            System.out.println("0. Voltar");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                cadastrarPagamento();
            } else if (opcao == 2) {
                exibirPagamentos();
            } else if (opcao == 3) {
                modificarPagamento();
            } else if (opcao == 4) {
                deletarPagamento();
            } else if (opcao == 0) {
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public static void cadastrarPagamento() throws SQLException {
        // Verifica se há pedidos
        Statement stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet pedidos = stmt.executeQuery("SELECT * FROM venda");
        
        if (!pedidos.next()) {
            System.out.println("Nenhuma venda cadastrada.");
            pedidos.close();
            stmt.close();
            return;
        }
        pedidos.beforeFirst();
        
        while (true) {
            System.out.println("\n--- Vendas Disponíveis ---");
            while (pedidos.next()) {
                System.out.printf("ID: %s | Caixa: %s | data/hora: %s%n",
                    pedidos.getInt("id_venda"),
                    pedidos.getInt("caixa_id_caixa"),
                    pedidos.getString("data_hora"));
            }
            pedidos.beforeFirst();
            
            System.out.print("Digite o ID do pedido a ser pago: ");
            String id_pedido = scanner.nextLine();

            System.out.print("Tipo de pagamento (Dinheiro, Cartão, Pix): ");
            String tipo = scanner.nextLine();
            
            System.out.print("Valor total: ");
            double valor = Double.parseDouble(scanner.nextLine());
            
            String data_hora = LocalDateTime.now().format(formatter);

            String sql = """
                INSERT INTO pagamento (tipo_pagamento, valor_pago, data_hora_pagamento, venda_id_venda)
                VALUES (?, ?, ?, ?)
                """;
                
            PreparedStatement insertStmt = conexao.prepareStatement(sql);
            insertStmt.setString(1, tipo);
            insertStmt.setDouble(2, valor);
            insertStmt.setString(3, data_hora);
            insertStmt.setString(4, id_pedido);
            insertStmt.executeUpdate();
            insertStmt.close();
            System.out.println("Pagamento registrado com sucesso!");

            System.out.print("Deseja cadastrar outro pagamento? (s/n): ");
            String cont = scanner.nextLine();
            if (!cont.equalsIgnoreCase("s")) {
                break;
            }
        }
        pedidos.close();
        stmt.close();
    }

    public static void exibirPagamentos() throws SQLException {
        Statement stmt = conexao.createStatement();
        ResultSet pagamentos = stmt.executeQuery("""
            SELECT pg.id_pagamento, pg.tipo_pagamento, pg.valor_pago, pg.data_hora_pagamento, pg.venda_id_venda
            FROM pagamento pg
            JOIN venda p ON pg.venda_id_venda = p.id_venda
            ORDER BY pg.data_hora_pagamento DESC
            """);

        if (!pagamentos.next()) {
            System.out.println("Nenhum pagamento registrado.");
        } else {
            System.out.println("\n--- Lista de Pagamentos ---");
            do {
                System.out.printf("ID: %s | Tipo: %s | Valor: R$%.2f | data_hora: %s | Pedido: %s%n",
                    pagamentos.getInt("id_pagamento"),
                    pagamentos.getString("tipo_pagamento"),
                    pagamentos.getDouble("valor_pago"),
                    pagamentos.getString("data_hora_pagamento"),
                    pagamentos.getInt("venda_id_venda"));
            } while (pagamentos.next());
        }
        pagamentos.close();
        stmt.close();
    }

    public static void modificarPagamento() throws SQLException {
        System.out.print("Digite o ID do pagamento que deseja modificar: ");
        String id_pg = scanner.nextLine();
        
        String sql = "SELECT * FROM pagamento WHERE id_pagamento = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_pg);
        ResultSet pagamento = stmt.executeQuery();

        if (!pagamento.next()) {
            System.out.println("Pagamento não encontrado.");
            pagamento.close();
            stmt.close();
            return;
        }
        
        System.out.printf("Atual: Tipo %s, Valor %.2f, Venda %s%n",
            pagamento.getString("tipo_pagamento"),
            pagamento.getDouble("valor_pago"),
            pagamento.getInt("venda_id_venda"));
        
        System.out.print("Novo tipo de pagamento (ou Enter para manter): ");
        String novo_tipo = scanner.nextLine();
        if (novo_tipo.isEmpty()) {
            novo_tipo = pagamento.getString("tipo_pagamento");
        }
        
        System.out.print("Novo valor (ou Enter para manter): ");
        String novo_valor_str = scanner.nextLine();
        double novo_valor;
        if (novo_valor_str.isEmpty()) {
            novo_valor = pagamento.getDouble("valor_pago");
        } else {
            novo_valor = Double.parseDouble(novo_valor_str);
        }
        
        pagamento.close();
        stmt.close();
        
        sql = """
            UPDATE pagamento SET tipo_pagamento = ?, valor_pago = ?
            WHERE id_pagamento = ?
            """;
        PreparedStatement updateStmt = conexao.prepareStatement(sql);
        updateStmt.setString(1, novo_tipo);
        updateStmt.setDouble(2, novo_valor);
        updateStmt.setString(3, id_pg);
        updateStmt.executeUpdate();
        updateStmt.close();
        System.out.println("Pagamento atualizado com sucesso!");
    }

    public static void deletarPagamento() throws SQLException {
        System.out.print("Digite o ID do pagamento que deseja deletar: ");
        String id_pg = scanner.nextLine();
        
        String sql = "SELECT * FROM pagamento WHERE id_pagamento = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, id_pg);
        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stmt.close();
            System.out.println("Pagamento não encontrado.");
            return;
        }
        rs.close();
        stmt.close();
        
        sql = "DELETE FROM pagamento WHERE id_pagamento = ?";
        PreparedStatement deleteStmt = conexao.prepareStatement(sql);
        deleteStmt.setString(1, id_pg);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        System.out.println("Pagamento deletado com sucesso!");
    }
}