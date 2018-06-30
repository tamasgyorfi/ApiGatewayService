package hu.bets.apigateway.service.schedules;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubBadgeResolverService implements BadgeResolverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClubBadgeResolverService.class);
    private static final String UNKNOWN_CREST_NAME = "99999";
    private static final double DISTANCE_THRESHOLD = .60;

    private final Map<String, String> crestsMap = new HashMap<>();

    @PostConstruct
    public void init() {

        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream("crests.properties");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF8"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] split = line.split("=");
                crestsMap.putIfAbsent(split[0], split[1]);
            }
        } catch (Exception e) {
            LOGGER.info("Exception while trying to load club crest map. Crests may be unavailable. ", e);
        }

        LOGGER.info("Initialized the crests map with {} entries.", crestsMap.size());
    }

    @Override
    public Map<String, String> resolveBadges(List<String> clubNames) {
        Map<String, String> retVal = new HashMap<>();
        for (String club : clubNames) {
            retVal.put(club, resolveBadge(club));
        }

        return retVal;
    }

    public String resolveBadge(String clubName) {
        LOGGER.info("Resolving crest for: {}", clubName);
        String result = "";
        double min = Integer.MAX_VALUE;
        for (String club : crestsMap.keySet()) {
            int distance = LevenshteinDistance.getDefaultInstance().apply(club, clubName);
            if (distance < min) {
                result = club;
                min = distance;
            }
        }

        Double res = new JaroWinklerDistance().apply(clubName, result);
        LOGGER.info("Best match is {}, L distance is {}, J distance is {}", result, min, res);

        if (res > DISTANCE_THRESHOLD) {
            return crestsMap.get(result);
        }

        return UNKNOWN_CREST_NAME;
    }
}
