package hu.bets;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ck on 12/07/2017.
 */
public class Main {

    public static void main(String[] a) throws IOException {
        Path path = Paths.get("C:\\Users\\ck\\IdeaProjects\\apigateway\\src\\main\\java\\hu\\bets\\crests.properties");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), "UTF8"));
        String s = "";
        Map<String, String> map = new HashMap<>();

        while ((s = reader.readLine()) != null) {
            String[] split = s.split("=");
            map.putIfAbsent(split[0], split[1]);
        }

        System.out.println(map);
        String saved = "Barcelona";
        String res = "";
        double min = 99999;
        for(String st : map.keySet()) {
            int distance = StringUtils.getLevenshteinDistance(st, saved);
            if (distance<min) {
                res = st;
                min = distance;
            }
        }

        System.out.println(map.get(res));
//            String[] split = s.split("=");
//
//            System.out.println(split[1] + "=" + split[0]);
//        }


    }
}
