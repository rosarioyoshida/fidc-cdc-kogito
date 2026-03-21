CREATE TABLE IF NOT EXISTS cessao (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    business_key VARCHAR(120) NOT NULL,
    status VARCHAR(40) NOT NULL,
    workflow_instance_id VARCHAR(120) NULL,
    cedente_id VARCHAR(120) NOT NULL,
    cessionaria_id VARCHAR(120) NOT NULL,
    valor_calculado DECIMAL(19, 2) NULL,
    valor_aprovado DECIMAL(19, 2) NULL,
    data_importacao TIMESTAMP NOT NULL,
    data_encerramento TIMESTAMP NULL,
    CONSTRAINT uq_cessao_business_key UNIQUE (business_key)
);

CREATE TABLE IF NOT EXISTS etapa_cessao (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    cessao_id CHAR(36) NOT NULL,
    nome_etapa VARCHAR(80) NOT NULL,
    status_etapa VARCHAR(40) NOT NULL,
    ordem INT NOT NULL,
    responsavel_id VARCHAR(120) NULL,
    inicio_em TIMESTAMP NULL,
    concluida_em TIMESTAMP NULL,
    resultado VARCHAR(120) NULL,
    justificativa VARCHAR(500) NULL,
    CONSTRAINT fk_etapa_cessao FOREIGN KEY (cessao_id) REFERENCES cessao (id),
    CONSTRAINT uq_etapa_cessao_ordem UNIQUE (cessao_id, ordem),
    CONSTRAINT uq_etapa_cessao_nome UNIQUE (cessao_id, nome_etapa)
);
