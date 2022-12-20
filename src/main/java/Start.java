import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Start {

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(System.getenv("APPDATA")+ "/Iwilldesollesnimmer"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MainFrame mf = new MainFrame();
        mf.start();



    }


}
