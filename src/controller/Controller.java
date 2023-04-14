package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultEditorKit;
import model.Episode;
import model.Season;
import resources.Utilities;
import view.SeriesGUI;

public class Controller {
    
    /** Objeto de SeriesGui -> View */
    private static SeriesGUI frame;
    /* Objetos de Episode y Season -> Model */
    private static Episode episode;
    private static Season season;
    
    /** Constructor privado para que no se pueda instancia la clase */
    private Controller() {
    }
    
    /**
     * 
     * @return Devuelve un episodio
     */
    public Episode getEpisode() {
        return episode;
    }

    /**
     * Acepta y asigna un episodio
     * @param episode El episodio
     */
    public void setEpisode(Episode episode) {
        Controller.episode = episode;
    } 
    
    /**
     * 
     * @return Devuelve una temporada de episodios
     */
    public Season getSeason() {
        return season;
    }
    
    /**
     * Acepta y asigna una temporada de episodios
     * @param season La temporada de episodios
     */
    public void setSeason(Season season) {
        Controller.season = season;
    }    
    
    /**
     * Inicia la aplicación dandole un LookAndFeelInfo determinado
     */
    public static void start() {       
        frame = new SeriesGUI();
        season = new Season();
        try{ 
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            java.util.logging.Logger.getLogger(SeriesGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }
    
    //========================================================================//
    // UN EPISODIO                                                            //
    //========================================================================// 
    
    /**
     * Después de que se seleccione una carpeta donde esta el episodio,
     * el método busca los archivos de subtitulos que hay en la carpeta
     * y los añade al objeto episodio, al igual que el titulo, la extensión y
     * la carpeta donde esta el archivo de video del episodio
     */
    public static void searchEpisode() {
        int contador = 0;
        int eng = 0;
        episode = new Episode();
        // Se devuelve una opción despues de haber seleccionado una carpeta
        // en el JFileChooser
        int option = openFileChooser("Select episode", new File(frame.getTxtDirectory().getText()));
        if (option == JFileChooser.APPROVE_OPTION) {
            // Se eliminan todos los JRadioButtons con el nombre de los subtítulos
            Utilities.removeRadioButtons(frame.getPnlRadioButtons());
            
            // Se añade directorio que se ha seleccionad en el JFileChooser
            // al JTextField en el objeto frame
            File episodeDirectory = frame.getFileChooser().getSelectedFile();
            frame.getTxtDirectory().setText(episodeDirectory.toString());
            
            // Se introducen en una lista todos los archivos del directorio elegido
            // y sus subdirectorios
            List<File> listFile = Utilities.listf(episodeDirectory.toString());
            // Se recorre la lista de los archivos de la lista
            for (File file : listFile) {
                // Si el archivo es un archivo se introducen los datos
                // dentro del objeto Episodio
                if (file.isFile()) { 
                    frame.getjListModelo().addElement(file.getName());
                    String[] extension = Utilities.returnExtension(file.getName());
                    if (extension[0].equals("mp4") || extension[0].equals("avi")) {
                        contador++;
                        episode.setTitle(extension[1]);
                        episode.setExtension(extension[0]);
                        episode.setFolder(file.getAbsoluteFile().getParent());
                    }
                    // Si el archivo es uno de subtítulos (.srt) se introduce
                    // en la lista de subtítulos del episodio
                    if (extension[0].equals("srt")) {
                        // Se distingue entre los subtítulos que tienen "English" en el ´nombre
                        // Estos deben aparecen primero entre los JRadioButtons
                        if (file.getName().contains("English")) {                            
                            episode.addSubtitles(file, eng);
                            eng++;
                        } else {
                            episode.addSubtitles(file);
                        }
                    }
                    // Si hay mas de un archivo de video se lanza una excepción
                    if (contador > 1) {
                        throw new RuntimeException("No puede haber más que un archivo de video.");
                    }
                }
            }
            // Se crean los JRadioButtons para cada uno de los subtítulos que aparecen en la lista
            Utilities.createRadioButtons(episode.getSubtitles(), episode.getTitle(), episode.getFolder(),
                    frame.getPnlRadioButtons(), frame.getLblProceso(), frame.getBtnGroupEpisode());

            frame.revalidate();
            frame.repaint();
            frame.pack();
        }
    }
    
    //========================================================================//
    // UNA TEMPORADA                                                          //
    //========================================================================// 
    
    /**
     * Busca todos los archivos de episodios y los añade a la lista de episodios que contiene
     */
    public static void searchSeason() {
        // Se devuelve una opción despues de haber seleccionado una carpeta
        // en el JFileChooser
        int option = openFileChooser("Seleccionar Temporada", new File(frame.getTxtDirEpisodes().getText()));
        if (option == JFileChooser.APPROVE_OPTION) {
            // Se eliminan todos los JRadioButtons con el nombre de los subtítulos
            Utilities.removeRadioButtons(frame.getPnlTempRadioBtns());
            // El getSelectedFile() hace que se pueda seleccionar el directorio
            // y que aparezca en el cuadro de texto
            File seasonDirectory = frame.getFileChooser().getSelectedFile();
            frame.getTxtDirEpisodes().setText(seasonDirectory.toString());
            
            // Se introduce en una lista todos los archivos que hay en la carpeta elegida
            // pero solo los "Files", no se tiene en cuenta los subdirectorios
            List<File> episodes = Utilities.listFilesDir(frame.getTxtDirEpisodes().getText());
            // Se recorre la lista
            for (File ep : episodes) {
                // Si el archivo es un "File" no un subdirectorio
                // Se crea un objeto episodio que almacenara todos los datos del mismo
                if (ep.isFile()) {
                    Episode episodeSeason = new Episode();
                    String[] extension = Utilities.returnExtension(ep.getName());
                    if (extension[0].equals("mp4") || extension[0].equals("avi")) {
                        episodeSeason.setTitle(extension[1]);
                        episodeSeason.setExtension(extension[0]);
                        episodeSeason.setFolder(ep.getAbsoluteFile().getParent());
                        // Se buscan los subtítulos en la carpeta asociada al
                        // archivo de video
                        ArrayList<File> subtitles
                                = (ArrayList<File>) Utilities.getlistSubtitles(
                                        episodeSeason.getTitle(), episodeSeason.getFolder());
                        episodeSeason.setSubtitles(subtitles);
                        season.addEpisode(episodeSeason);
                    }
                }
            }
            // Se rellena la JTable con los nombres de los episodios
            Utilities.fillJtable(episodes, frame.getjTableModelo());
        }
    }
    
    /**
     * Al pulsar en una de las celdas de la JTable, crea los JRadioButtons
     * con los nombres de los subtitulos que tiene asociado el objeto Episodio,
     * después de buscarlo en la lista con todos los episodios de la temporada
     * @param epClicked Episodio en el que se ha hecho click con el ratón
     * en la JTable de la GUI
     */
    public static void episodeClicked(String epClicked) {
        int eng = 0;
        String[] extension = Utilities.returnExtension(epClicked);
        ArrayList<File> epSubs = new ArrayList();
        for (Episode e : season.getSeason()) {
            if (extension[1].equals(e.getTitle())) {
                for (File file : e.getSubtitles()) {
                    if (file.getName().contains("English")) {
                        epSubs.add(eng, file);
                        eng++;
                    } else {
                        epSubs.add(file);
                    }
                }
                Utilities.createRadioButtons(epSubs, e.getTitle(),
                        e.getFolder(), frame.getPnlTempRadioBtns(),
                        frame.getLblFinProcesoSubs(), frame.getBtnGroupSeason());
            }
        }
        frame.revalidate();
        frame.repaint();
        frame.pack();
    }
    
    /**
     * Abre un JFileChooser, en el que se va a elegir la carpeta en la que está
     * el episodio o la temporada de episodios
     * @param title Titulo que aparecera en la parte superior de la ventana del 
     * JFileChooser
     * @param currentDir Directorio actual
     * @return Devuelve un entero que indica la opción que se ha elegido, OK o Cancelar
     */
    public static int openFileChooser(String title, File currentDir) {
        frame.getFileChooser().setDialogTitle(title);
        frame.getFileChooser().setCurrentDirectory(currentDir);
        
        // Sólo se puede elegir una carpeta, para ello son las dos instrucciones
        frame.getFileChooser().setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        frame.getFileChooser().setAcceptAllFileFilterUsed(false);
        
        // Se hace un menu contextual con la opcion de pegar
        JPopupMenu menu = new JPopupMenu();
        Action pegar = new DefaultEditorKit.PasteAction();
        pegar.putValue(Action.NAME, "Paste");
        menu.add(pegar);        
        frame.getFileChooser().setComponentPopupMenu(menu);
        
        return frame.getFileChooser().showOpenDialog(frame);
    }
} // End class Controller 
