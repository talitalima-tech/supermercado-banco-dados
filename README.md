# 📦 Trabalho – Banco de Dados (Supermercado)

## 📚 Descrição
Este repositório contém um trabalho acadêmico desenvolvido para a disciplina de Banco de Dados.  
O projeto tem como objetivo aplicar os conceitos de **modelagem de dados**, **criação de banco**, **inserção de dados** e **consultas SQL**, utilizando como estudo de caso um sistema de supermercado.

---

## 🧠 Conteúdos Desenvolvidos

- Modelagem Conceitual
- Modelagem Lógica
- Criação do banco de dados
- Inserção de dados para testes
- Consultas SQL (relatórios)
- Integração com Java utilizando JDBC

---

## 🗂️ Estrutura do Projeto

```

trabalho_banco_de_dados/
│
├── modelagem/
│   ├── modelagem-conceitual.png
│   └── modelagem-logica.png
│
├── sql/
│   ├── create-banco-supermercado.sql
│   ├── insercadados.sql
│   └── relatorios.sql
│
├── src/
│   └── Main.java
│
├── lib/
│   └── mysql-connector-j-9.5.0.jar
│
├── integrantes.txt
└── README.md

```

---

## 🧩 Modelagem de Dados

- **Modelagem Conceitual:**  
  Representa as entidades, atributos e relacionamentos do sistema, sem detalhes técnicos.

- **Modelagem Lógica:**  
  Define as tabelas, chaves primárias e chaves estrangeiras, servindo de base para a criação do banco.

---

## 🗄️ Banco de Dados

- O banco foi criado utilizando **MySQL**
- As tabelas foram definidas com:
  - Chaves primárias (PRIMARY KEY)
  - Chaves estrangeiras (FOREIGN KEY)
- Os dados foram inseridos utilizando comandos `INSERT INTO`
- As consultas utilizam `SELECT`, `JOIN` e filtros para gerar relatórios

---

## ☕ Integração com Java

- O projeto utiliza **Java** para conexão com o banco de dados
- A conexão é feita através do **JDBC**
- O driver utilizado é o **MySQL Connector/J**

O arquivo `Main.java` contém a classe principal do sistema, com o método `main`, responsável por iniciar a aplicação.

---

## 👥 Integrantes
- Talita Lima
- Matheus Duarte
- Amanda Laiane

---

## 🛠️ Tecnologias Utilizadas

- MySQL
- SQL
- Java
- JDBC
- VS Code
- Git e GitHub
```

## ▶️ Como Executar o Projeto

### 🔧 Pré-requisitos
- Java JDK instalado
- MySQL instalado e em execução
- Um editor ou terminal para executar Java

---

### 🗄️ Passo 1 – Criar o banco de dados
No MySQL, execute o script:

```sql
create-banco-supermercado.sql
````

Em seguida, execute o script de inserção de dados:

```sql
insercadados.sql
```

---

### ⚙️ Passo 2 – Configurar a conexão

No arquivo `Main.java`, ajuste as informações de conexão com o banco, se necessário:

* URL do banco
* Usuário
* Senha

---

### ☕ Passo 3 – Executar o projeto

No terminal, navegue até a pasta `src` e execute:

```bash
javac Main.java
java Main
```

Ou execute diretamente pelo editor (VS Code / IntelliJ).

---

### 📌 Observação

O projeto utiliza o driver **MySQL Connector/J** para realizar a conexão com o banco de dados.

```
