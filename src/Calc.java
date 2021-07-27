

import org.w3c.dom.ls.LSOutput;

import javax.xml.transform.Source;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Calc {

    public Scanner scan;

    public Calc(){
        scan = new Scanner(System.in);
    }

    public int parseInt(char s){
        return Character.getNumericValue(s);
    }

    public void calculate() {
        String st = scan.nextLine();
        st = st.trim();
        char[] row = st.toCharArray();
        int first = -1;
        int second = -1;

        System.out.println(row.length);

        switch (row.length) {
            case 3:
                System.out.println("here");
                if (isThisNededValue(row[0])) {
                    first = parseInt(row[0]);
                }else {
                    error("first");
                    break;
                }
                if (isThisNededValue(row[2])) {
                    second= parseInt(row[2]);
                }else {
                    error("second");
                    break;
                }
                calc(row[1], first, second);

            case 4:
                if (row[0] == '1' && row[1] == '0'){
                    first = 10;
                    if (isThisNededValue(row[3])){
                        second = parseInt(row[3]);
                    }else{
                        error("second");
                        break;
                    }
                    calc(row[2],first,second);
                }else{
                    if (isThisNededValue(row[0])) {
                        first = parseInt(row[0]);
                    }else {
                        error("first");
                        break;
                    }
                    if (row[2] == '1' && row[3] == '0') {
                        second = 10;
                    }else{
                        error("second");
                        break;
                    }
                    calc(row[1],first,second);
                }

            case 5:
                if (row[0] == '1' && row[1] == '0'){
                    first = 10;
                }else{
                    error("first");
                    break;
                }
                if (row[3] == '1' && row[4] == '0'){
                    second = 10;
                }else{
                    error("second");
                    break;
                }
                calc(row[2],first,second);
        }

    }

    public void error(String s){
        if (s == "first"){
            System.out.println("Некоректное значение первого операнда. Значение должно быть от 0 до 10");
        }else{
            System.out.println("Некоректное значение второго операнда. Значение должно быть от 0 до 10");
        }
    }

   public boolean isThisNededValue(char s){
       if (parseInt(s) >= 0 && parseInt(s) <=10){
           return true;
       }else{
           return false;
       }
   }

    public void calc(char s, int first, int second){

        switch (s){
            case '+':
                System.out.println(first + second);
                break;
            case '-':
                System.out.println(first - second);
                break;
            case '*':
                System.out.println(first * second);
                break;
            case '/':
                if (second != 0) {
                    System.out.println(first / second);
                    break;
                }else{
                    System.out.println("Деление на 0 невозможно");
                    break;
                }
            default:
                System.out.println("Некоректно указан операнд");
                break;
        }
    }

    public static void main(String[] args) {
        Calc cl = new Calc();
        cl.calculate();
    }
}
