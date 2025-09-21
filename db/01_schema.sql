-- 01_schema.sql - Crear tablas base de Vitalmas
BEGIN;
DROP TABLE IF EXISTS enfermedades CASCADE;
DROP TABLE IF EXISTS especialidades CASCADE;
CREATE TABLE especialidades (
        id UUID PRIMARY KEY,
        nombre TEXT NOT NULL UNIQUE
    );
CREATE TABLE enfermedades (
        id UUID PRIMARY KEY,
        nombre VARCHAR(255) NOT NULL,
        nivel_riesgo VARCHAR(50),
        operar BOOLEAN,
        tratamiento TEXT,
        id_especialidad UUID NOT NULL REFERENCES especialidades(id)
    );
COMMIT;