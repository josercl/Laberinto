package logic;

import java.util.TreeSet;

public class Nodo implements Comparable<Nodo>{

	private Nodo padre;
	private int numero;
	private TreeSet<Nodo> vecinos;
	
	public Nodo(int n){
		this.numero=n;
		this.vecinos=new TreeSet<Nodo>();
	}

	public Nodo getPadre() {
		return padre;
	}

	public void setPadre(Nodo padre) {
		this.padre = padre;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public boolean equals(Nodo n2){
		return this.numero==n2.getNumero();
	}
	
	public String toString(){
		return numero+"";
	}

	@Override
	public int compareTo(Nodo o) {
		return numero-o.getNumero();
	}

	public TreeSet<Nodo> getVecinos() {
		return vecinos;
	}

	public void setVecinos(TreeSet<Nodo> vecinos) {
		this.vecinos = vecinos;
	}
}
