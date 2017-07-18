package hu.bets.apigateway.model;

public class Bet {

    private String competitionId;
    private String matchId;
    private String homeTeamId;
    private String awayTeamId;
    private int homeTeamGoals;
    private int awayTeamGoals;

    public Bet(String competitionId, String matchId, String homeTeamId, String awayTeamId, int homeTeamGoals, int awayTeamGoals) {
        this.competitionId = competitionId;
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeTeamGoals = homeTeamGoals;
        this.awayTeamGoals = awayTeamGoals;
    }
}
