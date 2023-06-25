package bbd_vista;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import bdd.BDD_tienda;
import bdd.Conexion;
import bdd.Registro;


public class JControlador_BDD extends MouseAdapter implements ActionListener {

	private JFrame_BDD vista;
	private Conexion miBDD = new Conexion();
	Registro informacion = new Registro();
	private String [] botonesSQL = {"Cliente", "Consulta"};
	private String [] botonesCliente = {"Insert", "Delete", "Update","SQL","Fabricante","Producto"};
	
	public void iniciar() {
		vista = JFrame_BDD.getInstance();
		vista.setVisible(true);
		vista.ayuda.addActionListener(this);
		informacion.reasignar.addActionListener(this);
		vista.conectar.addActionListener(this);
		addBotonesSQL(botonesSQL);
		addBotonesCliente(botonesCliente);
	}
	
	public void addBotonesSQL(String [] botonesNombres) {
		JButton b;
		for (int i = 0; i < botonesNombres.length; i++) {
			b = vista.addBoton(botonesNombres[i]);
			b.addActionListener(this);
			b.addMouseListener(this);
			JFrame_BDD.laminaSQL.add(b);	
			
			if(botonesNombres[i].equals("Consulta")) {
						b.setBounds(200,510, 200, 40);
			}		
		}
		
		/*
		 * Este metodo evita realizar:
		 * 	vista.btn2.addActionListener(this);
		 * 	vista.btn1.addActionListener(this);
		 * 	vista.btn2.addMouseListener(this);
		 * 	vista.btn1.addMouseListener(this);
		 * 	y declarar 123123145 millones de atributos y metodos CSS para cada boton.
		 *  ....
		 */
	}
	
	/*
	 * Recorre el array de nombres y a cada boton le asigna el nombre del array[i]
	 * Ademas, asigna un ActionListener, un MouseListener y finalmente  auto-agrega a la lamina
	 * La distancia entre los botones se modifica con "setLocation" y la variable "dist-distancia"
	 */
	
	public void addBotonesCliente(String [] botonesNombres) {
		JButton b;
		int distancia = 0;
		int dist = 0;
		
		for (int i = 0; i < botonesNombres.length; i++) {
			b = vista.addBoton(botonesNombres[i]);
			b.addActionListener(this);
			b.addMouseListener(this);
			b.setLocation(50+distancia, 510);
			JFrame_BDD.laminaCliente.add(b);
			distancia += 175;
			
			if (botonesNombres[i].equals("Fabricante") || botonesNombres[i].equals("Producto")) {
				b.setBounds(140+dist, 300,260,50);
				dist += 250;
			}
		}
	}
	

	//En funcion del boton pulsado...
	public void actionPerformed(ActionEvent e) {
		
		if (!e.getActionCommand().equals("botonLogin"))
			Sonido.audio("sonido/boton.wav");
		
		switch (e.getActionCommand()) {
			case "help":
			 	informacion.iniciar();
			break;
			case "REASIGNAR":
				Conexion.setPassword(informacion.getNuevaPass());
				Conexion.setUsuario(informacion.getNuevoUsuario());
				JOptionPane.showMessageDialog(informacion, "CREDENCIALES CAMBIADOS CON EXITO");
				break;
			case "botonLogin":
				comprobarLogin();
				break;
			case "SQL":
				//vista.cardLayout.show(vista.laminaMain, "SQL"); //el CardLayout(laminaMain) llama a laminaSQL
				vista.cardLayout.previous(vista.laminaMain);
				break;
			case "Cliente":
				//vista.cardLayout.show(vista.laminaMain, "Cliente"); //el CardLayout(laminaMain) llama a laminaCliente
				vista.cardLayout.last(vista.laminaMain);
				break;
			case "Consulta":
				String consulta = vista.areaTexto.getText();
				if(consulta.equals("Introduce tu consulta aqui..."))
					consulta = "SELECT * FROM fabricante";
				miBDD.addConsulta(consulta);
				break;
			case "Fabricante":
				miBDD.addConsulta("SELECT * FROM fabricante");
				break;
			case "Producto":
				miBDD.addConsulta("SELECT * FROM producto");
				break;
			case "Insert":
				insertarFila();
				break;
			case "Delete":
				borrarFila();
				break;
			case "Update":
				actualizarFila();
				break;
			
		}
	}
	
	/*
	 * Metodo que hace de puente entre la capa del LOGIN y capa del CardLayout
	 * También se utiliza para lanzar un mensaje de error en caso de datos inválidos
	 */
	
	public void comprobarLogin() {
		if(datosCorrectos()) {
			miBDD.cargarBDD();
			Sonido.audio("sonido/Windows1.wav");
			vista.laminaLogin.setVisible(false);
			vista.add(vista.laminaMain);
			
			
		} else {
			JOptionPane.showMessageDialog(vista, "Credenciales incorrectas", "Error de conexion", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/*
	 * Comprueba si tanto usuario y password son correctos
	 * En caso de que lo sea, devuelve true. Sino, false.
	 */
	public boolean datosCorrectos() {
		boolean verificar = false;
		String pass = String.valueOf(vista.pass.getPassword());
		
		if (Conexion.getPassword().equals(pass) 
				&& Conexion.getUsuario().equals(vista.usuario.getText())) {
			verificar = true;
		}
		return verificar;
	}
	
	/*
	 * Se instancia el Scanner con el texto recibido por el TextField
	 * Se le asigna un nuevo delimitador "," para que separe las palabras del TextField por cada ','
	 * Se hace un while y se trocea el String con sc.next()
	 * Se pasa el ArrayList troceado por parametro al metodo miBDD.insert();
	 */
	
	private void insertarFila() {
		ArrayList <String> datos = new ArrayList <String> ();
		Scanner sc = new Scanner (vista.jTextUpdate.getText());
		sc.useDelimiter(",");
		while (sc.hasNext()) {
			datos.add(sc.next());
		}
		miBDD.insert(datos);
	}
	
	/*
	 * Para sacar el valor de la columna y no el valor de la fila seleccionada primero:
	 * --> Se guardan todas las filas seleccionadas en el array 'filasSeleccionadas[]'
	 * --> Se recorre ese mismo array y se pasa el valor de la fila al metodo "getValueAt"
	 * --> De esta forma obtienes el valor de la columna seleccionado las filas con el raton
	 */
	
	private void borrarFila() {
		//cada vez que borro un elemento, se crea un nuevo arrayList
		ArrayList <Integer> id = new ArrayList<Integer>();
		
		int[] filasSeleccionadas = JFrame_BDD.tablaCliente.getSelectedRows(); //metodo JTable q devuelve las filas seleccionadas en Array[]
		for (int i = 0; i < filasSeleccionadas.length; i++) {
			//con esta linea obtendremos el valor de la clave ID en lugar de obtener el numero de FILA
			id.add(Integer.parseInt(String.valueOf(
					JFrame_BDD.tablaCliente.getValueAt(filasSeleccionadas[i],0))));
		}
		//guarda el nombre de la primera columna
		String nombreID = JFrame_BDD.tablaCliente.getColumnName(0);
		//se pasa el nombre de la columna y el valorID al metodo delete de la clase Conexion para eliminarlo
		miBDD.delete(nombreID, id);
	}
	
	public void actualizarFila() {
		
		String valor = vista.jTextUpdate.getText(); //se guarda el valor de la caja de texto en el String 'valor'
		int columnaSeleccionada = JFrame_BDD.tablaCliente.getSelectedColumn(); //me permite saber que columna se ha seleccionado
		int filaSeleccionada = JFrame_BDD.tablaCliente.getSelectedRow(); //me permite saber que fila se ha seleccionado
		
		//Este if prohibe modificar las claves primarias
		if (JFrame_BDD.tablaCliente.getColumnName(columnaSeleccionada).equals("id_fabricante") ||
				JFrame_BDD.tablaCliente.getColumnName(columnaSeleccionada).equals("id_producto")) {
			JOptionPane.showMessageDialog(vista, "No puedes editar las claves primarias", "Error de actualizacion", JOptionPane.ERROR_MESSAGE);
		}
		
		else if (columnaSeleccionada > -1) {
		String nombreColumna = JFrame_BDD.tablaCliente.getColumnName(columnaSeleccionada); //se obtiene el nombre de la columna seleccionada
		String id = (""+JFrame_BDD.tablaCliente.getValueAt(filaSeleccionada,0)); //se obtiene la id
		
		miBDD.update(nombreColumna, valor, id); //se realizan los cambios en la bdd
		
		} else
			JOptionPane.showMessageDialog(vista, "No has seleccionado la fila", "Error de seleccion", JOptionPane.ERROR_MESSAGE);
	}
	
	/*
	 * Los 2 metodos mouse se utilizan para crear el efecto "Hover" del CSS.
	 * Cuando el raton pasa por encima de un boton(el primer metodo), cambia a un color
	 * Cuando sale (el segundo metodo), vuelve a su color original
	 */

	@Override
	public void mouseEntered(MouseEvent e) {
		JButton b = (JButton) e.getComponent();
		b.setBackground(new Color(255, 191, 0));
		b.setBackground(new Color(255, 191, 0));
		b.setForeground(Color.RED);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		JButton b = (JButton) e.getComponent();
		b.setBackground(new Color(31,4,153));
		b.setBackground(new Color(31,4,153));
		b.setForeground(Color.WHITE);
	}
	
	
	

	
}
