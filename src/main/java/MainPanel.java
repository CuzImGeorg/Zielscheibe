import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    private ArrayList<File> files = new ArrayList<>();
    private boolean gamestart = false;
    private  String OS = System.getProperty("os.name").toLowerCase();

    private Random rdm = new Random();


    private int x,y;
    public MainPanel () {
        setLayout(null);
        setBackground(Color.darkGray);
        setSize(1000,1000);
        x = 400;
        y = 400;
        File[] file = null;
        if(OS.startsWith("win")) {
            file = new File(System.getenv("APPDATA")+ "/Iwilldesollesnimmer").listFiles();
        }else {
            file = new File( "/opt/Iwilldesollesnimmer").listFiles();

        }

        for(File f : file) {
            files.add(f);
        }

        JButton b = new JButton();
        b.setText("Upload img");
        b.setBounds(900,900,100, 40);
        b.setBackground(Color.CYAN);
        Thread t = new Thread(()-> {
            b.addActionListener((l)-> {
                JFileChooser chooser = new JFileChooser();

                int status = chooser.showOpenDialog(null);
                if (status == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    if (f == null) {
                        return;
                    }
                    files.add(f);
                    var source = f;
                    File dest = null;
                    if(OS.startsWith("win")) {
                        dest = new File(System.getenv("APPDATA")+ "/Iwilldesollesnimmer/" + f.getName());
                    }else {
                        dest = new File( "/opt/Iwilldesollesnimmer/" + f.getName());
                    }

                    try {
                        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        });
        t.start();

        add(b);

        Thread t2 = new Thread(() -> {
            while (files.size() == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            update();
            mouseListner();
            mainKnife();

            firstCreateImg();
        });
        t2.start();



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
//                Bild b = new Bild(2, 30, 980);
                Bild b = new Bild(files.get(rdm.nextInt(files.size())), 30, 980);
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
        currentBild.moveToMid();
        currentBild.moveRdm();
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
            currentBild.movedown(1500);
            currentBild.setDead(true);


            movein();
//            Bild b = new Bild(2,30,730);
            Bild b = new Bild(files.get(rdm.nextInt(files.size())),30,730);
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
