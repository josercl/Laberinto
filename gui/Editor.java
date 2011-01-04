package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import logic.Casilla;
import logic.Grafo;
import logic.Nodo;
@SuppressWarnings("serial")
public class Editor extends JDialog implements MouseListener{

	private int filas, columnas, inicio = -1, fin = -1;
	private Casilla[][] casillas;
	private Principal parent;
	private JPopupMenu pop;
	private Grafo grafo;

	JButton cerrar,guardar;
	
	int ancho_casilla = 40;
	int alto_casilla = 40;
	int ancho_alto_boton = 7;
	
	private ResourceBundle rb=Util.resource;

	public Editor(Principal parent, int filas, int columnas) {
		super(parent, false);
		this.filas = filas;
		this.columnas = columnas;
		this.parent = parent;
		initComponents();
	}
	
	public Editor(Principal parent, int filas, int columnas,int inicio, int fin,Grafo g) {
		super(parent, false);
		this.filas = filas;
		this.columnas = columnas;
		this.parent = parent;
		this.inicio=inicio;
		this.fin=fin;
		this.grafo=g;
		initComponents();
	}

	private void initComponents() {
		setTitle(rb.getString("editor.titulo"));
		casillas = new Casilla[filas][columnas];
		setLayout(new BorderLayout());

		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		add(bar, BorderLayout.NORTH);

		cerrar = new JButton(rb.getString("editor.toolbar.cerrar"));
		cerrar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				cerrar();
			}
		});

		guardar = new JButton(rb.getString("editor.toolbar.guardar"));
		guardar.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});

		bar.add(guardar);
		bar.add(cerrar);

		pop=new JPopupMenu();
		JMenuItem inicio_item=new JMenuItem(rb.getString("editor.popupmenu.inicio"));
		
		inicio_item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				establecerInicio(pop.getInvoker());
			}
		});
		
		JMenuItem fin_item=new JMenuItem(rb.getString("editor.popupmenu.fin"));
		
		fin_item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				establecerFin(pop.getInvoker());
			}
		});
		
		pop.add(inicio_item);
		pop.add(fin_item);
		
		agregarCasillas();
		
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screen=tk.getScreenSize();
		setBounds(0,0,screen.width,screen.height);
	}

	private void agregarCasillas() {
		JPanel casillas_panel = new JPanel(new GridLayout(filas, columnas));
		JPanel casillaPanel = null;
		JButton abajo, derecha;
		int numeroNodo=0;
		Nodo actual=null;
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				casillas[i][j] = new Casilla(i * columnas + j);

				casillaPanel = new JPanel(new BorderLayout());
				
				numeroNodo=i*columnas+j;
				
				if(this.inicio==numeroNodo){
					casillaPanel.setBackground(Util.inicioColor);
				}
				if(this.fin==numeroNodo){
					casillaPanel.setBackground(Util.finColor);
				}
				
				if(grafo!=null){
					actual=grafo.obtenerNodo(numeroNodo);
				}
				
				casillaPanel.add(new JLabel(numeroNodo+"",JLabel.CENTER),BorderLayout.CENTER);
				
				casillaPanel.addMouseListener(this);

				final int fila = i;
				final int columna = j;

				//if (i != filas - 1) {
					abajo = new JButton();
					abajo.setBackground(Color.black);
					
					if(grafo!=null){
						if((i+1<filas) && actual.getVecinos().contains(new Nodo( (i+1)*columnas+j  ))){
							abajo.setBackground(Color.white);
							casillas[i][j].setVecinoInf(true);
						}
						if((i-1>=0) && actual.getVecinos().contains(new Nodo( (i-1)*columnas+j  ))){
							casillas[i][j].setVecinoSup(true);
						}
					}
					
					abajo.setPreferredSize(new Dimension(ancho_casilla,ancho_alto_boton));

					abajo.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							boolean vecinoInf = casillas[fila][columna].tieneVecinoInf();
							casillas[fila][columna].setVecinoInf(!vecinoInf);
							if (fila + 1 < filas) {
								casillas[fila + 1][columna].setVecinoSup(!vecinoInf);
							}

							JButton b = (JButton) e.getSource();
							b.setBackground((!vecinoInf) ? Color.white: Color.black);
						}
					});

					casillaPanel.add(abajo, BorderLayout.SOUTH);
				//}

				//if (j != columnas - 1) {
					derecha = new JButton();
					derecha.setBackground(Color.black);
					
					if(grafo!=null){
						if((j+1<columnas) && actual.getVecinos().contains(new Nodo( (i*columnas)+j+1 ))){
							derecha.setBackground(Color.white);
							casillas[i][j].setVecinoDer(true);
						}
						if((j-1>=0) && actual.getVecinos().contains(new Nodo( (i*columnas)+j-1 ))){
							casillas[i][j].setVecinoIzq(true);
						}
					}
					
					derecha.setPreferredSize(new Dimension(ancho_alto_boton,alto_casilla));

					derecha.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							boolean vecinoDer = casillas[fila][columna].tieneVecinoDer();
							casillas[fila][columna].setVecinoDer(!vecinoDer);
							if (columna + 1 < columnas) {
								casillas[fila][columna + 1].setVecinoIzq(!vecinoDer);
							}

							JButton b = (JButton) e.getSource();
							b.setBackground((!vecinoDer) ? Color.white: Color.black);
						}
					});

					casillaPanel.add(derecha, BorderLayout.EAST);
				//}

				casillas_panel.add(casillaPanel);
			}
		}

		add(casillas_panel, BorderLayout.CENTER);
	}

	private void cerrar() {
		setVisible(false);
		parent.grafo=null;
		parent.nodos=null;
	}

	private void guardar() {
		int opcion = parent.chooser.showSaveDialog(this);
		if (opcion == JFileChooser.APPROVE_OPTION) {
			File archivo = parent.chooser.getSelectedFile();
			if (!archivo.isFile()) {
				try {
					if (!archivo.createNewFile()) {
						Util.mostrarMensaje(this,Util.mensajes("mensajes.error.archivo_no_crear", new Object[]{archivo.getName()}));
					}
				} catch (IOException e) {
					Util.mostrarMensaje(this, Util.mensajes("mensajes.error.archivo_no_crear",new Object[]{archivo.getName()}));
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
					String str=null;
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
					Util.mostrarMensaje(this,Util.mensajes("mensajes.error.archivo_no_guardar",new Object[]{archivo.getName()}));
				}
			} else {
				Util.mostrarMensaje(this,Util.mensajes("mensajes.error.archivo_no_permisos", new Object[]{archivo.getName()}));
			}
			parent.leerArchivo(archivo,false);
			setVisible(false);
		}
	}

	
	public void mouseClicked(MouseEvent e) {}

	
	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}

	
	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	
	public void mouseEntered(MouseEvent e) {}

	
	public void mouseExited(MouseEvent e) {}
	
	private void showPopup(MouseEvent e){
		if (e.isPopupTrigger()) {
            pop.show(e.getComponent(),e.getX(), e.getY());
        }
	}
	
	private void establecerInicio(Component c){
		establecerInicioFin(c, true);
	}
	
	private void establecerFin(Component c){
		establecerInicioFin(c, false);
	}
	
	private void establecerInicioFin(Component c,boolean inicio){
		JPanel panel=(JPanel) c;
		JPanel grid=((JPanel) getContentPane().getComponent(1));
		int totalPaneles=grid.getComponentCount();
		JPanel temp=null;
		
		if(inicio){
			if(this.inicio!=-1){
				grid.getComponent(this.inicio).setBackground(null);
			}
		}else{
			if(this.fin!=-1){
				grid.getComponent(this.fin).setBackground(null);
			}
		}
		
		for(int i=0;i<totalPaneles;i++){
			temp=(JPanel) grid.getComponent(i);
			if(temp.equals(panel)){
				if(inicio){
					this.inicio=i;
					temp.setBackground(Util.inicioColor);
				}else{
					this.fin=i;
					temp.setBackground(Util.finColor);
				}
			}
		}
	}

	public Grafo getGrafo() {
		return grafo;
	}

	public void setGrafo(Grafo grafo) {
		this.grafo = grafo;
	}

}
