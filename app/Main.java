package app;

import gui.Principal;

import javax.swing.SwingUtilities;

public class Main{
	public static void main(String [] args){
		
		//Locale espanol=new Locale("es","VE");
		
		//Locale.setDefault(espanol);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Principal();
			}
		});
	}
}
