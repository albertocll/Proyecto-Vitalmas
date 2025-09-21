# Scripts de Base de Datos - Vitalmas

Este directorio contiene los scripts SQL necesarios para inicializar y poblar la base de datos de **Vitalmas**.

## 📂 Archivos

1. **01_schema.sql**
   - Crea las tablas base (`especialidades`, `enfermedades`).
   - Incluye `DROP TABLE IF EXISTS` para resetear el esquema antes de recrearlo.

2. **03_seed_enfermedades.sql**
   - Inserta las **22 especialidades médicas** y las **240 enfermedades** extraídas del dataset original.
   - Debe ejecutarse después de `01_schema.sql`.

## 🚀 Uso en DBeaver

1. Conéctate a la base de datos `vitalmas` (usuario: `vitalmas`, contraseña: `vitalmas`).
2. Abre el archivo `01_schema.sql` en un editor SQL y ejecútalo (Ctrl+A → Ctrl+Enter).
3. Abre el archivo `03_seed_enfermedades.sql` y ejecútalo de la misma forma.
4. Verifica que las tablas tienen datos:
   ```sql
   SELECT COUNT(*) FROM especialidades; -- debería devolver 22
   SELECT COUNT(*) FROM enfermedades;   -- debería devolver 240
   ```

## 🧩 Notas
- Todos los scripts están envueltos en `BEGIN; ... COMMIT;` para asegurar transacciones atómicas.
- En caso de error, PostgreSQL revertirá los cambios y no quedará la base en estado inconsistente.
- Los scripts están preparados para usarse tanto en desarrollo como en pruebas iniciales de producción.