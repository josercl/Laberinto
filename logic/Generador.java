package logic;

import gui.Principal;
import gui.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

public class Generador {

	private int filas,columnas;
	private Casilla[][] casillas;
	private Principal parent;
	
	private final int ARRIBA=1;
	private final int ABAJO=2;
	private final int IZQUIERDA=3;
	private final int DERECHA=4;
	
	public Generador(Principal parent,int filas,int columnas){
		this.parent=parent;
		this.filas=filas;
		this.columnas=columnas;
		init();
	}
	
	public final void init(){
		this.casillas=new Casilla[filas][columnas];
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				casillas[i][j] = new Casilla(i * columnas + j);
			}
		}
	}
	
	public void generar(){

		TreeSet<Casilla> visitados=new TreeSet<Casilla>();
		List<Casilla> porVisitar=new ArrayList<Casilla>();
		
		porVisitar.add(0,casillas[0][0]); //el primer nodo a visitar es la casilla[0][0]
		
		Random rnd=new Random();
		
		boolean listo=false;
		int cantidadVecinosAElegir=0;
		int filaActual=0,columnaActual=0;
		List<Integer> direccionesElegidas=null;
		Casilla vecino=null;
		
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
				Collections.shuffle(direcciones[numero]);
			}
		}
		
		
		while(porVisitar.size()!=0){
			Casilla actual=porVisitar.get(0);
			cantidadVecinosAElegir=0;
			visitados.add(actual);
			
			int numero=actual.getNumero();
			filaActual=numero/columnas;
			columnaActual=numero%columnas;
			
			List<Integer> vecinos=direcciones[numero];
			
			//obtenemos el vecino que va en la direccion elegida
			vecino=actual;
			while(visitados.contains(vecino) && direcciones[numero].size()!=0){
				int direccionElegida=direcciones[numero].get(0);
				direcciones[numero].remove(0);
				switch(direccionElegida){
					case ARRIBA:
						vecino=casillas[filaActual-1][columnaActual];
						if(!visitados.contains(vecino)){
							actual.setVecinoSup(true);
							vecino.setVecinoInf(true);
							porVisitar.add(0,vecino);
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
			if(direcciones[actual.getNumero()].size()==0){
				porVisitar.remove(actual);
			}
		}
		
		File archivo=new File(System.getProperty("java.io.tmpdir")+"lab.txt");
		Util.guardar(archivo, casillas, 0, filas*columnas-1);
		parent.leerArchivo(archivo,false);
	}
	
	public int getFilas() {
		return filas;
	}

	public void setFilas(int filas) {
		this.filas = filas;
	}

	public int getColumnas() {
		return columnas;
	}

	public void setColumnas(int columnas) {
		this.columnas = columnas;
	}
	
	
	
}
