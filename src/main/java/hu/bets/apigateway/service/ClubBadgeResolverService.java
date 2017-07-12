package hu.bets.apigateway.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubBadgeResolverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClubBadgeResolverService.class);
    private static final String CREST_ADDRESS_PREFIX = "https://raw.githubusercontent.com/toptipr/multimedia/master/crests/";
    private static final String FILE_FORMAT = ".PNG";
    private static final String UNKNOWN_CREST_NAME = "99999";

    private final Map<String, String> crestsMap = new HashMap<>();

    @PostConstruct
    private void init() {
        try {
            Path path = Paths.get(this.getClass().getClassLoader().getResource("crests.properties").toURI());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), "UTF8"));
            String line = "";

            while ((line = reader.readLine()) != null) {
                String[] split = line.split("=");
                crestsMap.putIfAbsent(split[0], split[1]);
            }
        } catch (Exception e) {
            LOGGER.info("Exception while trying to load club crest map. Crests may be unavailable. ", e);
        }

    }

    public Map<String, String> resolveBadges(List<String> clubNames) {
        return null;
    }

    private String resolveBadge(String clubName) {
        String result = "";
        double min = Integer.MAX_VALUE;
        for (String club : crestsMap.keySet()) {
            int distance = StringUtils.getLevenshteinDistance(club, clubName);
            if (distance < min) {
                result = club;
                min = distance;
            }
        }

        if (StringUtils.getJaroWinklerDistance(clubName, result) > .60) {
            return CREST_ADDRESS_PREFIX + crestsMap.get(result) + FILE_FORMAT;
        }

        return CREST_ADDRESS_PREFIX + UNKNOWN_CREST_NAME + FILE_FORMAT;
    }
}
