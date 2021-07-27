import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SObject {
    String name;
    Long x;
    Long y;
    Long width;
    Long height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return Math.toIntExact(x);
    }

    public void setX(Long x) {
        this.x = x;
    }

    public int getY() {
        return Math.toIntExact(y);
    }

    public void setY(Long y) {
        this.y = y;
    }

    public int getWidth() {
        return Math.toIntExact(width);
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public int getHeight() {
        return Math.toIntExact(height);
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public BufferedImage getImg() throws IOException {
        return ImageIO.read(new File("src/elements/" + name + "Img.png"));
    }

}