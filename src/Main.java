import ui.LoginFrame;
import javax.swing.*;

/**
 * Clase Main - Inicia la interfaz gráfica del sistema
 */
public class Main {
    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        }); 
    }
}

