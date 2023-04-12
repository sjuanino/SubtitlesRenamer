import controller.Controller;
import java.awt.EventQueue;

/**
 * Clase que inicia la aplicación
 * @author Sonia Juanino Vega
 */
public class Runner {

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Controller.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
