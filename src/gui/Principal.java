package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import logic.Generador;
import logic.Grafo;
import logic.Nodo;

public class Principal extends JFrame {
	private static final long serialVersionUID = 1L;
	private Tablero tableroPrint;
	private Generador generador;
	public JFileChooser chooser;
	private Editor editor;
	private JButton resolver,dibujar;

	final int margen=20;
	int ancho_celda=Util.ancho_celda; //ancho de cada "celda" del laberinto
	int alto_celda=Util.alto_celda; //alto de cada "celda" del laberinto
	int radio_punto=Util.radio_punto;

	private boolean pintarLaberinto=false;
	int inicio,fin,filas,columnas,nodos[][];

	private ResourceBundle rb=null;

	Grafo grafo;
	private JToolBar bar;
	
	Toolkit tk=Toolkit.getDefaultToolkit();
	Dimension screen=tk.getScreenSize();

	public Principal(){
		super();
		rb=Util.resource;	
		setTitle(rb.getString("laberinto"));
		initComponents();
	}

	private void initComponents(){
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		bar=new JToolBar();
		bar.setFloatable(false);
		dibujar=new JButton(rb.getString("toolbar.dibujar"));
		resolver=new JButton(rb.getString("toolbar.resolver"));
		JButton generar_btn=new JButton(rb.getString("menu.archivo.generar_laberinto"));

		bar.add(dibujar);bar.add(resolver);bar.add(generar_btn);

		dibujar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableroPrint.getPasos().removeAllElements();
				//dibujarTablero();
				repaint();
			}
		});

		resolver.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				resolver(inicio,fin);
				dibujarTablero();
			}
		});
		
		generar_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generar();
			}
		});
			
		JMenuBar menubar=new JMenuBar();
		JMenu archivo=new JMenu(rb.getString("menu.archivo"));
		archivo.setMnemonic(rb.getString("menu.achivo.mnemonic").charAt(0));

		JMenuItem cargar=new JMenuItem(rb.getString("menu.archivo.abrir_laberinto"));
		cargar.setAccelerator(KeyStroke.getKeyStroke(rb.getString("menu.archivo.abrir_laberinto.accelerator")));
		cargar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				cargarLaberinto(false);
			}
		});
		archivo.add(cargar);

		JMenuItem nuevo_editor_item=new JMenuItem(rb.getString("menu.archivo.nuevo_laberinto"));
		nuevo_editor_item.setAccelerator(KeyStroke.getKeyStroke(rb.getString("menu.archivo.nuevo_laberinto.accelerator")));
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

		JMenuItem generar=new JMenuItem(rb.getString("menu.archivo.generar_laberinto"));
		generar.setAccelerator(KeyStroke.getKeyStroke(rb.getString("menu.archivo.generar_laberinto.accelerator")));
		generar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generar();
			}
		});
		
		galeria_laberintos.add(generar);
		galeria_laberintos.addSeparator();
		
		for(int i=1;i<=5;i++){
			JMenuItem gmi=new JMenuItem(rb.getString("laberinto")+" "+i);
			final int m=i;
			gmi.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					InputStream is= Principal.class.getResourceAsStream("/laberinto/lab" + m + ".txt");
					
					leerArchivo(is,"lab"+m+".txt", false);
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
		mntmImprimir.setAccelerator(KeyStroke.getKeyStroke(rb.getString("menu.archivo.imprimir.accelerator")));
		archivo.add(mntmImprimir);
		archivo.add(galeria_laberintos);

		archivo.addSeparator();
		JMenuItem salir=new JMenuItem(rb.getString("menu.archivo.salir"));
		salir.setMnemonic(rb.getString("menu.archivo.salir_mnemonic").charAt(0));
		salir.setAccelerator(KeyStroke.getKeyStroke(rb.getString("menu.archivo.salir.accelerator")));
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

		tableroPrint=new Tablero();
		tableroPrint.setSize(screen);
		
		getContentPane().add(tableroPrint,BorderLayout.CENTER);

		chooser=new JFileChooser();

		int av=400;
		int alv=400;
		setBounds((screen.width-av)/2,(screen.height-alv)/2,av,alv);

		generador=new Generador(this, 0, 0);
		
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
	}

	public void dibujarTablero(){
		if(nodos!=null){
			tableroPrint.limpiar();
			tableroPrint.repaint();
			
			ancho_celda=tableroPrint.getWidth()/columnas;
			alto_celda=tableroPrint.getHeight()/filas;
			radio_punto=ancho_celda/8;
			
			for(int fila=0;fila<filas;fila++){
				for(int columna=0;columna<columnas;columna++){
					int numero=columna+fila*columnas;
					//lineas horizontales
					tableroPrint.agregarLinea(columna*ancho_celda, 0, (columna+1)*ancho_celda, 0);
					if((fila>0 && nodos[numero][numero-columnas]==0)){
						tableroPrint.agregarLinea(columna*ancho_celda, fila*alto_celda, (columna+1)*ancho_celda, fila*alto_celda);
					}
					tableroPrint.agregarLinea(columna*ancho_celda, (filas)*alto_celda, (columna+1)*ancho_celda, (filas)*alto_celda);

					//lineas verticales
					tableroPrint.agregarLinea(0,fila*alto_celda,0,(fila+1)*alto_celda);
					if(columna>0 && nodos[numero][numero-1]==0){
						tableroPrint.agregarLinea(columna*ancho_celda,fila*alto_celda,columna*ancho_celda,(fila+1)*alto_celda);
					}
					tableroPrint.agregarLinea(columnas*ancho_celda,fila*alto_celda,columnas*ancho_celda,(fila+1)*alto_celda);
					
					if(numero==inicio){
						tableroPrint.setInicio(new Circulo(columna*ancho_celda+(ancho_celda/2)-radio_punto,fila*alto_celda+(alto_celda/2)-radio_punto,radio_punto));
					}
					if(numero==fin){
						tableroPrint.setFin(new Circulo(columna*ancho_celda+(ancho_celda/2)-radio_punto,fila*alto_celda+(alto_celda/2)-radio_punto,radio_punto));
					}
				}
			}
		}
	}

	public void resolver(int inicio,int fin){
		final JFrame p=this;
		final int in=inicio;
		final int fn=fin;
		
		new Thread(new Runnable() {
			
			public void run() {
				
				Vector<Nodo> resultado=grafo.DFS(in, fn);

				if(resultado!=null){
					for(int i=resultado.size()-1;i>=0;i--){
						int numero=resultado.get(i).getNumero();

						int fila=numero/columnas;
						int columna=numero%columnas;
						
						tableroPrint.agregarPaso(columna*ancho_celda, fila*alto_celda, ancho_celda, alto_celda);
						
						tableroPrint.repaint();

						try {
							Thread.sleep(38);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					JOptionPane.showMessageDialog(p, rb.getString("mensajes.no_solucion"));
				}
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

	public void leerArchivo(InputStream inputStream,String archivo,boolean abrirEditor){
		String linea="";
		BufferedReader br;
		grafo=new Grafo();
		pintarLaberinto=!abrirEditor;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
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
		} catch (NumberFormatException e) {
			Util.mostrarMensaje(this,rb.getString("mensajes.error.archivo_sintaxis"));
		} catch (IOException e) {
			Util.mostrarMensaje(this,Util.mensajes("mensajes.error.archivo_no_leer",new Object[]{archivo}));
		}

		if(!abrirEditor){
			dibujarTablero();
			repaint();
		}else{
			editor=new Editor(this,filas, columnas,inicio,fin,grafo);
			editor.setVisible(true);
		}
	}
	
	public void leerArchivo(File archivo,boolean abrirEditor){
		try {
			this.leerArchivo(new FileInputStream(archivo),archivo.getName(), abrirEditor);
		} catch (FileNotFoundException e) {
			Util.mostrarMensaje(this,Util.mensajes("mensajes.error.archivo_no_existe", new Object[]{archivo.getName()}));
		}
	}

	private void mostrarEditor(){
		int[][] datos=preguntarFilasColumnas();
		if(datos!=null){
			editor=new Editor(this,datos[0][0], datos[0][1]);
			editor.setVisible(true);
		}
	}
	
	private int[][] preguntarFilasColumnas(){
		String cString,fString=JOptionPane.showInputDialog (rb.getString("editor.prompt.filas"), "2");
		int filas=2;
		int columnas=2;
		int[][] resultados=new int[1][2];
		boolean error=false;
		try{
			filas=Integer.parseInt(fString);
			resultados[0][0]=filas;
			try{
				cString=JOptionPane.showInputDialog(rb.getString("editor.prompt.columnas"), "2");
				columnas=Integer.parseInt(cString);
				resultados[0][1]=columnas;
			}catch(NumberFormatException nfe){
				error=true;
				JOptionPane.showMessageDialog(this, rb.getString("mensajes.error_leyendo_columnas"),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}catch(NumberFormatException nfe){
			error=true;
			JOptionPane.showMessageDialog(this, rb.getString("mensajes.error_leyendo_filas"),"Error",JOptionPane.ERROR_MESSAGE);
		}
		if(error){
			resultados=null;
		}
		return resultados;
	}

	private void imprimir(){
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setJobName("Laberinto");
		pj.setPrintable(new Printable() {
			public int print(Graphics pg, PageFormat pf, int pageNum) {
				if (pageNum > 0) {
					return Printable.NO_SUCH_PAGE;
				}
				pf.setOrientation(PageFormat.LANDSCAPE);
				
				Graphics2D g2 = (Graphics2D) pg;
				g2.translate(pf.getImageableX(), pf.getImageableY());
				tableroPrint.paint(pg);
				return Printable.PAGE_EXISTS;
			}
		});
		if (!pj.printDialog()){
			return;
		}
		try {
			pj.print();
		} catch (PrinterException ex) {
			ex.printStackTrace();
		}

	}

	private void generar(){
		int[][]datos=preguntarFilasColumnas();
		if(datos!=null){
			generador.setFilas(datos[0][0]);
			generador.setColumnas(datos[0][1]);
			generador.init();
			generador.generar();
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(pintarLaberinto){
			dibujarTablero();
		}
	}
}