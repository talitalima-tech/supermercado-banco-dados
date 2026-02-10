-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema supermercado
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `supermercado` DEFAULT CHARACTER SET utf8mb4 ;
USE `supermercado` ;

-- -----------------------------------------------------
-- Table `cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cliente` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `cpf` VARCHAR(11) NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `funcao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `funcao` (
  `id_funcao` INT NOT NULL AUTO_INCREMENT,
  `nome_funcao` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_funcao`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `funcionario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `funcionario` (
  `id_funcionario` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `funcao_id_funcao` INT NOT NULL,
  PRIMARY KEY (`id_funcionario`),
  INDEX `fk_funcionario_funcao1_idx` (`funcao_id_funcao` ASC),
  CONSTRAINT `fk_funcionario_funcao1`
    FOREIGN KEY (`funcao_id_funcao`)
    REFERENCES `funcao` (`id_funcao`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `caixa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `caixa` (
  `id_caixa` INT NOT NULL AUTO_INCREMENT,
  `numero` INT NOT NULL,
  PRIMARY KEY (`id_caixa`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `produto` (
  `id_produto` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `descricao` VARCHAR(100) NULL,
  `preco` DECIMAL(10,2) NOT NULL,
  `estoque` INT NOT NULL,
  `estoque_minimo` INT NOT NULL,
  PRIMARY KEY (`id_produto`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `venda` (
  `id_venda` INT NOT NULL AUTO_INCREMENT,
  `data_hora` DATETIME NOT NULL,
  `total` DECIMAL(10,2) NULL,
  `cliente_id_cliente` INT NOT NULL,
  `funcionario_id_funcionario` INT NOT NULL,
  `caixa_id_caixa` INT NOT NULL,
  PRIMARY KEY (`id_venda`),
  INDEX `fk_venda_cliente1_idx` (`cliente_id_cliente` ASC),
  INDEX `fk_venda_funcionario1_idx` (`funcionario_id_funcionario` ASC),
  INDEX `fk_venda_caixa1_idx` (`caixa_id_caixa` ASC),
  CONSTRAINT `fk_venda_cliente1`
    FOREIGN KEY (`cliente_id_cliente`)
    REFERENCES `cliente` (`id_cliente`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_venda_funcionario1`
    FOREIGN KEY (`funcionario_id_funcionario`)
    REFERENCES `funcionario` (`id_funcionario`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_venda_caixa1`
    FOREIGN KEY (`caixa_id_caixa`)
    REFERENCES `caixa` (`id_caixa`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `pagamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pagamento` (
  `id_pagamento` INT NOT NULL AUTO_INCREMENT,
  `tipo_pagamento` VARCHAR(45) NOT NULL,
  `data_hora_pagamento` DATETIME NOT NULL,
  `valor_pago` DECIMAL(10,2) NOT NULL,
  `venda_id_venda` INT NOT NULL,
  PRIMARY KEY (`id_pagamento`),
  INDEX `fk_pagamento_venda1_idx` (`venda_id_venda` ASC),
  CONSTRAINT `fk_pagamento_venda1`
    FOREIGN KEY (`venda_id_venda`)
    REFERENCES `venda` (`id_venda`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `item_venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `item_venda` (
  `venda_id_venda` INT NOT NULL,
  `produto_id_produto` INT NOT NULL,
  `quantidade` INT NOT NULL,
  `preco_unitario` DECIMAL(10,2) NULL,
  PRIMARY KEY (`venda_id_venda`, `produto_id_produto`),
  INDEX `fk_item_venda_produto_idx` (`produto_id_produto` ASC),
  INDEX `fk_item_venda_venda_idx` (`venda_id_venda` ASC),
  CONSTRAINT `fk_item_venda_venda`
    FOREIGN KEY (`venda_id_venda`)
    REFERENCES `venda` (`id_venda`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_item_venda_produto`
    FOREIGN KEY (`produto_id_produto`)
    REFERENCES `produto` (`id_produto`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
