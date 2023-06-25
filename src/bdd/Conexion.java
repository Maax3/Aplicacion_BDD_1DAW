package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import bbd_vista.JFrame_BDD;

public class Conexion {
	// atributos de conexion
	private static String USUARIO = "root"; //RECUERDA MODIFICAR ESTO SI TU BDD TIENE OTRO USUARIO
	private static String PASSWORD = "12345"; //RECUERDA MODIFICAR ESTO SI TU BDD TIENE OTRA CLAVE
	private static final String URL = "jdbc:mysql://localhost:3306";
	private Connection conexion = null;
	private Statement st = null;
	// atributos de contenido
	private ResultSet tablaVirtual; // se crea la tabla virtual para contener las consultas
	private BDD_tienda t = BDD_tienda.getInstance(); // se inician los metodos del constructor BDD_tienda
	private ArrayList<String> miTienda = t.crearTienda(); // se recupera los CREATE table de la clase BDD_tienda en	formato ArrayList
	public DefaultTableModel tablaSQL = new DefaultTableModel(); // se establece un modelo de tabla a rellenar
	private static int aux = 0;
	//listas para guardar id's borradas
	private ArrayList<Integer> pKeyFabricante = new ArrayList<Integer>();
	private ArrayList<Integer> pKeyProducto = new ArrayList<Integer>();

	public Conexion() {

		this.cargarBDD();
		
	}

	// metodo que establece la conexion
	public void cargarBDD() {

		try {
			// los 3 pilares para establecer una conexion:
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
			st = conexion.createStatement();

			// se recorre el ArrayList creando la base de datos
			for (int i = 0; i < miTienda.size(); i++) {
				st.executeUpdate(miTienda.get(i));
			}

		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, err.getMessage(), "Error en la conexion", JOptionPane.ERROR_MESSAGE);
		}

	}

	/*
	 * Procesa la consulta del JTextArea y devuelve los datos que posteriormente se
	 * guardan en una tabla
	 */

	public void addConsulta(String consulta) {
		
		//se inicia el modelo de la tabla justo despues de usar el boton 'consulta'
		//de esta forma el fondo permanece negro hasta realizar la primera consulta
		//el if sirve para no volver a inicializar el modelo(DefaultTableModel) una & otra vez y evitar problemas de actualizacion de datos
		if (aux == 0) {
			JFrame_BDD.addJTable(tablaSQL);  // se inicia la tabla con los estilos CSS de la vista
			aux++;
		}
		
		try {
			tablaVirtual = st.executeQuery(consulta); // tabla virtual = instancia de ResultSet
			Vector<String> contenidoTabla; // guarda el contenido de la consulta
			Vector<String> nombres = getNombreColumnas(); // devuelve el nombre de las columnas
			tablaSQL.setColumnIdentifiers(nombres); //se inicia la tabla con los nombres de las columnas del vector
			tablaSQL.setRowCount(0); // establece todas las filas a 0, es decir, borra todo el contenido

			// mientras haya filas por recorrer...
			while (tablaVirtual.next()) {

				contenidoTabla = new Vector<String>(); // se crea una nueva instancia del vector

				for (int i = 1; i < tablaVirtual.getMetaData().getColumnCount() + 1; i++) {
					contenidoTabla.add(tablaVirtual.getString(i)); // se guarda fila entera en el vector
				}
				tablaSQL.addRow(contenidoTabla); // se inserta el vector en la tabla
			}

			rellenarEspaciosVacios(tablaSQL);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error en consulta", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * Pinta filas vacias y estas adoptan el estilo de la tabla (fondo negro)
	 */

	private void rellenarEspaciosVacios(DefaultTableModel tablaSQL) {
		while (tablaSQL.getRowCount() < 16) {
			Vector<String> relleno = new Vector<String>();
			relleno.add("");
			tablaSQL.addRow(relleno);
		}
	}

	/*
	 * Obtiene el nombre de las columnas para cada consulta
	 */

	private Vector<String> getNombreColumnas() {
		Vector<String> lista = new Vector<String>();

		try {
			// se le suma +1 porque las columnas empiezan en 1 en lugar de 0
			for (int i = 1; i < tablaVirtual.getMetaData().getColumnCount() + 1; i++) {
				lista.add(tablaVirtual.getMetaData().getColumnName(i));
			}

		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, err.getMessage(), "Error en el nombre de las columnas",
					JOptionPane.ERROR_MESSAGE);
		}

		return lista;
	}
	
	
	/*
	 * Metodo insert
	 * Se escoge una de las 2 tablas y se insertan los datos.
	 * Si existen claves borradas estaran guardadas en las listas pKeyProducto/Fabricante
	 * Sino, se pondra la AUTO_INCREMENT por defecto 
	 */

	public void insert(ArrayList<String> datos) {
		String consulta = "";
		
		
		try {
		
		//la primera palabra del arraylist {producto,"","",""}
		switch(datos.get(0)) {
		
		case "producto":
		
			consulta = "INSERT INTO " + datos.get(0) + " (id_producto, nombre, precio, id_fabricante) "
					+ "VALUES ('" + addPrimaryKey(pKeyProducto) + "', '" + datos.get(1) + "', '" + datos.get(2) + "', '" + datos.get(3) + "')";
			st.executeUpdate(consulta);
			eliminarElemento(pKeyProducto);
			addConsulta("SELECT * FROM producto");
			break;
			
		case "fabricante":
			
			consulta = "INSERT INTO " + datos.get(0) + " VALUES('" + addPrimaryKey(pKeyFabricante) + "', '" + datos.get(1) + "')";
			st.executeUpdate(consulta);
			eliminarElemento(pKeyFabricante);
			addConsulta("SELECT * FROM fabricante");
			break;
		default:
			JOptionPane.showMessageDialog(null, "Introduce: nombreTabla y los datos separados por ',' "
				+"\n"+ "	Ejemplo: fabricante,Apple"
				+"\n"+ "Omite la clave primaria", "Error en insertar datos",
					JOptionPane.ERROR_MESSAGE);
			}
		}catch (Exception e) {
			
			JOptionPane.showMessageDialog(null, "Introduce: nombreTabla y los datos separados por ',' "
					+"\n"+ "	Ejemplo: fabricante,Apple"
					+"\n"+ "Omite la clave primaria", "Error en insertar datos",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	
	/*
	 * Metodo que restaura las claves borradas
	 * Utiliza las listas pKeyFabricante/Producto que contienen sus respectivas id's.
	 * Si la lista esta vacia, la clave primaria pasa a ser "0" 
	 * Si la clave es "0", el AUTO_INCREMENT aumentará el último valor en 1 mas de forma automatica. 
	 */
	
	public int addPrimaryKey(ArrayList<Integer> id) {
		int pk = 0;
		Collections.sort(id); // <-- la razon por la que todos los ArrayList son Integer
		
		if (id.size() > 0) {
			pk = id.get(0);
		}
		return pk;
	}
	
	/*
	 * Borra el primer elemento de las listas pKeyProducto/Fabricante
	 * Se realiza en el metodo insert despues de que se hayan validado todos los datos
	 * y asi se evita el borrado prematuro de las claves en el metodo addPrimaryKey()
	 * que puede ocurrir si se insertan mal los datos en 'tiempo de consulta'
	 */

	public void eliminarElemento(ArrayList<Integer> id) {
		if (id.size() > 0) {
			id.remove(0);
		}
	}
		
	
/*
 * Segun el id_tabla (la primera columna) se sabe si quieres borrar de producto o de fabricante.
 * El metodo permite borrar una fila seleccionada o varias. Para borrar varias selecciona multiples filas a la vez.
 * El arrayList "id" contiene todos los id's que se quieren utilizar para borrar. 
 * 
 * Ademas, esas id's se guardan en sus respectivas listas pKey para poder usarse en el metodo insert() 
 * y de este modo, rellenar los "huecos" de las claves primarias.
 * 
 * Por ultimo, para rellenar los huecos que puedan crearse al borrar productos "referenciados" existen: 
 * La "consultaSelect" y el ResultSet "claves" 
 * Estos realizan una busqueda de todos los id's que pertenzcan al producto que vaya a ser borrado y, 
 * una vez hecha la consulta, se recorre "claves" para agregar esos ids en "pKeyProducto".
 * De esta forma, no se pierde ninguna clave.
 */
	

	public void delete(String id_tabla, ArrayList<Integer> id) {
		
		//consulta para buscar las claves referenciadas,se le asigna la clave con el (i) del for.
		String consultaSelect = "SELECT id_producto "
							  + "FROM producto INNER JOIN fabricante "
							  + "ON fabricante.id_fabricante = producto.id_fabricante "
							  + "WHERE producto.id_fabricante = ";
		String consulta = "";
		ResultSet claves;

		try {
			
			if (id_tabla.equals("id_producto")) {

				for (int i = 0; i < id.size(); i++) {
					consulta = "DELETE FROM producto WHERE " + id_tabla + " = '" + id.get(i) + "'";
					st.executeUpdate(consulta);
					pKeyProducto.add(id.get(i));
				}
					addConsulta("SELECT * FROM producto");

			} else {
				//el "id" es la lista de las claves "seleccionadas" de la tabla
				for (int i = 0; i < id.size(); i++) {
					claves = st.executeQuery(consultaSelect+id.get(i)+";"); //se lanza la consulta para buscar claves
						while(claves.next()) {
							pKeyProducto.add(claves.getInt("id_producto")); //se guardan las claves que se vayan a eliminar
						}
					//se borra el fabricante y las claves referenciadas
					consulta = "DELETE FROM fabricante WHERE " + id_tabla + " = '" + id.get(i) + "'";
					st.executeUpdate(consulta);
					pKeyFabricante.add(id.get(i));
				}
					addConsulta("SELECT * FROM fabricante");
			}
			
		} catch (SQLException ex) {
		
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error en borrado de datos",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/*
	 * El metodo recibe lo necesario por parametros 
	 * Realiza un SELECT * FROM para dar el efecto de 'actualizado' en tiempor real
	 */
	
	public void update(String nombreColumna, String textoModificado, String id) {
		String consulta = "";

		try {
			//if tramposo 
			if (JFrame_BDD.tablaSQL.getColumnCount() < 3) {
				consulta = "UPDATE fabricante SET " + nombreColumna + " = '" + textoModificado + "' WHERE id_fabricante = '" + id + "'";
				st.executeUpdate(consulta);
				addConsulta("SELECT * FROM fabricante");
		} else {
				consulta = "UPDATE producto SET " + nombreColumna + " = '" + textoModificado + "' WHERE id_producto = '" + id + "'";
				st.executeUpdate(consulta);
				addConsulta("SELECT * FROM producto");
		}
            
         } catch (SQLException ex) {
        	 JOptionPane.showMessageDialog(null, ex.getMessage(), "Error en el actualizado de datos",
 					JOptionPane.ERROR_MESSAGE);
         }
	}

	/*
	 * Metodos basicos para login
	 */
	
	public static String getPassword() {
		return PASSWORD;
	}

	public static String getUsuario() {
		return USUARIO;
	}
	
	public static void setPassword(String pass) {
		PASSWORD = pass;
	}

	public static void setUsuario(String usu) {
		USUARIO = usu;
	}


}
