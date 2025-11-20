// import ui.MainWindow;
import structures.TaskManagerCLI;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
     
        // SwingUtilities.invokeLater(() -> {
        //     MainWindow mainWindow = new MainWindow();
        //     mainWindow.setVisible(true);
        // });

        TaskManagerCLI app = new TaskManagerCLI();
        app.start();


    }
}