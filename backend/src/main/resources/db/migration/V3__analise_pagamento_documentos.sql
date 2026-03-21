CREATE TABLE IF NOT EXISTS regra_elegibilidade (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    cessao_id CHAR(36) NOT NULL,
    codigo_regra VARCHAR(80) NOT NULL,
    descricao VARCHAR(240) NOT NULL,
    resultado VARCHAR(20) NOT NULL,
    severidade VARCHAR(20) NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    avaliada_em TIMESTAMP NOT NULL,
    CONSTRAINT fk_regra_elegibilidade_cessao FOREIGN KEY (cessao_id) REFERENCES cessao (id)
);

CREATE TABLE IF NOT EXISTS contrato (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    cessao_id CHAR(36) NOT NULL,
    identificador_externo VARCHAR(120) NOT NULL,
    sacado_id VARCHAR(120) NOT NULL,
    valor_nominal DECIMAL(19, 2) NOT NULL,
    data_origem DATE NOT NULL,
    status_registro VARCHAR(20) NOT NULL,
    CONSTRAINT fk_contrato_cessao FOREIGN KEY (cessao_id) REFERENCES cessao (id),
    CONSTRAINT uq_contrato_cessao_identificador UNIQUE (cessao_id, identificador_externo)
);

CREATE TABLE IF NOT EXISTS parcela (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    contrato_id CHAR(36) NOT NULL,
    identificador_externo VARCHAR(120) NOT NULL,
    numero_parcela INT NOT NULL,
    vencimento DATE NOT NULL,
    valor DECIMAL(19, 2) NOT NULL,
    status_registro VARCHAR(20) NOT NULL,
    CONSTRAINT fk_parcela_contrato FOREIGN KEY (contrato_id) REFERENCES contrato (id),
    CONSTRAINT uq_parcela_contrato_numero UNIQUE (contrato_id, numero_parcela)
);

CREATE TABLE IF NOT EXISTS pagamento (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    cessao_id CHAR(36) NOT NULL,
    valor DECIMAL(19, 2) NOT NULL,
    status_pagamento VARCHAR(20) NOT NULL,
    autorizado_por VARCHAR(120) NULL,
    autorizado_em TIMESTAMP NULL,
    confirmado_em TIMESTAMP NULL,
    comprovante_referencia VARCHAR(240) NULL,
    CONSTRAINT fk_pagamento_cessao FOREIGN KEY (cessao_id) REFERENCES cessao (id)
);

CREATE TABLE IF NOT EXISTS lastro (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    cessao_id CHAR(36) NOT NULL,
    contrato_id CHAR(36) NULL,
    parcela_id CHAR(36) NULL,
    tipo_documento VARCHAR(80) NOT NULL,
    origem VARCHAR(120) NOT NULL,
    status_validacao VARCHAR(20) NOT NULL,
    recebido_em TIMESTAMP NOT NULL,
    validado_em TIMESTAMP NULL,
    CONSTRAINT fk_lastro_cessao FOREIGN KEY (cessao_id) REFERENCES cessao (id),
    CONSTRAINT fk_lastro_contrato FOREIGN KEY (contrato_id) REFERENCES contrato (id),
    CONSTRAINT fk_lastro_parcela FOREIGN KEY (parcela_id) REFERENCES parcela (id)
);

CREATE TABLE IF NOT EXISTS oferta_registradora (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    cessao_id CHAR(36) NOT NULL,
    tipo_operacao VARCHAR(20) NOT NULL,
    request_id VARCHAR(120) NOT NULL,
    http_status INT NULL,
    status_negocio VARCHAR(80) NULL,
    tentativa INT NOT NULL,
    request_payload_ref VARCHAR(240) NULL,
    response_payload_ref VARCHAR(240) NULL,
    executada_em TIMESTAMP NOT NULL,
    CONSTRAINT fk_oferta_registradora_cessao FOREIGN KEY (cessao_id) REFERENCES cessao (id)
);

CREATE TABLE IF NOT EXISTS termo_aceite (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    cessao_id CHAR(36) NOT NULL,
    versao INT NOT NULL,
    emitido_em TIMESTAMP NOT NULL,
    referencia_documento VARCHAR(240) NOT NULL,
    status_documento VARCHAR(20) NOT NULL,
    CONSTRAINT fk_termo_aceite_cessao FOREIGN KEY (cessao_id) REFERENCES cessao (id),
    CONSTRAINT uq_termo_cessao_versao UNIQUE (cessao_id, versao)
);
