package bdd;
import java.util.*;

import bbd_vista.JFrame_BDD;
public class BDD_tienda {

	private ArrayList <String> tienda = new ArrayList<String>();
	private String query = "";
	private static BDD_tienda miTienda;
	
	private BDD_tienda() {
		tienda.add("DROP DATABASE IF EXISTS tienda");
		tienda.add("CREATE DATABASE tienda;");
		tienda.add("USE `tienda`;");
		this.addFabricante();
		this.addDatosFabricante();
		this.addProducto();
		this.addDatosProducto();
	}
	
	//Patron Singleton
	//Crea un unico punto de acceso haciendo que todas las demas clases compartan la misma vista
	public static BDD_tienda getInstance() {
		if (miTienda == null) {
			miTienda = new BDD_tienda();
		}
		return miTienda;
	}
	
	public void addFabricante() {
		query = 	"CREATE TABLE fabricante ("
				+	"id_fabricante integer unsigned AUTO_INCREMENT,"
				+   "nombre varchar(70) NOT NULL,"
				+ 	"PRIMARY KEY (id_fabricante));";
		tienda.add(query);
	}
	
	public void addProducto() {
		query =     "CREATE TABLE producto ("
				+	"id_producto integer unsigned AUTO_INCREMENT,"
				+   "nombre varchar(70) NOT NULL,"
				+	"precio double unsigned NOT NULL,"
				+	"id_fabricante integer unsigned NOT NULL,"
				+ 	"PRIMARY KEY (id_producto),"
				+	"FOREIGN KEY (id_fabricante) REFERENCES fabricante (id_fabricante) ON DELETE CASCADE);";
		tienda.add(query);
	} 
	
	public void addDatosFabricante() {
		query = "INSERT INTO `fabricante` VALUES "
				+ "(1,'Asus'),"
				+ "(2,'Lenovo'),"
				+ "(3,'Hewlett-Packard'),"
				+ "(4,'Samsung'),"
				+ "(5,'Seagate'),"
				+ "(6,'Crucial'),"
				+ "(7,'Gigabyte'),"
				+ "(8,'Huawei'),"
				+ "(9,'Xiaomi');";
		tienda.add(query);
	}
	
	public void addDatosProducto() {
		query = "INSERT INTO `producto` VALUES "
				+ "(1,'Disco Duro SATA3 1TB',86.99,5),"
				+ "(2,'Memoria RAM DDR4 8GB',120,6),"
				+ "(3,'Disco SSD 1TB',150.99,4),"
				+ "(4,'GeForce GTX 1050Ti',185,7),"
				+ "(5,'GeForce GTX 1080 Xtreme',755,6),"
				+ "(6,'Monitor 24 LED Full HD',202,1),"
				+ "(7,'Monitor 27 LED Full HD',245.99,1),"
				+ "(8,'Portátil Yoga 520',559,2),"
				+ "(9,'Portátil Ideapd 320',444,2),"
				+ "(10,'Impresora HP Deskjet 3740',59.99,3),"
				+ "(11,'Impresora HP Laserjet Pro M26nw',180,3);";
		tienda.add(query);
	}
	
	//se usa en un for de la clase Conexion para crear la base de datos en mySQL
	public ArrayList<String> crearTienda() {	
		return tienda;
	}
	
}
