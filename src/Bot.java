import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class Bot extends JFrame {
    public int counter = 0;
    public Point first = new Point();
    public Point second = new Point();
    Robot bot = new Robot();
    JSONParser parser = new JSONParser();

    public Bot() throws AWTException {
        super("БОТ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 100);
        setVisible(true);

    }

    public SObject getObject(String name) throws IOException, ParseException {
        File file = new File("src/elements/" + name + ".txt");
        FileReader fileReader = new FileReader(file);
        Scanner sacan = new Scanner(fileReader);
        Object obj = parser.parse(sacan.nextLine());
        JSONObject jobj = (JSONObject) obj;
        SObject sobj = new SObject();
        sobj.setName((String) jobj.get("name"));
        sobj.setX((Long) jobj.get("x"));
        sobj.setY((Long) jobj.get("y"));
        sobj.setWidth((Long) jobj.get("width"));
        sobj.setHeight((Long) jobj.get("height"));
        return sobj;
    }

    public void createScreenShoot(String name) {
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (counter) {
                    case 0:
                        first.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
                        System.out.println("first: x= " + first.x + "//" + "y= " + first.y);
                        break;
                    case 1:
                        second.setLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
                        System.out.println("second: x= " + second.x + "//" + "y= " + second.y);
                        break;
                    case 2:
                        try {
                            createScreen(name);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                }
                //  System.out.println("x= " + e.getX() + "//" + "y= " + e.getY());
                counter++;
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

    public void createScreen(String name) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("x", first.x);
        obj.put("y", first.y);
        obj.put("width", second.x - first.x);
        obj.put("height", second.y - first.y);

        FileWriter fileWriter = new FileWriter("src/elements/" + name + ".txt");
        fileWriter.write(obj.toJSONString());
        fileWriter.flush();
        fileWriter.close();

        BufferedImage img = new BufferedImage(second.x - first.x, second.y - first.y, BufferedImage.TYPE_INT_RGB);
        BufferedImage screen = bot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

        for (int i = first.x; i < second.x; i++) {
            for (int j = first.y; j < second.y; j++) {
                img.setRGB(i - first.x, j - first.y, screen.getRGB(i, j));
            }
        }
        File fl = new File("src/elements/" + name + "Img.png");
        ImageIO.write(img, "png", fl);

        System.out.println("Done");
    }

    public void moveMouse(int x, int y) throws InterruptedException {
        bot.mouseMove(x,y);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(200);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static void main(String[] args) throws AWTException, IOException, ParseException {
        Bot bt = new Bot();
        bt. createScreenShoot("enemyHelth");

    }
}