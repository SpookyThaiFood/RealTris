package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class FileHandler {

    public FileHandler() {
    }

    public boolean save(String text, String filename) {
        try {
            FileWriter stream = new FileWriter(filename);
            PrintWriter file = new PrintWriter(stream);

            file.print(text);
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean append(String line, String filename) {
        try {
            FileWriter stream = new FileWriter(filename, true);
            PrintWriter file = new PrintWriter(stream);

            file.println(line);
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String[] openArray(String filename) {
        try {
            FileReader stream = new FileReader(filename);
            BufferedReader file = new BufferedReader(stream);
            String line = file.readLine();
            LinkedList<String> list = new LinkedList();
            while (line != null) {
                list.add(line);
                line = file.readLine();
            }
            String[] array = new String[0];
            array = (String[]) list.toArray(array);
            file.close();
            return array;
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
