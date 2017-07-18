package hu.bets.apigateway.service;

import java.util.List;
import java.util.Map;

public interface BadgeResolverService {

    /**
     * Given a list of club names, returns a club name - image name mapping.
     *
     * @param clubNames nemes of the clubs we need badges for
     * @return a mapping between the club name and badge image.
     */
    Map<String, String> resolveBadges(List<String> clubNames);
}
