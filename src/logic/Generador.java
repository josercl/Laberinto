package logic;

import gui.Principal;
import gui.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Clase encargada de generar laberintos de forma semi-aleatoria.
 * 
 * @author José Rafael Carrero León &lt;<a href="mailto:josercl@gmail.com">josercl@gmail.com</a>&gt;
 * @since 1.6 
 *
 */
public class Generador {

	/**
	 * Numero de filas del laberinto
	 */
	private int filas;
	
	/**
	 * Numero de columnas del laberinto
	 */
	private int columnas;
	
	private Casilla[][] casillas;
	private Principal parent;
	
	private final int ARRIBA=1;
	private final int ABAJO=2;
	private final int IZQUIERDA=3;
	private final int DERECHA=4;
	
	/**
	 * Crea el objeto generador
	 * @param parent Ventana principal donde se muestra los laberintos a resolver
	 * @param filas El número de filas del laberinto
	 * @param columnas El número de columnas del laberinto
	 */
	public Generador(Principal parent,int filas,int columnas){
		this.parent=parent;
		this.filas=filas;
		this.columnas=columnas;
		init();
	}
	
	/**
	 * Inicializa el arreglo que mantiene las casillas del laberinto
	 */
	public final void init(){
		this.casillas=new Casilla[filas][columnas];
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				casillas[i][j] = new Casilla(i * columnas + j);
			}
		}
	}
	
	/**
	 * Genera un laberinto de forma semi-aleatoria.
	 * 
	 * El algoritmo hace uso de DFS (Depth First Search) con backtracking,
	 * asegurando que se puede llegar a cualquier casilla del laberinto sin importar la casilla de inicio
	 */
	@SuppressWarnings("unchecked")
	public void generar(){

		//Conjuntos usados en el algoritmo de generacion
		TreeSet<Casilla> visitados=new TreeSet<Casilla>();
		List<Casilla> porVisitar=new ArrayList<Casilla>();
		
		porVisitar.add(0,casillas[0][0]); //el primer nodo a visitar es la casilla[0][0]
		
		int filaActual=0,columnaActual=0;
		Casilla vecino=null;
		
		//El arreglo direcciones contiene las posibles direcciones que se pueden tomar a partir de cualquier casilla
		//cada lista de direcciones tendrá a lo sumo 4 valores: arriba,abajo,izquierda y derecha
		List<Integer> direcciones[]=new ArrayList[filas*columnas];
		for(int i=0;i<filas;i++){
			for(int j=0;j<columnas;j++){
				int numero=i*columnas+j;
				direcciones[numero]=new ArrayList<Integer>();
				
				if(i!=0){ //si no estoy en la primera fila, puedo ir hacia arriba
					direcciones[numero].add(ARRIBA);
				}
				if(i!=filas-1){ //si no estoy en la ultima fila, puedo ir hacia abajo
					direcciones[numero].add(ABAJO);
				}
				if(j!=0){ //si no estoy en la primera columna, puedo ir hacia la izquierda
					direcciones[numero].add(IZQUIERDA);
				}
				if(j!=columnas-1){ //si no estoy en la ultima columna, puedo ir hacia la derecha
					direcciones[numero].add(DERECHA);
				}
				//Desordenamos el arreglo de posiciones para tener aleatoriedad en las direcciones tomadas en el recorrido
				Collections.shuffle(direcciones[numero]);
			}
		}
		
		//Mientras queden nodos por visitar en la pila seguimos el recorrido
		while(porVisitar.size()!=0){
			Casilla actual=porVisitar.get(0); //Obtenemos la primera casilla de la pila para empezar el recorrido
			visitados.add(actual); //marcamos la casilla actual como visitada
			
			int numero=actual.getNumero();
			filaActual=numero/columnas;
			columnaActual=numero%columnas;
			
			vecino=actual;
			while(visitados.contains(vecino) && direcciones[numero].size()!=0){
				int direccionElegida=direcciones[numero].get(0); //obtenemos la direccion del primer vecino de la casilla actual
				direcciones[numero].remove(0); //eliminamos la direccion elegida para no elegirla de nuevo en iteraciones siguientes
				switch(direccionElegida){
					case ARRIBA:
						vecino=casillas[filaActual-1][columnaActual];
						if(!visitados.contains(vecino)){
							actual.setVecinoSup(true);
							vecino.setVecinoInf(true);
							porVisitar.add(0,vecino); //agregamos el vecino elegido a la lista de nodos por visitar
						}
						break;
					case ABAJO:
						vecino=casillas[filaActual+1][columnaActual];
						if(!visitados.contains(vecino)){
							actual.setVecinoInf(true);
							vecino.setVecinoSup(true);
							porVisitar.add(0,vecino);
						}
						break;
					case IZQUIERDA:
						vecino=casillas[filaActual][columnaActual-1];
						if(!visitados.contains(vecino)){
							actual.setVecinoIzq(true);
							vecino.setVecinoDer(true);
							porVisitar.add(0,vecino);
						}
						break;
					case DERECHA:
						vecino=casillas[filaActual][columnaActual+1];
						if(!visitados.contains(vecino)){
							actual.setVecinoDer(true);
							vecino.setVecinoIzq(true);
							porVisitar.add(0,vecino);
						}
						break;
				}
			}
			//si la casilla actual no tiene mas vecinos sin recorrer
			//la eliminamos de la lista de nodos por visitar
			if(direcciones[actual.getNumero()].size()==0){ 
				porVisitar.remove(actual);
			}
		}
		
		//guardamos el laberinto generado en un archivo temporal y lo cargamos en la pantalla principal
		File archivo=new File(System.getProperty("java.io.tmpdir")+File.separator+"lab.txt");
		Util.guardar(archivo, casillas, 0, filas*columnas-1);
		parent.leerArchivo(archivo,false);
	}
	
	/**
	 * Retorna el numero de filas que tendrá el laberinto generado
	 * @return cantidad filas que tendrá el laberinto a generar
	 */
	public int getFilas() {
		return filas;
	}

	/**
	 * Establece el número de filas del laberinto
	 * @param filas Cantidad de filas que tendrá el laberinto generado
	 */
	public void setFilas(int filas) {
		this.filas = filas;
	}

	/**
	 * Retorna el numero de columnas que tendrá el laberinto generado
	 * @return cantidad de columnas que tendrá el laberinto generado
	 */
	public int getColumnas() {
		return columnas;
	}

	/**
	 * Establece el número de columnas del laberinto
	 * @param columnas cantidad de columnas que tendrá el laberinto generado
	 */
	public void setColumnas(int columnas) {
		this.columnas = columnas;
	}
	
	
	
}
