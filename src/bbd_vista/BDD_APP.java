package bbd_vista;

public class BDD_APP {
	public static void main(String[] args) {
		
		// Delay para el splashArt
		try {
		    Thread.sleep(3000); 
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		
		JControlador_BDD c = new JControlador_BDD();
		c.iniciar();
		
		/*
		 * PARA QUE FUNCIONE NECESITAS:
		 * 		UNA CONEXION CON BDD
		 * 		MODIFICAR EL USUARIO Y PASS EN LA CLASE "bdd/Conexion"	
		 */
		
		
		/*
		 * LISTA DE CONSULTAS DE PRUEBAS:

* --> MUESTRA LOS FABRICANTRES SIN PRODUCTO

SELECT * 
FROM fabricante 
WHERE id_fabricante NOT IN (
	SELECT id_fabricante 
	FROM producto);

* --> MUESTRA EL PRODUCTO MAS CARO DE CADA FABRICANTE
	
SELECT fabricante.nombre, producto.nombre, precio 
FROM fabricante INNER JOIN producto 
ON fabricante.id_fabricante = producto.id_fabricante
	WHERE precio = (
	SELECT MAX(precio)
	FROM producto
	WHERE id_fabricante = fabricante.id_fabricante); 	
 
* --> MUESTRA EL PRODUCTO MAS CARO

SELECT nombre, precio 
FROM producto
WHERE precio = (
	SELECT MAX(precio)
	FROM producto)

* --> MUESTRA EL NUMERO DE PRODUCTOS QUE TIENE CADA FABRICANTE

SELECT id_fabricante, nombre, (
	SELECT COUNT(id_producto)
	FROM producto
	WHERE id_fabricante = fabricante.id_fabricante)
	AS "Numero de productos"
FROM fabricante	
		   		
*/
	}
}
