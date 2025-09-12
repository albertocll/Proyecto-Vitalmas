-- ===============================
-- Limpiar tablas en orden correcto
-- ===============================
DELETE FROM ENFERMEDAD_SINTOMA;
DELETE FROM ENFERMEDAD;
DELETE FROM SINTOMA;
DELETE FROM USUARIO;

-- ===============================
-- Datos iniciales para ENFERMEDAD
--  OJO: columna correcta es OPERAR (no REQUIERE_OPERACION)
-- ===============================
INSERT INTO ENFERMEDAD (ID, NOMBRE, NIVEL_RIESGO, OPERAR, TRATAMIENTO)
VALUES ('11111111-1111-1111-1111-111111111111', 'Diabetes tipo 2', 'ALTO', FALSE, 'Metformina');

INSERT INTO ENFERMEDAD (ID, NOMBRE, NIVEL_RIESGO, OPERAR, TRATAMIENTO)
VALUES ('22222222-2222-2222-2222-222222222222', 'Hipertensión', 'ALTO', FALSE, 'Betabloqueantes');

-- ============================
-- Datos iniciales para SINTOMA
-- ============================
INSERT INTO SINTOMA (ID, NOMBRE, DESCRIPCION)
VALUES ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'Tos', 'Síntoma respiratorio típico');

INSERT INTO SINTOMA (ID, NOMBRE, DESCRIPCION)
VALUES ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2', 'Dolor de cabeza', 'Síntoma neurológico general');

-- ========================================
-- Relaciones iniciales ENFERMEDAD_SINTOMA
-- ========================================
INSERT INTO ENFERMEDAD_SINTOMA (ENFERMEDAD_ID, SINTOMA_ID)
VALUES ('11111111-1111-1111-1111-111111111111', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1');

INSERT INTO ENFERMEDAD_SINTOMA (ENFERMEDAD_ID, SINTOMA_ID)
VALUES ('22222222-2222-2222-2222-222222222222', 'bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2');

-- ============================
-- Datos iniciales para USUARIO
-- Nota: esto no afecta a Basic Auth en memoria; es para tener la fila en BD.
-- ============================
INSERT INTO USUARIO (ID, USUARIO, NOMBRE, PASSWORD, ROL, ENABLED)
VALUES ('33333333-3333-3333-3333-333333333333',
        'house',
        'Dr. Gregory House',
        '{noop}house123',
        'MEDICO',
        TRUE);
