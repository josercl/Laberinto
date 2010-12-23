package gui;

import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

public class Tablero extends JPanel {

	private Vector<Linea> lineas;
	
	public Tablero(){
		super();
		lineas=new Vector<Linea>(0);
	}
	
	public void agregarLinea(Linea l){
		lineas.add(l);
	}
	
	public void agregarLinea(int x1,int y1,int x2,int y2){
		lineas.add(new Linea(x1,y1,x2,y2));
	}
	
	public void paint(Graphics g){
		super.paint(g);
		for(Linea l:lineas){
			g.drawLine(l.x1, l.y2, l.x2, l.y2);
		}
	}

	public Vector<Linea> getLineas() {
		return lineas;
	}

	public void setLineas(Vector<Linea> lineas) {
		this.lineas = lineas;
	}
}

class Linea{
	public int x1,y1,x2,y2;
	public Linea(int x1,int y1,int x2,int y2){
		this.x1=x1;
		this.x2=x2;
		this.y1=y1;
		this.y2=y2;
	}
}