CREATE TABLE IF NOT EXISTS cessao_read_model (
    cessao_business_key VARCHAR(120) PRIMARY KEY,
    status_atual VARCHAR(40) NOT NULL,
    etapa_atual VARCHAR(80) NOT NULL,
    pendencias JSON NOT NULL,
    ultimo_evento VARCHAR(120) NULL,
    ultima_atualizacao TIMESTAMP NOT NULL,
    resumo_financeiro JSON NOT NULL,
    resumo_documental JSON NOT NULL,
    resumo_auditoria JSON NOT NULL,
    indicadores_tarefas_humanas JSON NOT NULL
);
