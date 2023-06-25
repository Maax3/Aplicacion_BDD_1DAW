package bdd;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import bbd_vista.JFrame_BDD;


/*
 * Clase que informa sobre todas combinaciones de frutas y su valor.
 */	

@SuppressWarnings("serial")
public class Registro extends JFrame {
	
	private JFrame_BDD v = JFrame_BDD.getInstance();
	private JPanel p;
	//BTS
	public JButton reasignar;
	public JTextField usuario;
	public JPasswordField pass;
	//CSS
	private Color rojo = new Color(221,30,63);
	private Font fuenteMono = new Font(Font.MONOSPACED, Font.BOLD, 25);
	private static Color negro = new Color(0, 34, 46);
	private LineBorder bordeVerde = new LineBorder(Color.GREEN, 2);
	private Color verdeOscuro = new Color(71, 100, 104);
	
	public Registro () {
		this.setSize(500, 500);
		this.setLocationRelativeTo(v);
		this.setResizable(false);
		this.setTitle("Registro");
		this.setDefaultCloseOperation(JFrame_BDD.DISPOSE_ON_CLOSE);
		addPanel();
	}
	
	public void iniciar() {
		this.setVisible(true);
	}
	
	public void addPanel() {
		p = new JPanel();
		p.setLayout(null);
		p.setBackground(negro);
		p.setBorder(bordeVerde);
		addTitulo();
		addBoton();
		addTxt("USUARIO:",75,160);
		addTxt("PASSWORD:",75,230);
		addJTextUsuario();
		addJPassUsuario();
		this.add(p);	
	}
	
	public void addTitulo() {
			JLabel titulo = new JLabel("REASIGNAR");
			titulo.setForeground(Color.GREEN);
			titulo.setOpaque(true);
			titulo.setBackground(verdeOscuro);
			titulo.setFont(fuenteMono);
			titulo.setBounds(145, 60, 232, 40);
			titulo.setHorizontalAlignment(JLabel.CENTER);
			titulo.setVerticalAlignment(JLabel.CENTER);
			p.add(titulo);
		}
	
	public void addBoton() {
		reasignar = new JButton();
		reasignar.setActionCommand("REASIGNAR");
		reasignar.setBounds(130,350,250,70);
		reasignar.setIcon(new ImageIcon("imagenes/signup.png"));
		reasignar.setFocusable(false);
		reasignar.setContentAreaFilled(false);
		reasignar.setBackground(null);
		reasignar.setBorder(null);
		p.add(reasignar);
	}
	
	public void addTxt(String txt, int x,int y) {
		JLabel titulo = new JLabel(txt);
		titulo.setForeground(Color.GREEN);
		titulo.setFont(fuenteMono);
		titulo.setBounds(x, y, 140, 40);
		titulo.setBorder(new MatteBorder(2,2,2,0,Color.GREEN));
		titulo.setHorizontalAlignment(JLabel.CENTER);
		titulo.setVerticalAlignment(JLabel.CENTER);
		p.add(titulo);
	}
	
	// Etiqueta del usuario
	public void addJTextUsuario() {
		usuario = new JTextField();
		usuario.setBounds(215, 160, 240, 40);
		usuario.setForeground(Color.WHITE);
		usuario.setBorder(new MatteBorder(2,0,2,2,Color.GREEN));
		usuario.setFont(fuenteMono);
		usuario.setHorizontalAlignment(JTextField.CENTER); // centra el texto
		usuario.setOpaque(false);  //vuelve a la etiqueta transparente
		p.add(usuario); //se añade al JPanel (laminaLogin)
	}

	// Etiqueta de la password
	public void addJPassUsuario() {
		pass = new JPasswordField();
		pass.setBounds(215, 230, 240, 40);
		pass.setForeground(Color.WHITE);
		pass.setBorder(new MatteBorder(2,0,2,2,Color.GREEN));
		pass.setFont(fuenteMono);
		pass.setHorizontalAlignment(JTextField.CENTER); // centra el texto
		pass.setOpaque(false);   //vuelve a la etiqueta transparente
		p.add(pass);  //se añade al JPanel (laminaLogin)
	}
	
	public String getNuevoUsuario() {
		return String.valueOf(usuario.getText());
	}
	public String getNuevaPass() {
		return String.valueOf(pass.getPassword());
	}
	
	
	}
	
