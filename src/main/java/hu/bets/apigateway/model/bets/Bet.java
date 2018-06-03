package hu.bets.apigateway.model.bets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Bet {

    private String competitionId;
    private String matchId;
    private String homeTeamId;
    private String awayTeamId;
    private int homeTeamGoals;
    private int awayTeamGoals;

    @JsonCreator
    public Bet(@JsonProperty("competitionId") String competitionId,
               @JsonProperty("matchId") String matchId,
               @JsonProperty("homeTeamId") String homeTeamId,
               @JsonProperty("awayTeamId") String awayTeamId,
               @JsonProperty("homeTeamGoals") int homeTeamGoals,
               @JsonProperty("awayTeamGoals") int awayTeamGoals) {
        this.competitionId = competitionId;
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeTeamGoals = homeTeamGoals;
        this.awayTeamGoals = awayTeamGoals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bet bet = (Bet) o;

        if (homeTeamGoals != bet.homeTeamGoals) return false;
        if (awayTeamGoals != bet.awayTeamGoals) return false;
        if (competitionId != null ? !competitionId.equals(bet.competitionId) : bet.competitionId != null) return false;
        if (matchId != null ? !matchId.equals(bet.matchId) : bet.matchId != null) return false;
        if (homeTeamId != null ? !homeTeamId.equals(bet.homeTeamId) : bet.homeTeamId != null) return false;
        return awayTeamId != null ? awayTeamId.equals(bet.awayTeamId) : bet.awayTeamId == null;
    }

    @Override
    public int hashCode() {
        int result = competitionId != null ? competitionId.hashCode() : 0;
        result = 31 * result + (matchId != null ? matchId.hashCode() : 0);
        result = 31 * result + (homeTeamId != null ? homeTeamId.hashCode() : 0);
        result = 31 * result + (awayTeamId != null ? awayTeamId.hashCode() : 0);
        result = 31 * result + homeTeamGoals;
        result = 31 * result + awayTeamGoals;
        return result;
    }
}
