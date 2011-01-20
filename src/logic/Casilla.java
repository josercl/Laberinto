package logic;

/**
 * Clase usada para representar una casilla del editor y el generador de
 * laberintos.
 * 
 * @author José Rafael Carrero León &lt;<a href="mailto:josercl@gmail.com">josercl@gmail.com</a>&gt;
 * @since 1.6
 * 
 */
public class Casilla implements Comparable<Casilla> {
	private int numero;
	private boolean vecinoSup, vecinoInf, vecinoIzq, vecinoDer;

	/**
	 * Crea una casilla sin vecinos
	 * 
	 * @param n El número que representa a la casilla dentro del laberinto
	 */
	public Casilla(int n) {
		this.numero = n;
		this.vecinoSup = false;
		this.vecinoInf = false;
		this.vecinoIzq = false;
		this.vecinoDer = false;
	}

	/**
	 * Compara 2 casillas. Una casilla se considera igual a otra si el número
	 * que las representa es igual
	 * 
	 * @param c Casilla usara para comparar con la casilla actual
	 * @return Si una casilla es igual a la casilla usada como argumento
	 */
	public boolean equals(Casilla c) {
		return this.numero == c.getNumero();
	}

	/**
	 * Obtiene el número representativo de la casilla
	 * 
	 * @return El número que representa a la casilla
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * Determina si la casilla tiene un vecino arriba en el laberinto
	 * 
	 * @return Si la casilla tiene una casilla adyacente hacia arriba
	 */
	public boolean tieneVecinoSup() {
		return vecinoSup;
	}

	/**
	 * Establece la existencia de una casilla adyacente hacia arriba
	 * 
	 * @param vecinoSup True si la casilla tiene un vecino arriba, falso en caso contrario
	 */
	public void setVecinoSup(boolean vecinoSup) {
		this.vecinoSup = vecinoSup;
	}

	/**
	 * Determina si la casilla tiene un vecino abajo en el laberinto
	 * 
	 * @return Si la casilla tiene una casilla adyacente hacia abajo
	 */
	public boolean tieneVecinoInf() {
		return vecinoInf;
	}

	public void setVecinoInf(boolean vecinoInf) {
		this.vecinoInf = vecinoInf;
	}

	/**
	 * Determina si la casilla tiene un vecino a la izquierda en el laberinto
	 * 
	 * @return Si la casilla tiene una casilla adyacente a la izquierda
	 */
	public boolean tieneVecinoIzq() {
		return vecinoIzq;
	}

	public void setVecinoIzq(boolean vecinoIzq) {
		this.vecinoIzq = vecinoIzq;
	}

	/**
	 * Determina si la casilla tiene un vecino a la derecha en el laberinto
	 * 
	 * @return Si la casilla tiene una casilla adyacente a la derecha
	 */
	public boolean tieneVecinoDer() {
		return vecinoDer;
	}

	public void setVecinoDer(boolean vecinoDer) {
		this.vecinoDer = vecinoDer;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Casilla c) {
		return this.numero - c.getNumero();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.numero + "";
	}

}
