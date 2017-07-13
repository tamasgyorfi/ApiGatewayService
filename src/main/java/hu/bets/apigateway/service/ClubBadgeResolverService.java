package hu.bets.apigateway.service;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubBadgeResolverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClubBadgeResolverService.class);
    private static final String UNKNOWN_CREST_NAME = "99999";

    private final Map<String, String> crestsMap = new HashMap<>();

    @PostConstruct
    protected void init() {

        try {
            URI uri = this.getClass().getClassLoader().getResource("crests.properties").toURI();
            createFileSystem(uri);
            Path path = Paths.get(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), "UTF8"));
            String line = "";

            while ((line = reader.readLine()) != null) {
                String[] split = line.split("=");
                crestsMap.putIfAbsent(split[0], split[1]);
            }
        } catch (Exception e) {
            LOGGER.info("Exception while trying to load club crest map. Crests may be unavailable. ", e);
        }

        LOGGER.info("Initialized the crests map with {} entries.", crestsMap.size());
    }

    private void createFileSystem(URI uri) throws IOException {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        FileSystem zipfs = FileSystems.newFileSystem(uri, env);
    }

    public Map<String, String> resolveBadges(List<String> clubNames) {
        Map<String, String> retVal = new HashMap<>();
        for (String club : clubNames) {
            retVal.put(club, resolveBadge(club));
        }

        return retVal;
    }

    private String resolveBadge(String clubName) {
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

        if (new JaroWinklerDistance().apply(clubName, result) > .60) {
            return crestsMap.get(result);
        }

        return UNKNOWN_CREST_NAME;
    }
}
