package logic;

import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

public class Grafo {

	private Vector<Nodo> nodos;
	
	public Grafo(){
		this.nodos=new Vector<Nodo>(0);
	}
	
	public void add(Nodo n){
		this.nodos.add(n);
	}
	
	public Nodo obtenerNodo(int n){
		for(Nodo nodo:nodos){
			if(nodo.getNumero()==n){
				return nodo;
			}
		}
		return null;
	}
	
	public Nodo procesarAgregar(int nodoNumero){
		if(existe(nodoNumero)){
			return obtenerNodo(nodoNumero);
		}else{
			Nodo nuevo=new Nodo(nodoNumero);
			add(nuevo);
			return nuevo;
		}
	}
	
	public boolean existe(int n){
		return obtenerNodo(n)!=null;
	}
	
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

	public Vector<Nodo> getNodos() {
		return nodos;
	}

}
