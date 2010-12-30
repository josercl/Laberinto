package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicBorders.MarginBorder;

public class Tablero extends JPanel {

	private Vector<Linea> lineas;
	private Vector<Paso> pasos;
	
	public Tablero(){
		super();
		lineas=new Vector<Linea>(0);
		pasos=new Vector<Paso>(0);
	}
	
	public void agregarLinea(Linea l){
		lineas.add(l);
	}
	
	public void agregarLinea(int x1,int y1,int x2,int y2){
		lineas.add(new Linea(x1,y1,x2,y2));
	}
	
	public void agregarPaso(Paso p){
		pasos.add(p);
	}
	
	public void agregarPaso(int x1,int y1,int x2,int y2){
		pasos.add(new Paso(x1,y1,x2,y2));
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		g.translate(Util.margen, Util.margen);
		
		g.setColor(Color.green);
		for(Paso p:pasos){
			g.fillRect(p.x1, p.y1, p.x2, p.y2);
		}
		
		g.setColor(Color.black);
		for(Linea l:lineas){
			g.drawLine(l.x1, l.y1, l.x2, l.y2);
		}
		g.translate(0,0);
	}

	public Vector<Linea> getLineas() {
		return lineas;
	}

	public void setLineas(Vector<Linea> lineas) {
		this.lineas = lineas;
	}

	public Vector<Paso> getPasos() {
		return pasos;
	}

	public void setPasos(Vector<Paso> pasos) {
		this.pasos = pasos;
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

class Paso extends Linea{
	public Paso(int x1,int y1,int ancho,int alto){
		super(x1, y1, ancho, alto);
	}
}