# Aplicacion de gestion base de datos

Proyecto final realizado con Java y Swing en 1DAW (CRUD). 

![z](/imagenes/z4.png)
![z12](/imagenes/z3.gif)

## Prueba
Se ha utilizado el programa Jar2exe Wizard para convertir el .jar en un archivo ejecutable.exe. Podéis descargar directamente el archivo .exe y ejecutarlo en Windows. 

 ``` IMPORTANTE: ``` la aplicación necesita realizar una conexión con una base de datos real. Para que funcione correctamente necesitáis tener un servicio de BDD instalado en el puerto :3306. 

Además, cuando iniciéis la APP ```debeis crear una sesion``` introduciendo la password y usuario que utilizais para conectaros en MYSQL. Por ejemplo: root, root.

## Funcionamiento
La aplicación crea una base de datos llamada ```tienda``` de forma automática que permite realizar la gestión de sus fabricantes y productos. 

## Freatures
La APP cuenta con un login y además se separa en 2 vistas adicionales a las que puedes ir accediendo mediante el boton ```SQL``` o ```CLIENTE```. 

### Login
El boton ```REGLAS``` permite la creacion de un nuevo usuario y contraseña. La interfaz en si, existe para validar los datos.

![l1](/imagenes/z1.png)

### Vista SQL
Permite realizar cualquier tipo de consulta sobre la tabla como si de MariaDB o MySQL Workbench se tratase. Además, también integra los comandos básicos como ```SHOW TABLES``` o ```DESC TABLE```

#### Muestra los fabricantes sin producto

```SQL
SELECT * 
FROM fabricante 
WHERE id_fabricante NOT IN (
	SELECT id_fabricante 
	FROM producto);
```

![b1](/imagenes/x1.png)


#### Muestra el producto mas caro de cada fabricante

```SQL
SELECT fabricante.nombre, producto.nombre, precio 
FROM fabricante INNER JOIN producto 
ON fabricante.id_fabricante = producto.id_fabricante
	WHERE precio = (
	SELECT MAX(precio)
	FROM producto
	WHERE id_fabricante = fabricante.id_fabricante);
``` 

![b2](/imagenes/x2.png)

#### Muestra el numero de productos que tiene cada fabricante

```SQL
SELECT id_fabricante, nombre, (
	SELECT COUNT(id_producto)
	FROM producto
	WHERE id_fabricante = fabricante.id_fabricante)
	AS "Numero de productos"
FROM fabricante
```

![b3](/imagenes/x3.png)

### Vista Cliente
Permite visualizar las 2 tablas de la BDD y realizar un CRUD sobre cualquiera de ellas.

#### DELETE
La aplicación permite eliminar cualquier producto o fabricante y acepta la multi-selección.

![bd5](/imagenes/bd5.gif)

#### UPDATE
El programa permite actualizar cualquier dato exceptuando las claves primarias y foráneas.

![bd6](/imagenes/bd6.gif)

#### INSERT
Es posible insertar cualquier producto siempre que se haga referencia a un fabricante que exista. No es necesario introducir la clave primaria porque la app organiza de forma automática e inserta el producto en la clave que toque. 

Es decir, si existen huecos entre claves ```[1,2,3,7]``` rellenará primero las claves ```[4,5,6]``` que hayan sido eliminadas previamente.

![bd7](/imagenes/bd7.gif)