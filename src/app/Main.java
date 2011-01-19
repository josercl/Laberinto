package app;

import gui.Principal;
import gui.Util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            Util.resource = ResourceBundle.getBundle("resources.textos", Locale.getDefault());
        } catch (MissingResourceException mre) {
            Locale.setDefault(new Locale("en"));
            Util.resource = ResourceBundle.getBundle("resources.textos", Locale.getDefault()); 
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new Principal();
            }
        });
    }
}
