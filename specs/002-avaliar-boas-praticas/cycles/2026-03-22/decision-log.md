# Decision Log - 2026-03-22

| ID | Decisao | Justificativa |
|----|---------|---------------|
| D-001 | Tratar F-001 como bloqueio alto, nao como melhoria cosmetica | O lint faz parte do gate de qualidade e hoje nao e repetivel |
| D-002 | Tratar F-002 como bloqueio alto controlavel | A divergencia de baseline de autenticacao afeta entendimento operacional e seguranca |
| D-003 | Tratar F-003 e F-004 como medias | Sao desvios relevantes de governanca, mas sem bloquear execucao imediata do produto |
| D-004 | Tratar F-005 como medio e nao alto | O build backend passa, mas a profundidade de integracao fica limitada no host atual |
