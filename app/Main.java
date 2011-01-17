package app;

import gui.Principal;
import gui.Util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

public class Main{
	public static void main(String [] args){
		try{
			Util.resource=ResourceBundle.getBundle("resources.textos", Locale.getDefault());
		}catch(MissingResourceException mre){
			Locale.setDefault(new Locale("en"));
			Util.resource=ResourceBundle.getBundle("resources.textos", Locale.getDefault());
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Principal();
			}
		});
	}
}
