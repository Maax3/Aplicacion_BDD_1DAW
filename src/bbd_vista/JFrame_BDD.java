package bbd_vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

@SuppressWarnings("serial")
public class JFrame_BDD extends JFrame {
	
	private static JFrame_BDD miVista;
	// atributos de la primera lamina (Login)
	public JPanel laminaLogin;
	public JTextField usuario;
	public JPasswordField pass;
	public JButton conectar;
	public JButton ayuda;
	private JLabel iconos;
	private JLabel titulo;
	
	// atributos del contenedor (CardLayout)
	public JPanel laminaMain;
	public CardLayout cardLayout = new CardLayout(); // Layout para contener N paneles
	public static JPanel laminaCliente;
	public static JPanel laminaSQL;
	// atributos de LaminaSQL
	public JTextArea areaTexto;
	// atributos de LaminaCliente
	public JTextField jTextUpdate;
	//Tablas
	public static JTable tablaSQL;
	public static JTable tablaCliente;
	// CSS
	private static Color negro = new Color(0, 34, 46);
	private Color verdeOscuro = new Color(71, 100, 104);
	private static Color azul = new Color(31, 4, 153);
	private LineBorder bordeVerde = new LineBorder(Color.GREEN, 2);
	private static Font fuenteMono = new Font(Font.MONOSPACED, Font.BOLD, 22);

	public JFrame_BDD() {
		this.setSize(785, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Tienda");
		this.setDefaultCloseOperation(JFrame_BDD.EXIT_ON_CLOSE);
		addLaminaSQL();
		addLaminaCliente();
		addLaminaLogin();
		addLaminaMain();
	}
	
	//Patron Singleton
			//Crea un unico punto de acceso haciendo que todas las demas clases compartan la misma vista
			public static JFrame_BDD getInstance() {
				if (miVista == null) {
						miVista = new JFrame_BDD();
				}
				return miVista;
			}
			
	public void addBotonAyuda() {
		ayuda = new JButton();
		ayuda.setActionCommand("help");
		ayuda.setBounds(640,450,100,100);
		ayuda.setBackground(null); //transparenta el boton
		ayuda.setIcon(new ImageIcon("imagenes/info1.png"));
		ayuda.setFocusable(false);
		ayuda.setBorder(null);
		ayuda.setContentAreaFilled(false);
		ayuda.setHorizontalAlignment(JRadioButton.CENTER);
		ayuda.setVerticalAlignment(JRadioButton.CENTER);
		laminaLogin.add(ayuda);
			}

	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	// :::::::::::::::::::::::::::::::::METODOS DE LA PRIMERA LAMINA::::::::::::::::::::::::::::::::::::::::::::
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	// contenedor para el login de usuario/pass
	public void addLaminaLogin() {
		laminaLogin = new JPanel();
		laminaLogin.setLayout(null);
		laminaLogin.setBackground(negro);
		laminaLogin.setBorder(bordeVerde);
		iconos = addIcono(225, 190, "imagenes/user.png"); // se instancia un nuevo icono
		laminaLogin.add(iconos);
		iconos = addIcono(225, 290, "imagenes/pass.png"); // se reinstancia para añadir el icono del candado
		laminaLogin.add(iconos);
		addJTextUsuario();
		addJPassUsuario();
		addBotonLogin();
		titulo = addLabel(225, 70, "INTRODUCE TUS DATOS:");
		laminaLogin.add(titulo);
		addBotonAyuda();
		this.add(laminaLogin); //se añade a la ventana (JFrame)
	}

	// Etiqueta del usuario
	public void addJTextUsuario() {
		usuario = new JTextField();
		usuario.setBounds(255, 200, 250, 50);
		usuario.setForeground(Color.WHITE);
		usuario.setBorder(bordeVerde);
		usuario.setFont(fuenteMono);
		usuario.setHorizontalAlignment(JTextField.CENTER); // centra el texto
		usuario.setOpaque(false);  //vuelve a la etiqueta transparente
		laminaLogin.add(usuario); //se añade al JPanel (laminaLogin)
	}

	// Etiqueta de la password
	public void addJPassUsuario() {
		pass = new JPasswordField();
		pass.setBounds(255, 300, 250, 50);
		pass.setForeground(Color.WHITE);
		pass.setBorder(bordeVerde);
		pass.setFont(fuenteMono);
		pass.setHorizontalAlignment(JTextField.CENTER); // centra el texto
		pass.setOpaque(false);   //vuelve a la etiqueta transparente
		laminaLogin.add(pass);  //se añade al JPanel (laminaLogin)
	}

	// Define el estilo del boton del icono 'Login'
	public void addBotonLogin() {
		conectar = new JButton();
		conectar.setActionCommand("botonLogin");
		conectar.setBounds(285, 400, 200, 80);
		conectar.setBorder(null); // quita el borde
		conectar.setContentAreaFilled(false); // hace el boton transparente
		conectar.setIcon(new ImageIcon("imagenes/login3.png"));
		conectar.setFocusable(false);
		laminaLogin.add(conectar);  //se añade al JPanel (laminaLogin)
	}

	// Crea nuevos iconos
	public JLabel addIcono(int posX, int posY, String imagen) {
		JLabel icono = new JLabel();
		icono.setIcon(new ImageIcon(imagen));
		icono.setBounds(posX, posY, 70, 70);
		return icono;
	}
	//Texto "Introduce tus datos: "
	public JLabel addLabel(int posX, int posY, String texto) {
		JLabel etiqueta = new JLabel();
		etiqueta.setText(texto);
		etiqueta.setOpaque(true);
		etiqueta.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
		etiqueta.setBackground(verdeOscuro);
		etiqueta.setForeground(Color.GREEN);
		etiqueta.setBounds(posX, posY, 325, 50);
		etiqueta.setHorizontalAlignment(JLabel.CENTER);

		return etiqueta;
	}

	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	// :::::::::::::::::::::::::::::::::METODOS PARA LAS LAMINAS MAIN, SQL, CLIENTE:::::::::::::::::::::::::::::
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	// no se usa this.add(laminaCliente) porque se agrega en laminaMain
	private void addLaminaCliente() {
		laminaCliente = new JPanel();
		laminaCliente.setLayout(null);
		laminaCliente.setBackground(negro);
		laminaCliente.setBorder(new LineBorder(new Color(15, 126, 218), 2));
		addJTextUpdate(); //se invoca el metodo para crear un JTextField
	}

	// no se usa this.add(laminaSQL) porque se agrega en laminaMain
	private void addLaminaSQL() {
		laminaSQL = new JPanel();
		laminaSQL.setLayout(null);
		laminaSQL.setBackground(negro);
		laminaSQL.setBorder(bordeVerde);
		addJTextArea(); //se invoca el metodo para crear un JTextArea
	}

	// Lamina que contiene el CardLayout.
	// No necesita estilos, actua como un simple contenedor
	// El Cardlayout permite intercambiar entre JPanels de forma sencilla con el metodo "show"
	public void addLaminaMain() {
		laminaMain = new JPanel();
		laminaMain.setLayout(cardLayout);
		laminaMain.add("SQL", laminaSQL); //se le pone un alias a la tabla para llamarla con show en JControlador_BDD.
		laminaMain.add("Cliente", laminaCliente); //se le pone un alias a la tabla para llamarla con show en JControlador_BDD.
	}

	// Define el estilo del boton y lo devuelve
	public JButton addBoton(String texto) {
		JButton b = new JButton();
		b.setText(texto);
		b.setBounds(575, 510, 150, 40);
		b.setFont(fuenteMono);
		b.setFocusable(false);
		b.setForeground(Color.WHITE);
		b.setBackground(azul);
		return b;
	}

	// Creamos un area de texto y definimos su estilo
	private void addJTextArea() {
		areaTexto = new JTextArea();
		// esto permite usar 'enter' para saltar de linea y evitar que desborde el texto
		JScrollPane scroll = new JScrollPane(areaTexto); 													
		scroll.setBounds(2, 290, 775, 200);
		scroll.setBorder(null);
		areaTexto.setMargin(new Insets(5, 10, 0, 0)); // 5px de margen arriba, 10 a la izq, 0 a la derecha/abajo
		areaTexto.setBackground(verdeOscuro);
		areaTexto.setForeground(Color.GREEN);
		areaTexto.setFont(fuenteMono);
		areaTexto.setText("Introduce tu consulta aqui...");
		areaTexto.setSelectedTextColor(Color.RED);
		areaTexto.setSelectionColor(verdeOscuro);
		laminaSQL.add(scroll); //se agrega el JTextArea a la LaminaSQL
	}
	
	// Etiqueta para el JTextField que usan los botones UPDATE e INSERT
		public void addJTextUpdate() {
			jTextUpdate = new JTextField();
			jTextUpdate.setText("Espacio para realizar INSERT y UPDATE");
			jTextUpdate.setSelectedTextColor(Color.RED);
			jTextUpdate.setSelectionColor(negro);
			jTextUpdate.setBounds(100, 450, 550, 50);
			jTextUpdate.setForeground(Color.WHITE);
			jTextUpdate.setBorder(bordeVerde);
			jTextUpdate.setFont(fuenteMono);
			jTextUpdate.setHorizontalAlignment(JTextField.CENTER); // centra el texto
			jTextUpdate.setOpaque(false); // vuelve a la etiqueta transparente
			laminaCliente.add(jTextUpdate); //se agrega el JTextArea a la LaminaCliente
		}

	/*
	 * Se crean 2 tablas diferentes, pero con el mismo contenido.
	 * Una para cada lamina. 
	 * El metodo ModificarTabla() pide la lamina a la que deseas agregar la tabla por parametros.
	 */

	public static void addJTable(DefaultTableModel tabla) {
	
		tablaSQL = modificarTabla(laminaSQL);
		tablaCliente = modificarTabla(laminaCliente);
		tablaSQL.setModel(tabla);
		tablaCliente.setModel(tabla);
		
	}
	
	/*
	 * Define los estilos de la tabla y la devuelve
	 */

	public static JTable modificarTabla(JPanel lamina) {
		
		/*
		 * Para crear una JTable con encabezado personalizado: 
		 * -> Añadir los nombres de las columnas en "DefaultTableModel" 
		 * -> Definir la clase JTable y añadir el DefaultTableModel con .setModel(encabezado); 
		 * -> Definir JTableHeader creando una instancia y modificando su CSS 
		 * -> Añadir un ScrollPane y meter la tabla dentro (en el constructor);
		 */

		//CSS de la tabla
		JTable tabla = new JTable();
		tabla.setFont(fuenteMono);
		tabla.setBackground(negro);
		tabla.setShowGrid(false);
		tabla.setForeground(Color.GREEN);

		//Se modifica el CSS de la cabecera
		JTableHeader header = tabla.getTableHeader();
		header.setBackground(azul);
		header.setForeground(Color.WHITE);
		header.setFont(fuenteMono);

		//Se añade un sroll
		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBounds(2, 0, 775, 300);
		scroll.setBorder(null);
		scroll.setOpaque(false);
		//IMPORTANTE: si añades la JTable al scroll, debes asignar el scrollPane en lugar de la JTable
		//En caso contrario, no se podra visualizar en la lamina asignada
		lamina.add(scroll); 
		return tabla;
	}
}
