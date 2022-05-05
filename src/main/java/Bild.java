import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Bild implements ToDraw{

    private int x,y;
    private BufferedImage img, bloodimg;
    private Knife k;

    private boolean dead = false;

    public Bild(int img, int x, int y ) {
        try {
            this.img = ImageIO.read(getClass().getResource("/" + img+".png"));
            this.bloodimg = ImageIO.read(getClass().getResource("/blood.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.x = x;
        this.y = y;
        k = new Knife(x+30,y+30,50,50);

    }


    public void moveup(int up) {
        AtomicInteger i = new AtomicInteger(0);
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(()-> {
            if(i.get()<up){
                y--;
                k.setY(k.getY()-1);
                i.getAndIncrement();
            }else {
                ses.shutdownNow();
            }
        },0,2, TimeUnit.MILLISECONDS);
    }

    public void moveIN(int in) {
        AtomicInteger i = new AtomicInteger(0);
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(()->{
            if(i.get()<in){
                x++;
                k.setX(k.getX()+1);
                i.getAndIncrement();
            }else {
                ses.shutdownNow();
            }
        },0,2,TimeUnit.MILLISECONDS);
    }

    public void movedown(int down) {
        AtomicInteger i = new AtomicInteger(0);
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(()-> {
            if(i.get()<down){
                y++;
                k.setY(k.getY()+1);
                i.getAndIncrement();
            }else {
                ses.shutdownNow();
            }
        },0,2500, TimeUnit.MICROSECONDS);
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

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, x,y,80,80,null);
        if(dead) {
            g.drawImage(bloodimg,x,y,80,80,null);
            k.draw(g);
        }
    }
}
