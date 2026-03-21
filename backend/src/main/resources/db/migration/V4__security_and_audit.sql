CREATE TABLE IF NOT EXISTS perfil_acesso (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    nome VARCHAR(80) NOT NULL,
    descricao VARCHAR(240) NOT NULL,
    CONSTRAINT uq_perfil_acesso_nome UNIQUE (nome)
);

CREATE TABLE IF NOT EXISTS usuario (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    username VARCHAR(120) NOT NULL,
    nome_exibicao VARCHAR(160) NOT NULL,
    ativo BOOLEAN NOT NULL,
    CONSTRAINT uq_usuario_username UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS usuario_perfil_acesso (
    usuario_id CHAR(36) NOT NULL,
    perfil_acesso_id CHAR(36) NOT NULL,
    PRIMARY KEY (usuario_id, perfil_acesso_id),
    CONSTRAINT fk_usuario_perfil_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_usuario_perfil_perfil FOREIGN KEY (perfil_acesso_id) REFERENCES perfil_acesso (id)
);

CREATE TABLE IF NOT EXISTS permissao_etapa (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    perfil_acesso_id CHAR(36) NOT NULL,
    nome_etapa VARCHAR(80) NOT NULL,
    CONSTRAINT fk_permissao_etapa_perfil FOREIGN KEY (perfil_acesso_id) REFERENCES perfil_acesso (id),
    CONSTRAINT uq_permissao_etapa UNIQUE (perfil_acesso_id, nome_etapa)
);

CREATE TABLE IF NOT EXISTS evento_auditoria (
    id CHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    cessao_id CHAR(36) NOT NULL,
    etapa_id CHAR(36) NULL,
    ator_id VARCHAR(120) NOT NULL,
    perfil VARCHAR(80) NOT NULL,
    tipo_evento VARCHAR(120) NOT NULL,
    resultado VARCHAR(80) NOT NULL,
    correlation_id VARCHAR(120) NOT NULL,
    ocorrido_em TIMESTAMP NOT NULL,
    detalhe_ref TEXT NULL,
    CONSTRAINT fk_evento_auditoria_cessao FOREIGN KEY (cessao_id) REFERENCES cessao (id),
    CONSTRAINT fk_evento_auditoria_etapa FOREIGN KEY (etapa_id) REFERENCES etapa_cessao (id)
);
