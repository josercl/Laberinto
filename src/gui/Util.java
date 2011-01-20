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

/**
 * Clase utilitaria con algunas constantes y funciones usadas en el programa
 * principal
 * 
 * @author José Rafael Carrero León &lt;<a
 *         href="mailto:josercl@gmail.com">josercl@gmail.com</a>&gt;
 * @since 1.6
 * 
 */
public class Util {

	/**
	 * Color usado para representar la casilla de inicio de la solucion del
	 * laberinto
	 */
	public static final Color inicioColor = Color.blue;
	/**
	 * Color usado para representar la casilla final de la solución del
	 * laberinto
	 */
	public static final Color finColor = Color.red;

	/**
	 * Función usada para mostrar mensajes de error
	 * 
	 * @param parent
	 *            Ventana "padre" del mensaje de error
	 * @param mensaje
	 *            Mensaje de error que será mostrado
	 */
	public static void mostrarMensaje(Component parent, String mensaje) {
		JOptionPane.showMessageDialog(parent, mensaje, "Error",JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Objeto encargado de cargar los mensajes del sistema de acuerdo a la configuración local de idiomas
	 */
	public static ResourceBundle resource = ResourceBundle.getBundle(
			"resources.textos", Locale.getDefault());

	private static MessageFormat formatter = new MessageFormat("");

	/**
	 * Función que parametriza los mensajes que toman ciertos argumentos en el archivo de propiedades
	 * @param key Identificador dentro el archivo de propiedades del mensaje a mostrar
	 * @param argumentos Argumentos del mensaje que será mostrado
	 * @return El mensaje descrito en el archivo de propiedades tomando en cuenta los parámetros suministrados
	 */
	public static String mensajes(String key, Object[] argumentos) {
		formatter.applyPattern(resource.getString(key));
		return formatter.format(argumentos);
	}

	/**
	 * Función que guarda en un archivo de texto la información del laberinto: Número de filas, número de columnas,
	 * casilla inicial, casilla final y el listado de las casillas con sus vecinos
	 * @param archivo Archivo donde será guardado el laberinto
	 * @param casillas Arreglo que contiene las casillas del laberinto
	 * @param inicio Casilla del laberinto que será usada como inicio de la solución
	 * @param fin Casilla del laberinto que será usada como fin de la solución
	 */
	public static void guardar(File archivo, Casilla[][] casillas, int inicio,
			int fin) {
		int filas = casillas.length;
		int columnas = casillas[0].length;

		if (!archivo.isFile()) {
			try {
				if (!archivo.createNewFile()) {
					Util.mostrarMensaje(null, Util.mensajes(
							"mensajes.error.archivo_no_crear",
							new Object[] { archivo.getName() }));
				}
			} catch (IOException e) {
				Util.mostrarMensaje(null, Util.mensajes(
						"mensajes.error.archivo_no_crear",
						new Object[] { archivo.getName() }));
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
