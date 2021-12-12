package yuhyeon.ch02.item09;

import org.assertj.core.api.ThrowableAssert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TryFinallyExample {

    public static void main(String[] args) throws IOException {
        firstLineOfFileTryResources("firstLine", "default");
    }

    public static String firstLineOfFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }

    public static String firstLineOfFileTryResources(String path, String defaultVal) throws IOException {
        path = null;
        try(BufferedReader br =
                    new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }

    }
}

//catch (Exception e) {
//        e.printStackTrace();
//        return defaultVal;
//        }
