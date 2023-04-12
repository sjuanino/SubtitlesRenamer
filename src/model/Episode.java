package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que tiene todos los atributos de un episodio de una serie
 * @author Sonia Juanino Vega
 */
public class Episode {
    /** Título de un episodio */
    private String title;
    /** Extension de un episodio (mp4 o avi) */
    private String extension;
    /** Carpeta en la que esta alojado el episodio */
    private String folder;
    /** Lista con todos los subitulos de un episodio*/
    private ArrayList<File> subtitles;

    /** Constructor sin parámetros */
    public Episode() {
        subtitles = new ArrayList();
    }
    
    /** Constructor con parámetros */
    public Episode(String title, String extension, String folder) {
        this();
        this.title = title;
        this.extension = extension;
        this.folder = folder;
    }    

    /**
     * 
     * @return Devuelve el título del episodio
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Acepta y asigna el título de un episodio
     * @param title El título de un episodio
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /** 
     * 
     * @return Devuelve la extensión de un episodio
     */
    public String getExtension() {
        return extension;
    }
    
    /**
     * Acepta y asigna la extensión de un episodio
     * @param extension La extensión de un episodio (mp4 o avi)
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    /**
     * 
     * @return Devuelve la carpeta donde esta almacenado el archivo del episodio 
     */
    public String getFolder() {
        return folder;
    }
    
    /**
     * Acepta y asigna la carpeta donde está el archivo del episodio
     * @param folder La carpeta que contiene el episodio
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }
    
    /**
     * 
     * @return Devuelve la lista con los subtítulos de un episodio
     */
    public List<File> getSubtitles() {
        return subtitles;
    }
    
    /**
     * Acepta y asigna la lista con los subtítulos de un episodio
     * @param subtitles La lista de subtitulos de un episodio
     */
    public void setSubtitles(ArrayList<File> subtitles) {
        this.subtitles = subtitles;
    }
    
    /**
     * Agrega un subtítulo a la lista de episodios
     * @param sub El archivo con los subtítulos
     */
    public void addSubtitles(File sub) {
        subtitles.add(sub);
    }
    
    /** Agrega un subtítulo a la lista de subtitulos
     * en la posición que se le indica
     * @param sub El archivo con los subtítulos
     * @param num Posición en la que agregar el archivo con los subtítulos
     */
    public void addSubtitles(File sub, int num) {
        subtitles.add(num, sub);
    }
    
    /**
     * Sobreescribe el método toString()
     * @return Devuelve los datos de un episodio
     */
    @Override
    public String toString() {
        return "Episode{" + "title=" + title 
                + ", extension=" + extension 
                + ", folder=" + folder+ "\n"
                + ", subtitles=" + subtitles 
                + '}';
    }
      
    
}
