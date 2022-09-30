CREATE TABLE cliente (
	id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(60) NOT NULL,
    email VARCHAR(30) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    
    primary key (id)
);