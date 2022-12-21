import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class Displayer extends Window {

    private BufferedImage pic;

    public static void main(String[] args) {
        String directoryFilePath = "/Users/wsartin/Downloads/jpgPosters/";
        GraphicsDevice screen = setUp();
        try {
            showPoster(directoryFilePath, screen);
        } catch(Exception e) {
            System.out.println("IDK");
        }
    }

    private static GraphicsDevice setUp() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = ge.getDefaultScreenDevice();

        if (!screen.isFullScreenSupported()) {
            System.out.println("Full screen mode not supported");
            System.exit(1);
        }
        return screen;
    }

    private static void showPoster(String directoryFilePath, GraphicsDevice screen) {

        try {
            File currentDirectory = new File(directoryFilePath);
            File[] files = currentDirectory.listFiles();

            assert files != null;
            for (File file : files) {
                if (file.isFile() && !file.isHidden()) {
                    System.out.println("FILE: "+ file.toPath());
                    BufferedImage loadedpic2 = ImageIO.read(file);
                    BufferedImage rotated2 = rotate(loadedpic2);
                    screen.setFullScreenWindow(new Displayer(rotated2));
                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public Displayer(BufferedImage pic) {
        super(new Frame());

        this.pic = pic;

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                exitApplication();
            }
        });
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        // Draw the image
        g.drawImage(pic, 0, 0, getWidth(), getHeight(), this);
    }

    private void exitApplication() {
        System.exit(0);
    }

    private static BufferedImage rotate(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, image.getType());

        Graphics2D g = newImage.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((height - width) / 2, (height - width) / 2);
        at.rotate(Math.toRadians(90), height / 2, width / 2);
        g.setTransform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return newImage;
    }
}
