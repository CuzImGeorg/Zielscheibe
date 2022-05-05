import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainPanel extends JPanel {

    private ArrayList<ToDraw> toDraw = new ArrayList<>();
    private ArrayList<Bild> waitingBilder = new ArrayList<>();
    private Bild currentBild;

    private boolean gamestart = false;

    private Random rdm = new Random();


    private int x,y;
    public MainPanel () {
        setLayout(null);
        setBackground(Color.darkGray);
        setSize(1000,1000);
        x = 400;
        y = 400;
        update();
        mouseListner();
        mainKnife();
        firstCreateImg();

    }

    public void update() {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(()-> {
            updateUI();
        },0,4,TimeUnit.MILLISECONDS);
    }

    public void mainKnife() {
        Knife f = new Knife(800,600,400,400);
        toDraw.add(f);
    }


    public void firstCreateImg() {
        AtomicInteger i = new AtomicInteger(0);
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(()-> {
            if(i.get()<9) {
                Bild b = new Bild(3, 30, 980);
                waitingBilder.add(b);
                toDraw.add(b);
                b.moveup(960-(100*i.get()));
                i.getAndIncrement();
            }else {
                ses.shutdownNow();
                gamestart = true;
                movein();

            }
        },0,1, TimeUnit.SECONDS);
    }

    public void movein() {
        waitingBilder.get(0).moveIN(120);
        currentBild = waitingBilder.get(0);
        waitingBilder.remove(0);
        for(Bild t : waitingBilder) {
            t.moveup(100);
        }
    }

    public void doTheCollisionThing(Knife k) {
        if(k.getX() > currentBild.getX() &&
            k.getX() < currentBild.getX()+80 &&
            k.getY() > currentBild.getY()&&
            k.getY() < currentBild.getY()+80
        ){
            toDraw.remove(k);
            currentBild.movedown(1000);
            currentBild.setDead(true);

            movein();
            Bild b = new Bild(3,30,730);
            waitingBilder.add(b);
            toDraw.add(b);

        }else {

        }
    }
    private boolean can = true;
    public void mouseListner() {

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(can) {
                    if (gamestart) {
                        Knife k = new Knife(e.getX(), e.getY(), 80, 80);
                        toDraw.add(k);
                        doTheCollisionThing(k);
                    }
                    can = false;
                    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
                    ses.schedule(()-> {
                        can = true;
                    },1, TimeUnit.SECONDS);
                }

            }
            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (ToDraw t : toDraw) {
            t.draw(g);
        }

        //DRAW SEPERATOR
        g.drawLine(140,0,140,1000);
        g.drawLine(141,0,141,1000);

    }


}
