package gui;

import java.awt.Color;
import java.awt.Component;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class Util {

	public static final Color inicioColor=Color.blue;
	public static final Color finColor=Color.red;
	
	public static final int margen = 20;
	public static final int ancho_celda = 20; // ancho de cada "celda" del laberinto
	public static final int alto_celda = 20; // alto de cada "celda" del laberinto
	public static final int radio_punto=new Float(ancho_celda*.2).intValue();
	
	public static void mostrarMensaje(Component parent,String s){
		JOptionPane.showMessageDialog(parent, s, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static ResourceBundle resource=ResourceBundle.getBundle("resources.textos",Locale.getDefault());
	
	private static MessageFormat formatter=new MessageFormat("");
	
	public static String mensajes(String key,Object[] argumentos){
		formatter.applyPattern(resource.getString(key));
		return formatter.format(argumentos);
	}
	
}
