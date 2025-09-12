-- ===============================
-- Datos iniciales para ENFERMEDAD
-- ===============================
INSERT INTO ENFERMEDAD (ID, NOMBRE, NIVEL_RIESGO, OPERAR, TRATAMIENTO)
VALUES (RANDOM_UUID(), 'Diabetes tipo 2', 'Alto', FALSE, 'Metformina');

INSERT INTO ENFERMEDAD (ID, NOMBRE, NIVEL_RIESGO, OPERAR, TRATAMIENTO)
VALUES (RANDOM_UUID(), 'Hipertensión', 'Alto', FALSE, 'Betabloqueantes');

-- ============================
-- Datos iniciales para SINTOMA
-- ============================
INSERT INTO SINTOMA (ID, NOMBRE, DESCRIPCION)
VALUES (RANDOM_UUID(), 'Tos', 'Síntoma respiratorio típico');

INSERT INTO SINTOMA (ID, NOMBRE, DESCRIPCION)
VALUES (RANDOM_UUID(), 'Dolor de cabeza', 'Síntoma neurológico general');

-- ========================================
-- Relaciones iniciales ENFERMEDAD_SINTOMA
-- Usamos subselects para coger los UUIDs
-- ========================================
INSERT INTO ENFERMEDAD_SINTOMA (ENFERMEDAD_ID, SINTOMA_ID)
SELECT e.ID, s.ID
FROM ENFERMEDAD e, SINTOMA s
WHERE e.NOMBRE = 'Diabetes tipo 2' AND s.NOMBRE = 'Tos';

INSERT INTO ENFERMEDAD_SINTOMA (ENFERMEDAD_ID, SINTOMA_ID)
SELECT e.ID, s.ID
FROM ENFERMEDAD e, SINTOMA s
WHERE e.NOMBRE = 'Hipertensión' AND s.NOMBRE = 'Dolor de cabeza';
