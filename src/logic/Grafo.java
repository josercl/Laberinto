package logic;

import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
/**
 * Clase usada para representar el laberinto y encontrar la solución al mismo
 * 
 * @author José Rafael Carrero León <a href="mailto:josercl@gmail.com">&lt;josercl@gmail.com&gt;</a>
 * @since 1.6
 */
public class Grafo {

	private Vector<Nodo> nodos;
	
	/**
	 * Crea un grafo sin nodos
	 */
	public Grafo(){
		this.nodos=new Vector<Nodo>(0);
	}
	
	/*
	 * Agregar un nodo al grado
	 * @param n El nodo que ser� agregado al grafo
	 */
	public void add(Nodo n){
		this.nodos.add(n);
	}
	
	/**
	 * Obtiene el nodo representado por n en el grafo
	 * @param nodoNumero El número que representa al nodo
	 * @return El nodo especificado por n o null en caso de el nodo no exista en el grafo
	 */
	public Nodo obtenerNodo(int nodoNumero){
		for(Nodo nodo:nodos){
			if(nodo.getNumero()==nodoNumero){
				return nodo;
			}
		}
		return null;
	}
	
	/**
	 * Revisa si el nodo representado por <code>nodoNumero</code> ya existe en el grafo.
	 * 
	 * En caso de que el grafo no contenga el nodo especificado, se agrega a la lista de nodos del grafo
	 * @param nodoNumero El número del nodo a agregar 
	 * @return El nodo que fue agregado al grafo
	 */
	public Nodo procesarAgregar(int nodoNumero){
		if(existe(nodoNumero)){
			return obtenerNodo(nodoNumero);
		}else{
			Nodo nuevo=new Nodo(nodoNumero);
			add(nuevo);
			return nuevo;
		}
	}
	
	/**
	 * Revisa la existencia de un nodo dentro del grafo
	 * @param numeroNodo El número del nodo a buscar
	 * @return true si el nodo existe, false en caso contrario 
	 */
	public boolean existe(int numeroNodo){
		return obtenerNodo(numeroNodo)!=null;
	}

	/**
	 * Contruye un camino entre el nodo inicial y el nodo final usando un algoritmo de DFS y backtracking
	 * @param inicio El nodo inicial del recorrido
	 * @param fin El nodo final del recorrido
	 * @return Un camino entre el nodo inicial y el nodo final, en caso de no poder conseguir un camino
	 * entre los dos nodos, devuelve null;
	 */
	public Vector<Nodo> DFS(int inicio,int fin){
		
		TreeSet<Nodo> visitados=new TreeSet<Nodo>();
		Stack<Nodo> porVisitar=new Stack<Nodo>();
		
		Vector<Nodo> resultado=null;
		
		Nodo nodoInicial=obtenerNodo(inicio);
		porVisitar.push(nodoInicial); //el primer nodo a visitar es inicio
		
		boolean listo=false;
		Nodo nodofin=null;
		while(porVisitar.size()!=0 && !listo){
			Nodo actual=porVisitar.pop();
			visitados.add(actual);
			TreeSet<Nodo> vecinos=actual.getVecinos(); //obtenemos los vecinos del nodo
			
			for(Nodo n:vecinos){
				if(!visitados.contains(n)){ //si el vecino no ha sido visitado lo encolamos
					n.setPadre(actual);
					if(n.getNumero()==fin){
						listo=true;
						nodofin=n;
						break;
					}
					porVisitar.add(0,n);
				}
			}
		}
		
		if(listo){
			Nodo nodoActual=nodofin;
			resultado=new Vector<Nodo>();
			while(nodoActual!=null){
				resultado.add(nodoActual);
				nodoActual=nodoActual.getPadre();
			}
		}
		return resultado;
		
	}

	/**
	 * Devuelve los nodos que forman parte del grafo
	 * @return En listado de nodos del grafo
	 */
	public Vector<Nodo> getNodos() {
		return nodos;
	}

}
