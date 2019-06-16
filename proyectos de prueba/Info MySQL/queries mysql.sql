Basic MySQL Tutorial: http://www.mysqltutorial.org/basic-mysql-tutorial.aspx

-------------------
## INSERTAR FILA ##
INSERT INTO table_name ( columna1, columna2, ... columnaN )
   VALUES
   ( valor1, valor1, ... valorN );

Ejemplos:
INSERT INTO administradores ( mail, contraseña )
   VALUES
   ('admin', '123');

INSERT INTO usuarios ( mail, contraseña, nombre, apellido, f_nac, creditos, nro_tarj, marca_tarj, titu_tarj, venc_tarj, cod_tarj, premium )
   VALUES
   ('pedro@asd.com', 123456, 'pedro', 'perez', '1500-01-02', 5, 888888888, 'VISA', 'PEDRO PEREZ', '2020-05-01', 234, 0);

INSERT INTO propiedad ( titulo, descripcion, pais, provincia, localidad, domicilio, monto )
   VALUES
   ('casa1', 'descr1', 'arg', 'bsas', 'lp', '1900 12/34', 888888888);
   
INSERT INTO reservas ( propiedad, usuario, tipo, fecha_inicio, estado, monto )
   VALUES
   ( 'prueba2', 'juan@mail.com', 'subasta', '2018-12-25', 'DISPONIBLE_SUBASTA', 12345);  
   

------------------------------------
## SELECCIONAR (SINTAXIS EN GENERAL) ##
SELECT column_1, column_2, ...
FROM table_1
[INNER | LEFT |RIGHT] JOIN table_2 ON conditions
WHERE conditions
GROUP BY column_1
HAVING group_conditions
ORDER BY column_1
LIMIT offset, length;

------------------------------------
## SELECCIONAR TODAS LAS FILAS Y COLUMNAS ##
SELECT * FROM table_name

Ejemplos:
SELECT * FROM propiedad

------------------------------------
## SELECCIONAR UN NUMERO DE FILAS ##
SELECT * FROM master_name ORDER BY column LIMIT offset, nro_de_filas;
SELECT * FROM master_name ORDER BY column ASC/DESC LIMIT offset, nro_de_filas;

Ejemplos:
SELECT * FROM propiedad ORDER BY foto1 LIMIT 0, 5; /* selecciona 5 filas arrancando desde la 1era */
SELECT * FROM propiedad ORDER BY foto1 DESC LIMIT 6, 5 /* selec. 5 filas desde la 6ta, en orden descendiente */

---------------------------------
## SELECCIONAR N FILAS AL AZAR ##
SELECT * FROM table_name ORDER BY RAND() LIMIT N; /* selecciona N filas al azar */

Ejemplos:
SELECT * FROM propiedad ORDER BY RAND() LIMIT 5;

---------------------------------
## SELECCIONAR FILAS SEGUN SU EL VALOR DE SUS CAMPOS##
SELECT * FROM table_name WHERE [condition]

Ejemplos:
SELECT * FROM propiedad WHERE localidad = 'la plata'
SELECT * FROM usuarios WHERE mail = '2'

---------------------------
## BORRAR FILA ##
DELETE FROM `table_name` WHERE [condition]

Ejemplos:
DELETE FROM propiedad WHERE monto = 3434 limit 1 /* elimina la 1er coincidencia */
DELETE FROM propiedad WHERE titulo = 'zzz' /* elimina todas las coincidencias */
DELETE FROM usuarios WHERE mail = '2' limit 1
DELETE FROM reservas WHERE propiedad = 'prueba1' limit 1

----------------------------
## BORRAR TABLA ##
DROP [TEMPORARY] TABLE [IF EXISTS] table_name [, table_name]
[RESTRICT | CASCADE]

Ejemplos:
DROP TABLE tabla; /* elimina tabla */
DROP TABLE IF EXISTS tabla1, tabla2; /* elimina tabla1 y tabla 2. no falla si alguna tabla no se encuentra  */

----------------------------
## BORRAR TODAS LAS FILAS ##
TRUNCATE TABLE tablename

Ejemplos:
TRUNCATE TABLE usuarios

---------------------------------
## MODIFICAR FILA ##
UPDATE [LOW_PRIORITY] [IGNORE] table_name 
SET 
    column_name1 = expr1,
    column_name2 = expr2,
[WHERE
    condition];

Ejemplos:
UPDATE usuarios
SET 
    nombre = 'juan' /* nuevo nombre */
WHERE mail = 'asd@mail.com'; /* buscar fila segun mail indicado */

UPDATE propiedad
SET 
    titulo = 'prueba1' /* nuevo nombre */
WHERE titulo = 'prueba2'; /* buscar fila segun mail indicado */

UPDATE reservas
SET 
    monto = 760.60
WHERE propiedad = 'prueba1';


---------------------------