import map.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Just creates a new level, displays it as text and saves an image of it to img/map.png
 */
public class MainGame {

    /**
     * @param args args[0] to set a level size, if not set 13 is used
     */
    public static void main(String [ ] args){
        int size = 13;
        if (args.length>0) {
            size = Integer.valueOf(args[0]);
        }
        Level currentLevel = new Level(size);
        currentLevel.showLevelText();
        BufferedImage levelImage = currentLevel.getLevelImage();
        try {
            ImageIO.write(levelImage,"png",new File("img/map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
