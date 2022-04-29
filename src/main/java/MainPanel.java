import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private int x,y;
    public MainPanel () {
        setLayout(null);
        setBackground(Color.darkGray);
        x = 400;
        y = 400;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(x,y,25,25);
        g.drawOval(x,y,50,50);
        g.drawOval(x,y,75,75);
        g.drawOval(x,y,100,100);
    }
}
