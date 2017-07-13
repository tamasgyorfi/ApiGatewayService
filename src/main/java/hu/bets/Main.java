package hu.bets;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by tamas.gyorfi on 13/07/2017.
 */
public class Main {
    public static void main(String[] a) throws IOException {
        String s = "{\"error\":\"\",\"payload\":[{\"matchId\":\"match2\",\"homeTeamGoals\":1,\"awayTeamGoals\":0},{\"matchId\":\"match3\",\"homeTeamGoals\":1,\"awayTeamGoals\":0},{\"matchId\":\"match4\",\"homeTeamGoals\":1,\"awayTeamGoals\":0}]}";

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(s);

        List<String> matchIds = JsonPath.read(document, "$.payload");
        System.out.println(matchIds);
        System.out.println(matchIds.size());
    }
}
