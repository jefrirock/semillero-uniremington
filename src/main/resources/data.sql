-- Asignar orden a docentes existentes (ejecutar al iniciar)
-- Modifica estos valores según los IDs reales de tus docentes

UPDATE docentes SET orden = 1 WHERE nombre LIKE '%Adriana%';
UPDATE docentes SET orden = 2 WHERE nombre LIKE '%Bryan%';
UPDATE docentes SET orden = 3 WHERE nombre LIKE '%Edier%' OR nombre LIKE '%Aviles%';
UPDATE docentes SET orden = 4 WHERE nombre LIKE '%Emmanuel%' OR nombre LIKE '%Diaz%';
UPDATE docentes SET orden = 5 WHERE nombre LIKE '%Jose Ignacio%' OR nombre LIKE '%Negrete%';