import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bild {

    private int x,y;
    private BufferedImage img;

    public Bild(int img, int x, int y ) {
        try {
            this.img = ImageIO.read(getClass().getResource("/" + img+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.x = x;
        this.y = y;

    }


    public void moveup() {

    }




    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
