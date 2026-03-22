ALTER TABLE usuario
    ADD COLUMN IF NOT EXISTS email VARCHAR(160);

ALTER TABLE usuario
    ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255);

UPDATE usuario
SET email = CASE username
        WHEN 'operador' THEN 'operador@fidc.local'
        WHEN 'analista' THEN 'analista@fidc.local'
        WHEN 'aprovador' THEN 'aprovador@fidc.local'
        WHEN 'auditor' THEN 'auditor@fidc.local'
        WHEN 'integracao' THEN 'integracao@fidc.local'
        ELSE username || '@fidc.local'
    END
WHERE email IS NULL;

UPDATE usuario
SET password_hash = CASE username
        WHEN 'operador' THEN '{noop}operador123'
        WHEN 'analista' THEN '{noop}analista123'
        WHEN 'aprovador' THEN '{noop}aprovador123'
        WHEN 'auditor' THEN '{noop}auditor123'
        WHEN 'integracao' THEN '{noop}integracao123'
        ELSE '{noop}changeme123'
    END
WHERE password_hash IS NULL;

ALTER TABLE usuario
    ALTER COLUMN email SET NOT NULL;

ALTER TABLE usuario
    ALTER COLUMN password_hash SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uq_usuario_email'
    ) THEN
        ALTER TABLE usuario
            ADD CONSTRAINT uq_usuario_email UNIQUE (email);
    END IF;
END $$;
