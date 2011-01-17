package gui;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import logic.Casilla;

public class Util {

	public static final Color inicioColor = Color.blue;
	public static final Color finColor = Color.red;

	public static final int margen = 20;
	public static final int ancho_celda = 20; // ancho de cada "celda" del
												// laberinto
	public static final int alto_celda = 20; // alto de cada "celda" del
												// laberinto
	public static final int radio_punto = new Float(ancho_celda * .2).intValue();

	public static void mostrarMensaje(Component parent, String s) {
		JOptionPane.showMessageDialog(parent, s, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public static ResourceBundle resource = ResourceBundle.getBundle("resources.textos", Locale.getDefault());

	private static MessageFormat formatter = new MessageFormat("");

	public static String mensajes(String key, Object[] argumentos) {
		formatter.applyPattern(resource.getString(key));
		return formatter.format(argumentos);
	}

	public static void guardar(File archivo, Casilla[][] casillas, int inicio,
			int fin) {
		int filas = casillas.length;
		int columnas = casillas[0].length;
		
		if (!archivo.isFile()) {
			try {
				if (!archivo.createNewFile()) {
					Util.mostrarMensaje(null,Util.mensajes("mensajes.error.archivo_no_crear", new Object[]{archivo.getName()}));
				}
			} catch (IOException e) {
				Util.mostrarMensaje(null, Util.mensajes("mensajes.error.archivo_no_crear",new Object[]{archivo.getName()}));
			}
		}
		
		if (archivo.canWrite()) {
			Casilla c = null;
			try {
				PrintStream ps = new PrintStream(new FileOutputStream(archivo));
				ps.println(filas);
				ps.println(columnas);
				ps.println(inicio);
				ps.println(fin);
				String str = null;
				for (int i = 0; i < filas; i++) {
					for (int j = 0; j < columnas; j++) {
						c = casillas[i][j];
						str = c.getNumero() + "";
						if (c.tieneVecinoSup()) {
							str += " " + casillas[i - 1][j].getNumero();
						}
						if (c.tieneVecinoIzq()) {
							str += " " + casillas[i][j - 1].getNumero();
						}
						if (c.tieneVecinoDer()) {
							str += " " + casillas[i][j + 1].getNumero();
						}
						if (c.tieneVecinoInf()) {
							str += " " + casillas[i + 1][j].getNumero();
						}
						ps.println(str);
					}
				}

			} catch (IOException e) {
				Util.mostrarMensaje(null, Util.mensajes(
						"mensajes.error.archivo_no_guardar",
						new Object[] { archivo.getName() }));
			}
		} else {
			Util.mostrarMensaje(null, Util.mensajes(
					"mensajes.error.archivo_no_permisos",
					new Object[] { archivo.getName() }));
		}
	}

}
