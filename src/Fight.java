
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;;
import java.io.File;
import java.io.IOException;
import java.security.spec.RSAOtherPrimeInfo;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Fight {
    int healingRange = 30;

    Bot bot = new Bot();
    Robot robot = new Robot();
    Random rnd = new Random();
    Integer[] superPunch = {2,3,2,1};
    int punchCounter = 0;
    int healCounter = 0;
    int flag = 0;
    SObject heal1 = bot.getObject("heal1");
    SObject heal2 = bot.getObject("heal2");
    SObject heal3 = bot.getObject("heal3");
    SObject heal4 = bot.getObject("heal4");
    SObject heal5 = bot.getObject("heal5");
    SObject punch1 = bot.getObject("punch1");
    SObject punch2 = bot.getObject("punch2");
    SObject punch3 = bot.getObject("punch3");
    SObject mainFrame = bot.getObject("mainFrame");
    SObject end = bot.getObject("endFight");
    SObject endMark = bot.getObject("endFightMark");
    SObject error = bot.getObject("error1");
    SObject error2 = bot.getObject("error2");
    SObject target = bot.getObject("target");
    SObject isFight = bot.getObject("isFight");
    SObject isHealing = bot.getObject("isHealing");
    SObject rock = bot.getObject("rock");
    public Fight() throws AWTException, IOException, ParseException, org.json.simple.parser.ParseException {

    }

    public BufferedImage getScreen(){
       return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }

    public int compareImages(SObject obj) throws IOException {
        BufferedImage screen = getScreen();
        BufferedImage target = obj.getImg();
        int counter = 0;
        for (int i = obj.getX(); i < obj.getWidth() + obj.getX(); i++ ){
            for (int j = obj.getY(); j < obj.getHeight() + obj.getY(); j++){
              //  System.out.println(i + ":" + j + " // " + (i-obj.getX()) + ":" + (j-obj.getY()));
                if (screen.getRGB(i,j) == target.getRGB(i-obj.getX(), j-obj.getY())){
                    counter++;
                }
            }
        }

        return (counter * 100)/(obj.getHeight()*obj.getWidth());
    }

    public int getHelth() throws IOException, ParseException, org.json.simple.parser.ParseException {
        return compareImages(bot.getObject("helth"));
    }

    public int getEnemyHelth() throws IOException, ParseException, org.json.simple.parser.ParseException {
        SObject enemy = bot.getObject("enemyHelth");
        return compareImages(enemy);
    }

    public int isMyTurnToPunch() throws IOException, ParseException, org.json.simple.parser.ParseException {
        SObject turn = bot.getObject("punch2");
        return compareImages(turn);
    }

    public int isMyning() throws IOException, ParseException, org.json.simple.parser.ParseException {
        SObject myning = bot.getObject("myning");
        return  compareImages(myning);
    }

    public void isEndFight() throws IOException, InterruptedException {
        BufferedImage img = getScreen();
        File fl = new File("src/elements/screen.png");
        ImageIO.write(img, "png", fl);
        //Mat scr = null;
        Mat scr = Imgcodecs.imread("src/elements/screen.png");
        Mat temp = Imgcodecs.imread("src/elements/endFightImg.png");

        Mat outputImage=new Mat();
        // int machMethod=Imgproc.TM_CCOEFF;
        int machMethod=Imgproc.TM_CCOEFF_NORMED;
        //Template matching method
        Imgproc.matchTemplate(scr, temp, outputImage, machMethod);

        MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        Point matchLoc = mmr.maxLoc;
        int x = (int) matchLoc.x;
        int y = (int) matchLoc.y;
        //robot.mouseMove(x + temp.cols()/2, y + temp.rows()/2);
            if (y<900) {
                bot.moveMouse(x + temp.cols() / 2, y + temp.rows() / 2);
            }

    }

    public int isFighting() throws IOException {
        return compareImages(isFight);
    }

    public int isHealing() throws IOException {
        return compareImages(isHealing);
    }

    public void isEnemyBusy() throws IOException, InterruptedException {
        BufferedImage img = getScreen();
        File fl = new File("src/elements/screen.png");
        ImageIO.write(img, "png", fl);
        //Mat scr = null;
        Mat scr = Imgcodecs.imread("src/elements/screen.png");
        Mat temp = Imgcodecs.imread("src/elements/error2Img.png");

        Mat outputImage=new Mat();
        // int machMethod=Imgproc.TM_CCOEFF;
        int machMethod=Imgproc.TM_CCOEFF_NORMED;
        //Template matching method
        Imgproc.matchTemplate(scr, temp, outputImage, machMethod);

        MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        Point matchLoc = mmr.maxLoc;
        int x = (int) matchLoc.x;
        int y = (int) matchLoc.y;
        //robot.mouseMove(x + temp.cols()/2, y + temp.rows()/2);
        if (y<800) {
            robot.mouseMove(x + temp.cols() / 2, y + temp.rows() / 2);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }

    }

    public boolean heal() throws IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {
            if (getHelth() < healingRange) {
                System.out.println("heal");
                switch (healCounter) {
                    case 0:
                        bot.moveMouse(heal1.getX(), heal1.getY());
                        break;
                    case 1:
                        bot.moveMouse(heal2.getX(), heal2.getY());
                        break;
                    case 2:
                        bot.moveMouse(heal4.getX(), heal3.getY());
                        break;
                    case 3:
                        bot.moveMouse(heal5.getX(), heal4.getY());
                        break;
                }
                if (healCounter < 4) healCounter++;
                int counter = 0;
                while (getHelth() < healingRange) {
                    if (counter == 10) break;
                    Thread.sleep(1000);
                    counter++;
                }
            }
        return true;
    }

    public void figt() throws IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {
        punchCounter = 0;
        healCounter = 0;
        isEnemyBusy();
        while (isFighting() > 0){
             //  heal();
            while(isMyTurnToPunch() > 20){
                punchIt();
                Thread.sleep(400);
            }
        }

            Thread.sleep(1500);
            isEndFight();
            Thread.sleep(500);
            flag = 0;
            findIt();
    }

    public void figt2() throws IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {
        punchCounter = 0;
        healCounter = 0;
        while (isFighting() > 0){
           // heal();

            while(isMyTurnToPunch() > 20){
                System.out.println("punch");
                punchIt();
                isEnemyBusy();
                Thread.sleep(300);
            }

        }
        Thread.sleep(2000);
        isEndFight();
        Thread.sleep(300);
      // bobot();
        myning();
    }

    public void punchIt() throws IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {
        if (getEnemyHelth() > 5){
            robot.mouseMove(heal3.getX(),heal3.getY());
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        }
        Thread.sleep(200);
      // robot.mouseMove(punch2.getX(), punch2.getY());
      // robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
      // robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

       switch (superPunch[punchCounter]) {
           case 1:
               bot.moveMouse(punch1.getX(), punch1.getY());
               punchCounter++;
               break;
           case 2:
               bot.moveMouse(punch2.getX(), punch2.getY());
               punchCounter++;
               break;
           case 3:
               bot.moveMouse(punch3.getX(), punch3.getY());
               punchCounter++;
               break;
       }
       if (punchCounter == superPunch.length) {
           punchCounter = 0;
       }
    }

    public void findIt() throws IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {
       if (flag > 0 && flag < 4) {

           int x = rnd.nextInt(300) + 900;
           int y = rnd.nextInt(300) + 400;
           robot.mouseMove(rnd.nextInt(300) + 900, rnd.nextInt(300) + 400);
           robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

           for (int r = 0; r < 280; r++) {
               robot.mouseMove(x, y + r);
               Thread.sleep(1);
           }
           Thread.sleep(200);
           robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            flag++;
           Thread.sleep(1000);
       }else if (flag ==4 ){
           isEndFight();
           int x = rnd.nextInt(300) + 900;
           int y = rnd.nextInt(300) + 400;
           robot.mouseMove(rnd.nextInt(300) + 900, rnd.nextInt(300) + 400);
           robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

           for (int r = 0; r < 900; r++) {
               robot.mouseMove(x, y - r+200);
               Thread.sleep(1);
           }
           Thread.sleep(200);
           robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
           flag = 0;
           Thread.sleep(2000);
       }
       // robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
       // Thread.sleep(1000);
       // robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        BufferedImage screen = getScreen();
       // BufferedImage img = target.getImg();

        System.out.println("ищу");

        int[] colors = {};
        int[] ork = {
                -5225935, -5423057, -5488850, -5225421, -4767440, -4701647, -4438218,
                -4570575, -4243152, -6209232, -6143439, -4169901, -5946831, -5816016,
                -6272711, -5486794, -6141895, -5684430, -5290957, -3841191, -5095377,
                -5618380, -5291728, -6534599, -7189702, -10070964, -6927044, -6732235,
                -6927301, -6732492, -6601420, -4747644, -9024448, -7713731, -5869722,
                -5014160, -5804448, -5740699, -5283232, -4230288, -4230032, -6464180,
                -4954011, -6662840, -4559511, -7253179, -7318716, -6726321, -4428181,
                -6794944, -6989489, -8036519, -5678262, -4823981, -6202807, -4233386,
                -3850193, -4433328, -4113108, -4302000, -3982036, -7051675, -3520200,
                -4238004, -4368562, -5480097, -4175817, -5413291, -4109511, -4175560,
                -4759473, -5221557, -4698042, -5611180, -4043203

        };

        int[] gung = {
                -13808083, -13939669, -14072535, -13742290, -14005462, -13940949, -13941973,
                -13874132, -13874388, -14007254, -13808339, -13808851, -13742546, -13676753,
                -13677521, -13678033, -13613008, -13612752, -13611984, -13612496, -15654128,
                -14206426, -13811156, -13744339, -13152461, -13151694, -14274013, -13811925,
                -13216719, -13150416, -13748183, -13746391, -13022415, -13084368, -13018575,
                -13085136, -12889805, -13021647, -13022159, -12952782, -12953038, -12758219,
                -14141407, -12758476, -14009056, -12955088, -12954064, -12891088, -13086668,
                -15062506, -12825293, -12692175, -13088472, -12956886, -13285852, -12891094,
                -13549019, -13549276, -13616346, -13084625, -13285843, -13612504, -13220306,
                -13219794, -13220050, -13351636, -13614808, -13282515, -13285587, -13216466,
                -13215954, -13216722, -13153745, -13150161, -13481941, -13150930, -13283793,
                -13348050, -13615318, -13549014, -13614548, -13481940, -13415123, -13417428,
                -13680609, -13811940, -13878754, -14207201, -14670309, -14535905, -14665954,
                -12167370, -12561359, -12496333, -12561610, -12362949, -12100034, -12166598,

        };
        colors = ork;
       //int[] colors = {-12633315, -9476031, -9015740, -10396113, -10461394, -10330062, -10461136,
       //        -9870535, -10133451,  -10199244, -10133707,  -10263758, -10199243, -10133964, -10396112,
       //        -10066636, -10263759, -10068428, -12041958, -10330576, -10068172, -9739207, -9935814,
       //        -10330575, -10133450, -10067915, -10330058, -10264521, -10265289, -10461899, -10395340, -10461387, -10264778,
       //        -10264522, -10395849, -10264779,  -10198478, -10132685, -10199502, -10330572, -9804996, -10462665,
       //        -10329547, -10395594, -10329542, -10264517, -10396872, -10396104, -10395848, -10396360, -10329544,
       //        -10264264, -10000836, -10066371, -10198472, -10198728, -10198471,  -10197958, -10001864, -10198725,
       //        -10068679,  -11053260, -11580373, -11185103, -11119054, -10659271, -11120336, -11251150, -11119312,
       //        -11054288, -10791629, -10724811, -10658508, -10592972, -10527180, -10527692, -10987980, -10592718, -10658767,
       //        -10659279, -11646942, -10658512, -11185608, -10856140, -10594000, -10790092, -10526928, -12237782,
       //        -10855883, -10594257,  -10592723, -10857425, -11384284, -10987985, -11382742, -11316694,
       //        -11186132, -11054291, -11054292, -11448274, -11250901, -11251413, -11711694};


int fl=0;

        for (int i = mainFrame.getX(); i < mainFrame.getWidth() + mainFrame.getX(); i+=2) {
            for (int j = mainFrame.getY(); j < mainFrame.getHeight() + mainFrame.getY(); j+=2) {
                for (int k = 0; k < colors.length; k++) {
                    if (screen.getRGB(i, j) == colors[k]) {
                        System.out.println(colors[k]);
                        bot.moveMouse(i , j);
                        //robot.mouseMove(i,j);
                        Thread.sleep(1000);
                        isEnemyBusy();
                        if (isFighting()>0) {
                            figt();
                        }else{
                            i+=10;
                        }
                        fl++;
                        screen = getScreen();
                    }
                }
            }
          
            fl++;
        }
        if (flag == 0)flag++;
    }

    public void myning() throws IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {
        BufferedImage img = getScreen();
        File fl = new File("src/elements/screen.png");
        ImageIO.write(img, "png", fl);
        //Mat scr = null;
        Mat scr = Imgcodecs.imread("src/elements/screen.png");
       //Mat temp = Imgcodecs.imread("src/elements/ogn.png");
       Mat temp = Imgcodecs.imread("src/elements/iris.png");
      //  Mat temp = Imgcodecs.imread("src/elements/tsc.png");
      //  Mat temp = Imgcodecs.imread("src/elements/mand.png");
     //   Mat temp = Imgcodecs.imread("src/elements/anem.png");

        Mat outputImage=new Mat();
        // int machMethod=Imgproc.TM_CCOEFF;
        int machMethod=Imgproc.TM_CCOEFF_NORMED;
        //Template matching method
        Imgproc.matchTemplate(scr, temp, outputImage, machMethod);

        MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        Point matchLoc = mmr.maxLoc;
        int x = (int) matchLoc.x;
        int y = (int) matchLoc.y;
        //robot.mouseMove(x + temp.cols()/2, y + temp.rows()/2);
        if (y<900 & x < 1647) {
            bot.moveMouse(x + temp.cols() / 2, y + temp.rows() / 2);
        }
        isEnemyBusy();
        while  (isMyning() > 50) {
            Thread.sleep(50);
        }
        Thread.sleep(1000);
        if (getEnemyHelth() != 0) {
            figt2();
        }
    }

    public void separateColors() throws IOException, ParseException, AWTException {
        BufferedImage img = target.getImg();
        BufferedImage main = mainFrame.getImg();
        HashSet<Integer> h = new HashSet<Integer>();
        for (int k = 0; k < img.getWidth(); k++) {
            for (int t = 0; t < img.getHeight(); t++) {
                int counter = 0;
                for (int i = 0; i < mainFrame.getWidth(); i++) {
                    for (int j = 0; j < mainFrame.getHeight(); j++) {
                        if (img.getRGB(k, t) == main.getRGB(i, j)) {
                            counter++;
                        }
                    }
                }
                if (counter == 0){
                      h.add(img.getRGB(k,t));
                }
            }
        }
        Iterator<Integer> i = h.iterator();
        while (i.hasNext())
            System.out.print(i.next() + ", ");
    }


    public void bobot() throws IOException, InterruptedException, ParseException, org.json.simple.parser.ParseException {

        System.out.println("serch");
        BufferedImage img = getScreen();
        File fl = new File("src/elements/screen.png");
        ImageIO.write(img, "png", fl);
        //Mat scr = null;
        for (int i=0; i<4;i++) {

            Mat scr = Imgcodecs.imread("src/elements/screen.png");
            Mat temp = Imgcodecs.imread("src/elements/target" + i + ".png");

            Mat outputImage = new Mat();
            // int machMethod=Imgproc.TM_CCOEFF;
            int machMethod = Imgproc.TM_CCOEFF_NORMED;
            //Template matching method
            Imgproc.matchTemplate(scr, temp, outputImage, machMethod);

            MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
            Point matchLoc = mmr.maxLoc;
            int x = (int) matchLoc.x;
            int y = (int) matchLoc.y;
            // robot.mouseMove(x + temp.cols()/2, y + temp.rows()/2);
            if (y<800) {
                if (y>288 && x>513)
                bot.moveMouse(x + temp.cols() / 2, y + temp.rows() / 2);
            }
            isEnemyBusy();
            if (isFighting()>0){
                break;
            }
        }

        if (isFighting()>0){
            figt2();
        }else{
            bobot();
        }

    }


    public void timer() throws InterruptedException {
        robot.mouseMove(1566,378);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(5000);
        robot.mouseMove(1240,920);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(15000);
        robot.mouseMove(746,679);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(1000 *  60 * 60 + rnd.nextInt(1000 * 60 * 10)+1000*60*5);
    }

    public static void main(String[] args) throws AWTException, IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {
        Fight ft = new Fight();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //ft.rColor(-1);
      // Thread.sleep(4000);
      // ft.separateColors();
       // Thread.sleep(3000);
      //  System.out.println(ft.isFighting());
        System.out.println("here");
        // ft.isEndFight();
        //ft.figt();

       while (true) {
           //Thread.sleep(3000);
       //   ft.bobot() ;
         //  ft.myning();
         //  ft.isEndFight();
       //    ft.timer();
            // Thread.sleep(3000);
           ft.findIt();
         //  Thread.sleep(3000);
          //  ft.myning();



            //  System.out.println( ft.getHelth());
       }
        }
    }
