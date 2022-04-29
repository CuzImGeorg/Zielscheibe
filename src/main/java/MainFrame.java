import javax.swing.*;

public class MainFrame extends JFrame {

    public void start() {
        setTitle("Zielscheibe");
        setSize(1200,1000);
        setContentPane(new MainPanel());
        setVisible(true);



    }
}
