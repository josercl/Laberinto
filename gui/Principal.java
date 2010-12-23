package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import logic.Grafo;
import logic.Nodo;

public class Principal extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel tablero,solucion;
	private Tablero tableroPrint;
	public JFileChooser chooser;
	private Editor editor;
	private JButton resolver,dibujar;
	private final int ancho=400;
	private final int alto=400;

	final int margen=20;
	final int ancho_celda=20; //ancho de cada "celda" del laberinto
	final int alto_celda=20; //alto de cada "celda" del laberinto

	private boolean pintarLaberinto=false;
	int inicio,fin,filas,columnas,nodos[][];

	private ResourceBundle rb=null;

	Grafo grafo;

	public Principal(){
		super();
		rb=Util.resource;
		setTitle(rb.getString("laberinto"));
		initComponents();
	}

	private void initComponents(){
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screen=tk.getScreenSize();

		JToolBar bar=new JToolBar();
		bar.setFloatable(false);
		dibujar=new JButton(rb.getString("toolbar.dibujar"));
		resolver=new JButton(rb.getString("toolbar.resolver"));

		bar.add(dibujar);bar.add(resolver);

		dibujar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solucion.getGraphics().clearRect(0,0,solucion.getWidth(),solucion.getHeight());
				dibujarTablero();
			}
		});

		resolver.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				resolver(inicio,fin);
				dibujarTablero();
			}
		});

		JMenuBar menubar=new JMenuBar();
		JMenu archivo=new JMenu(rb.getString("menu.archivo"));
		archivo.setMnemonic(rb.getString("menu.achivo.mnemonic").charAt(0));

		JMenuItem cargar=new JMenuItem(rb.getString("menu.archivo.abrir_laberinto"));
		cargar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
		cargar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				cargarLaberinto(false);
			}
		});
		archivo.add(cargar);

		JMenuItem nuevo_editor_item=new JMenuItem(rb.getString("menu.archivo.nuevo_laberinto"));
		nuevo_editor_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		nuevo_editor_item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				mostrarEditor();
			}
		});
		archivo.add(nuevo_editor_item);

		JMenuItem viejo_editor_item=new JMenuItem(rb.getString("menu.archivo.editar_laberinto"));
		viejo_editor_item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				cargarLaberinto(true);
			}
		});
		archivo.add(viejo_editor_item);

		JMenu galeria_laberintos=new JMenu(rb.getString("menu.archivo.galeria"));

		for(int i=1;i<=5;i++){
			JMenuItem gmi=new JMenuItem(rb.getString("laberinto")+" "+i);
			final int m=i;
			gmi.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					URL url = Principal.class.getResource("/laberinto/lab" + m
							+ ".txt");
					try {
						File f = new File(url.toURI());
						leerArchivo(f, false);
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			});
			galeria_laberintos.add(gmi);
		}

		JMenuItem mntmImprimir = new JMenuItem(rb.getString("menu.archivo.imprimir"));
		mntmImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(nodos!=null){
					imprimir();
				}
			}
		});
		mntmImprimir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		archivo.add(mntmImprimir);
		archivo.add(galeria_laberintos);

		archivo.addSeparator();
		JMenuItem salir=new JMenuItem(rb.getString("menu.archivo.salir"));
		salir.setMnemonic(rb.getString("menu.archivo.salir_mnemonic").charAt(0));
		salir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
		archivo.add(salir);
		salir.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});

		menubar.add(archivo);

		setJMenuBar(menubar);

		getContentPane().add(bar,BorderLayout.NORTH);

		tablero=new JPanel();
		tablero.setSize(screen);
		tablero.setBackground(Color.white);

		solucion=new JPanel();
		solucion.setOpaque(false);
		solucion.setSize(screen);

		JLayeredPane layers = new JLayeredPane();
		layers.add(solucion, new Integer(1));
		layers.add(tablero, new Integer(123));

		getContentPane().add(layers,BorderLayout.CENTER);

		chooser=new JFileChooser();

		int av=400;
		int alv=400;
		setBounds((screen.width-av)/2,(screen.height-alv)/2,av,alv);

		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
	}

	public void dibujarTablero(){
		if(nodos!=null){
			Graphics g=tablero.getGraphics();
			tableroPrint.getLineas().removeAllElements();
			g.translate(margen,margen);
			for(int fila=0;fila<filas;fila++){
				for(int columna=0;columna<columnas;columna++){
					g.setColor(Color.black);
					int numero=columna+fila*columnas;
					//lineas horizontales
					g.drawLine(columna*ancho_celda, 0, (columna+1)*ancho_celda, 0);
					tableroPrint.agregarLinea(columna*ancho_celda, 0, (columna+1)*ancho_celda, 0);
					if((fila>0 && nodos[numero][numero-columnas]==0)){
						g.drawLine(columna*ancho_celda, fila*alto_celda, (columna+1)*ancho_celda, fila*alto_celda);
						tableroPrint.agregarLinea(columna*ancho_celda, fila*alto_celda, (columna+1)*ancho_celda, fila*alto_celda);
					}
					g.drawLine(columna*ancho_celda, (filas)*alto_celda, (columna+1)*ancho_celda, (filas)*alto_celda);
					tableroPrint.agregarLinea(columna*ancho_celda, (filas)*alto_celda, (columna+1)*ancho_celda, (filas)*alto_celda);

					//lineas verticales
					g.drawLine(0,fila*alto_celda,0,(fila+1)*alto_celda);
					tableroPrint.agregarLinea(0,fila*alto_celda,0,(fila+1)*alto_celda);
					if(columna>0 && nodos[numero][numero-1]==0){
						g.drawLine(columna*ancho_celda,fila*alto_celda,columna*ancho_celda,(fila+1)*alto_celda);
						tableroPrint.agregarLinea(columna*ancho_celda,fila*alto_celda,columna*ancho_celda,(fila+1)*alto_celda);
					}
					g.drawLine(columnas*ancho_celda,fila*alto_celda,columnas*ancho_celda,(fila+1)*alto_celda);
					tableroPrint.agregarLinea(columnas*ancho_celda,fila*alto_celda,columnas*ancho_celda,(fila+1)*alto_celda);


					if(numero==inicio){
						g.setColor(Util.inicioColor);
						g.fillArc(columna*ancho_celda+(ancho_celda/2)-5,fila*alto_celda+(alto_celda/2)-5,10,10,0,360);
					}
					if(numero==fin){
						g.setColor(Util.finColor);
						g.fillArc(columna*ancho_celda+(ancho_celda/2)-5,fila*alto_celda+(alto_celda/2)-5,10,10,0,360);
					}
				}
			}
			g.translate(0,0);
		}
	}

	public void resolver(int inicio,int fin){
		final JFrame p=this;
		final int in=inicio;
		final int fn=fin;

		new Thread(new Runnable() {
			
			public void run() {
					Graphics g=solucion.getGraphics();
					g.translate(margen,margen);

					Vector<Nodo> resultado=grafo.DFS(in, fn);

					if(resultado!=null){
						for(int i=resultado.size()-1;i>=0;i--){
							int numero=resultado.get(i).getNumero();

							int fila=numero/columnas;
							int columna=numero%columnas;

							g.setColor(Color.green);
							g.fillRect(columna*ancho_celda, fila*ancho_celda, ancho_celda, alto_celda);
							dibujarTablero();

							try {
								Thread.sleep(67);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}else{
						JOptionPane.showMessageDialog(p, rb.getString("mensajes.no_solucion"));
					}
					g.translate(0,0);
			}
		}).start();
	}

	private void cargarLaberinto(boolean abrirEditor){
		int opcion=chooser.showOpenDialog(this);
		if(opcion==JFileChooser.APPROVE_OPTION){
			File archivo=chooser.getSelectedFile();
			leerArchivo(archivo,abrirEditor);
		}
	}

	protected void leerArchivo(File archivo,boolean abrirEditor){
		tablero.getGraphics().clearRect(0, 0, tablero.getWidth(), tablero.getHeight());
		String linea="";
		BufferedReader br;
		grafo=new Grafo();
		pintarLaberinto=!abrirEditor;
		try {
			br = new BufferedReader(new FileReader(archivo));
			filas=Integer.parseInt(br.readLine());
			columnas=Integer.parseInt(br.readLine());
			inicio=Integer.parseInt(br.readLine());
			fin=Integer.parseInt(br.readLine());
			int total=filas*columnas;
			nodos=new int[total][total];
			Nodo nuevo=null,vecinoTemp=null;
			for(int i=0;i<total;i++){
				linea=br.readLine();
				String[] numeros=linea.trim().split(" ");

				int nodoNumero=Integer.parseInt(numeros[0]);

				nuevo=grafo.procesarAgregar(nodoNumero);

				for(int j=1;j<numeros.length;j++){
					nodos[i][Integer.parseInt(numeros[j])]=1;
					vecinoTemp=null;

					int nVecino=Integer.parseInt(numeros[j]);

					vecinoTemp=grafo.procesarAgregar(nVecino);
					nuevo.getVecinos().add(vecinoTemp);
				}
			}
			tableroPrint=new Tablero();
		} catch (FileNotFoundException e) {

			Util.mostrarMensaje(this,Util.mensajes("mensajes.error.archivo_no_existe", new Object[]{archivo.getName()}));
		} catch (NumberFormatException e) {
			Util.mostrarMensaje(this,rb.getString("mensajes.error.archivo_sintaxis"));
		} catch (IOException e) {
			Util.mostrarMensaje(this,Util.mensajes("mensajes.error.archivo_no_leer",new Object[]{archivo.getName()}));
		}

		if(!abrirEditor){
			dibujarTablero();
			repaint();
		}else{
			editor=new Editor(this,filas, columnas,inicio,fin,grafo);
			editor.setVisible(true);
		}
	}

	private void mostrarEditor(){
		String cString,fString=JOptionPane.showInputDialog (rb.getString("editor.prompt.filas"), "2");
		int filas=2;
		int columnas=2;
		boolean error=false;
		try{
			filas=Integer.parseInt(fString);
			try{
				cString=JOptionPane.showInputDialog(rb.getString("editor.prompt.columnas"), "2");
				columnas=Integer.parseInt(cString);
			}catch(NumberFormatException nfe){
				error=true;
				JOptionPane.showMessageDialog(this, rb.getString("mensajes.error_leyendo_columnas"),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}catch(NumberFormatException nfe){
			error=true;
			JOptionPane.showMessageDialog(this, rb.getString("mensajes.error_leyendo_filas"),"Error",JOptionPane.ERROR_MESSAGE);
		}

		if(!error){
			editor=new Editor(this,filas, columnas);
			editor.setVisible(true);
		}
	}

	private void imprimir(){
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setJobName(" Print Component ");

		pj.setPrintable(new Printable() {
			public int print(Graphics pg, PageFormat pf, int pageNum) {
				if (pageNum > 0) {
					return Printable.NO_SUCH_PAGE;
				}

//				Graphics2D g2 = (Graphics2D) pg;
//				g2.translate(pf.getImageableX(), pf.getImageableY());
				tableroPrint.paint(pg);
				return Printable.PAGE_EXISTS;
			}
		});
		if (pj.printDialog() == false)
			return;

		try {
			pj.print();
		} catch (PrinterException ex) {
			// handle exception
		}

	}

	
	public void paint(Graphics g) {
		super.paint(g);
		if(pintarLaberinto){
			dibujarTablero();
		}
	}
}