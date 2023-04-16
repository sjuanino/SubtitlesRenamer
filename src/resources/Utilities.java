package resources;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableModel;

public final class Utilities {
    
    private static int x = 0;
    private static int y = 0;
    
    /**
     * 
     * @param nameFile nombre del archivo
     * @return Devuelve un array de dos elementos  con el nombre de un archivo
     * y la extensión del mismo
     */
    public static String[] returnExtension(String nameFile) {
        int index = nameFile.lastIndexOf('.');
        String[] ext = new String[2];
        if (index > 0) {
            ext[0] = nameFile.substring(index + 1);
            ext[1] = nameFile.substring(0, index);
        }
        return ext;
    }
    
    /**
     * 
     * @param nameFile nombre del archivo
     * @return Devuelve el nombre de un archivo sin la extensión
     */
    public static String returnName(String nameFile) {
        return nameFile.substring(0, nameFile.lastIndexOf('.'));
    }
    
    /**
     * 
     * @param directoryPath La ruta del directorio
     * @return Devuelve todos los archivos de un directorio, y sus subdirectorios
     */
    public static List<File> listf(String directoryPath) {
        File directory = new File(directoryPath);
        List<File> resultList = new ArrayList<>();
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        for (File file : fList) {
            if (file.isDirectory()) {
                 resultList.addAll(listf(file.getAbsolutePath()));
            }
        }            
        return resultList;
    } 
    
    
    /**
     * 
     * @param directoryPath Nombre de la carpeta donde están los archivos
     * @return Devuelve los archivos que sean archivos (no los subdirectorios) 
     * que haya en una carpeta
     */
    public static List<File> listFilesDir(String directoryPath) {
        File directory = new File(directoryPath);
        List<File> resultList = new ArrayList<>();
        File[] fList = directory.listFiles();
        for (File f : fList){
            if (f.isFile()) {
                resultList.add(f);
            }
        }
        return resultList;
    }

    /**
     * 
     * @param nameFile Archivo del que se quieren buscar los subtítulos
     * @param directoryPath Ruta del directorio 
     * @return Devuelve una lista con los archivos de subtítulos
     */
    public static List<File> getlistSubtitles(String nameFile, String directoryPath) {
        ArrayList<File> listSubtitles = new ArrayList<>();
        searchSubtitles(nameFile, directoryPath, listSubtitles);
        return listSubtitles;
    }
    
    /**
     * Se busca dentro de una carpeta, un subdirectorio que tenga el mismo nombre
     * que el nombre de un archivo, y en caso de encontrarlo, se rellena una lista
     * con todos los elementos que contiene el subdirectorio
     * @param nameFile Nombre del archivo
     * @param directoryPath Ruta a la carpeta seleccionada
     * @param listSubtitles Lista de archivos de susbtitulos
     */
    public static void searchSubtitles(String nameFile, String directoryPath, ArrayList<File> listSubtitles) {
        File directory = new File(directoryPath);
        File[] fList = directory.listFiles();        
        for (File f : fList){
            if (f.isDirectory()) {
                if (f.getName().equals(nameFile)) {
                    for (File file : f.listFiles()) {
                        listSubtitles.add(file);
                    }
                } else {
                    searchSubtitles(nameFile, f.getAbsolutePath(), listSubtitles);
                }
            }
        }
    }
    
    /**
     * Abre un archivo de subtítulos y lo lee, de forma que despues de cada linea
     * se agrega "\r\n"
     * @param subFile El archivo de subtítulos
     * @param epTitle Título del episodio
     * @param epDirectory Directorio del episodio
     */
    public static void transformSrtFile(File subFile, String epTitle, String epDirectory) {
        BufferedReader reader = null;
        String line = null;
        StringBuilder srt = new StringBuilder();
        
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(subFile), "UTF-8"));
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println(ex);
        }

        try {
            while ((line = reader.readLine()) != null) {
                srt.append(line);
                srt.append("\r\n"); // Hay que poner \r\n para que lo reconozca como un archivo Windows
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        srt.delete(srt.length()-1, srt.length()); // remove '\n' or space from end string
        String doc = srt.toString();
            
        try{    
            FileWriter fw = new FileWriter(epDirectory + "\\" + epTitle + ".srt");    
            fw.write(doc);    
            fw.close();    
        } catch (IOException ex) {
            System.out.println(ex);
        } 
    }
    
    /**
     * Copia un archivo de una fuente a otro destino
     * @param source Archivo fuente
     * @param dest Archivo destino
     */
    public static void copyOneFile(File source, File dest) {
        try {
            Files.copy(source.toPath(), dest.toPath()); 
        } catch (IOException e) { 
            e.printStackTrace();
        }

    }
    
    /**
     * Crea los JRadioButtons para todos los subtítulos que se le hayan 
     * agregado a un objeto Episodio
     * @param aList Lista con los subtítulos
     * @param epTitle Título del episodio
     * @param epDir Directorio del episodio
     * @param panel Panel en el que se van a agregar los JRadioButton
     * @param label Etiqueta que indica cuando se ha hecho click en  uno de los
     * JRadioButtons y se transformado el archivo de subtítulos 
     * @param btnGroup ButtonGroup al que se van a añadir los JRadioButtons, para 
     * que solo se pueda elegir uno de ellos
     */
    public static void createRadioButtons(List<File> aList, String epTitle,
            String epDir, JPanel panel, JLabel label, ButtonGroup btnGroup) {

        for (int i = 0; i < aList.size(); i++) {
            File f = aList.get(i);
            JRadioButton rButton = new JRadioButton(f.getName() + " (" + (f.length() / 1000) + " KB)");
            btnGroup.add(rButton);
            if (i == 0) {
                x = 0;
            } else if (i % 3 != 0) {
                x++;
            } else {
                x = 0;
                y++;
            }
            addItem(panel, rButton, x, y, 1, 1, GridBagConstraints.FIRST_LINE_START);
            panel.revalidate();
            panel.repaint();

            rButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Utilities.transformSrtFile(f, epTitle, epDir);
                    label.setVisible(true);
                    label.setText("Subtitle renamed");
                }
            });
        }

    }
    
    /**
     * Añade a un panel con layout GridBagLayout un componente
     * (en este caso un JRadioButton)
     * @param p El panel
     * @param c El componente a agregar al panel
     * @param x Coordenada x de la celda a la que se va a añadir el componente
     * @param y Coordenada y de la celda a la que se va a añadir el componente
     * @param width Anchura para el componente
     * @param height Altura para el componente
     * @param align Alinación del componente
     */
    private static void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.weightx = 100.0;
        gc.weighty = 100.0;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = align;
        gc.fill = GridBagConstraints.NONE;
        p.add(c, gc);
    }
    
    /**
     * Elimina todos los JRadioButtons de un panel
     * @param panel El panel
     */
    public static void removeRadioButtons(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }
    
    /** 
     * Rellena una JTable con los nombres de los archivos de video
     * @param files Los archivos de video
     * @param tableModel El modelo de la JTable
     */
    public static void fillJtable(List<File> files, DefaultTableModel tableModel) {
        for(int i = 0; i<files.size(); i++) {
            String[] row = new String[1]; // Se crea una fila para añadir a la jTable
            // Se devuelve el titulo y la extension
            String[] titles = Utilities.returnExtension(files.get(i).getName());
            if (titles[0].equals("mp4") || titles[0].equals("avi")) {                   
                row[0] = files.get(i).getName();
                tableModel.addRow(row);
            }
        }            
    }
    
} // End Utilities class
