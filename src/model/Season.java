package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una temporada de una serie.
 * Se compone de una lista donde se almacenan episodios
 * @author Sonia Juanino Vega
 */
public class Season {
    
    /** Lista que almacena los episodios de una temporada */
    List<Episode> season;
    
    /** Constructor sin parametros */
    public Season() {
        this.season = new ArrayList();
    }

    /**
     * 
     * @return Devuelve la lista de episodios
     */
    public List<Episode> getSeason() {
        return season;
    }

    /** 
     * Acepta y asigna una lista de episodios
     * @param season La lista de episodios
     */
    public void setSeason(List<Episode> season) {
        this.season = season;
    }
    
    /**
     * Añade un episodio a la lista de episodios
     * @param episode El episodio que se añade a la lista
     */
    public void addEpisode(Episode episode) {
        this.getSeason().add(episode);
    }

} //End class Season
