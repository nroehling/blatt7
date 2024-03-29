import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Bildbetrachter ist die Hauptklasse der Bildbetrachter-Anwendung. Sie
 * erstellt die GUI der Anwendung, zeigt sie an und initialisiert alle
 * anderen Komponenten.
 * 
 * Erzeugen Sie ein Exemplar dieser Klasse, um die Anwendung zu starten.
 * 
 * @author Michael K�lling und David J. Barnes 
 * @version 1.0
 */
public class Bildbetrachter
{
    // statische Datenfelder (Klassenkonstanten und -variablen)
    private static final String VERSION = "Version 1.0";
    private static JFileChooser dateiauswahldialog = new JFileChooser(System.getProperty("user.dir"));

    // Datenfelder
    private JFrame fenster;
    private Bildflaeche bildflaeche;
    private JLabel dateinameLabel;
    private JLabel statusLabel;
    private Farbbild aktuellesBild;
    /**
     * Erzeuge einen Bildbetrachter und zeige seine GUI auf
     * dem Bildschirm an.
     */
    public Bildbetrachter()
    {
        aktuellesBild = null;
        fensterErzeugen();
    }

    // ---- Implementierung der Men�-Funktionen ----
    
    /**
     * 'Datei oeffnen'-Funktion: �ffnet einen Dateiauswahldialog zur 
     * Auswahl einer Bilddatei und zeigt das selektierte Bild an.
     */
    private void dateiOeffnen()
    {
        int ergebnis = dateiauswahldialog.showOpenDialog(fenster);

        if(ergebnis != JFileChooser.APPROVE_OPTION) { // abgebrochen
            return;  
        }
        File selektierteDatei = dateiauswahldialog.getSelectedFile();
        aktuellesBild = BilddateiManager.ladeBild(selektierteDatei);
        
        if(aktuellesBild == null) {   // Bilddatei nicht im g�ltigen Format
            JOptionPane.showMessageDialog(fenster,
                    "Die Datei hat keines der unterst�tzten Formate.",
                    "Fehler beim Bildladen",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        bildflaeche.setzeBild(aktuellesBild);
        dateinameAnzeigen(selektierteDatei.getPath());
        statusAnzeigen("Datei geladen.");
        fenster.pack();
    }

    /**
     * 'Schliessen'-Funktion: Schlie�t das aktuelle Bild.
     */
    private void schliessen()
    {
        aktuellesBild = null;
        bildflaeche.loeschen();
        dateinameAnzeigen(null);
    }
    
    
    /**
     * 'Beenden'-Funktion: Beendet die Anwendung.
     */
    private void beenden()
    {
        System.exit(0);
    }
    
    // ---- Hilfsmethoden ----

    /**
     * Zeigt den Dateinamen des aktuellen Bildes auf dem Label f�r den
     * Dateinamen.
     * Der Parameter sollte 'null' sein, wenn kein Bild geladen ist. 
     * 
     * @param dateiname  Der anzuzeigende Dateiname, oder null f�r 'keine Datei'.
     */
    private void dateinameAnzeigen(String dateiname)
    {
        if(dateiname == null) {
            dateinameLabel.setText("Keine Datei angezeigt.");
        }
        else {
            dateinameLabel.setText("Datei: " + dateiname);
        }
    }
    
    
    /**
     * Zeige den gegebenen Text in der Statuszeile am unteren
     * Rand des Fensters.
     * @param text der anzuzeigende Statustext.
     */
    private void statusAnzeigen(String text)
    {
        statusLabel.setText(text);
    }
    
    
    // ---- Swing-Anteil zum Erzeugen des Fensters mit allen Komponenten ----
    
    /**
     * Erzeuge das Swing-Fenster samt Inhalt.
     */
    private void fensterErzeugen()
    {
        fenster = new JFrame("Bildbetrachter");
        menuezeileErzeugen(fenster);
        
        Container contentPane = fenster.getContentPane();
        
        // Ein Layout mit h�bschen Abst�nden definieren
        contentPane.setLayout(new BorderLayout(6, 6));
        
        // Die Bildfl�che in der Mitte erzeugen
        bildflaeche = new Bildflaeche();
        contentPane.add(bildflaeche, BorderLayout.CENTER);

        // Zwei Labels oben und unten f�r den Dateinamen und Statusmeldungen
        dateinameLabel = new JLabel();
        contentPane.add(dateinameLabel, BorderLayout.NORTH);

        statusLabel = new JLabel(VERSION);
        contentPane.add(statusLabel, BorderLayout.SOUTH);
        
        // Aufbau abgeschlossen - Komponenten arrangieren lassen
        dateinameAnzeigen(null);
        fenster.pack();
        
        // Das Fenster in der Mitte des Bildschirms platzieren und anzeigen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        fenster.setLocation(d.width/2 - fenster.getWidth()/2, d.height/2 - fenster.getHeight()/2);
        fenster.setVisible(true);
    }
    
    /**
     * Die Men�zeile des Hauptfensters erzeugen.
     * @param fenster  Das Fenster, in das die Men�zeile eingef�gt werden soll.
     */
    private void menuezeileErzeugen(final JFrame fenster)
    {
        final int SHORTCUT_MASK =
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        JMenuBar menuezeile = new JMenuBar();
        fenster.setJMenuBar(menuezeile);
        
        JMenu menue;
        JMenuItem eintrag;
        
        // Das Datei-Men� erzeugen
        menue = new JMenu("Datei");
        menuezeile.add(menue);
        
        eintrag = new JMenuItem("�ffnen...");
            eintrag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
            eintrag.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { dateiOeffnen(); }
                           });
        menue.add(eintrag);

        eintrag = new JMenuItem("Schlie�en");
            eintrag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, SHORTCUT_MASK));
            eintrag.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { schliessen(); }
                           });
        menue.add(eintrag);
        menue.addSeparator();

        eintrag = new JMenuItem("Beenden");
            eintrag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
            eintrag.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { beenden(); }
                           });
        menue.add(eintrag);


        // Das Filter-Men� erzeugen
        menue = new JMenu("Filter");
        menuezeile.add(menue);
                    
        eintrag = new JMenuItem("Dunkler");
        eintrag.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) { 
                               DunkelFilter a = new DunkelFilter(aktuellesBild);
                               a.anwenden();
                               fenster.repaint();
                            }
                       });
         menue.add(eintrag);

        eintrag = new JMenuItem("Heller");
        eintrag.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) { 
                            	
                            	AufhellenFilter x = new AufhellenFilter(aktuellesBild);
                            	x.anwenden();
                            	fenster.repaint();
                      
                            }
                       });
         menue.add(eintrag);

        eintrag = new JMenuItem("Schwellwert");
        eintrag.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) { 
                                SchwellwertFilter b = new SchwellwertFilter(aktuellesBild);
                                b.anwenden();
                                fenster.repaint();
                            }
                       });
         menue.add(eintrag);

        
    }
}
