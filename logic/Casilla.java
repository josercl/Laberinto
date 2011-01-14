package logic;

public class Casilla implements Comparable<Casilla>{

	private int numero;
	private boolean vecinoSup,vecinoInf,vecinoIzq,vecinoDer;
	
	public Casilla(int n){
		this.numero=n;
		this.vecinoSup=false;
		this.vecinoInf=false;
		this.vecinoIzq=false;
		this.vecinoDer=false;
	}
	
	public boolean equals(Casilla c){
		return this.numero==c.getNumero();
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public boolean tieneVecinoSup() {
		return vecinoSup;
	}

	public void setVecinoSup(boolean vecinoSup) {
		this.vecinoSup = vecinoSup;
	}

	public boolean tieneVecinoInf() {
		return vecinoInf;
	}

	public void setVecinoInf(boolean vecinoInf) {
		this.vecinoInf = vecinoInf;
	}

	public boolean tieneVecinoIzq() {
		return vecinoIzq;
	}

	public void setVecinoIzq(boolean vecinoIzq) {
		this.vecinoIzq = vecinoIzq;
	}

	public boolean tieneVecinoDer() {
		return vecinoDer;
	}

	public void setVecinoDer(boolean vecinoDer) {
		this.vecinoDer = vecinoDer;
	}

	public int compareTo(Casilla c) {
		return this.numero-c.getNumero();
	}
	
	public String toString(){
		return this.numero+"";
	}
	
}
