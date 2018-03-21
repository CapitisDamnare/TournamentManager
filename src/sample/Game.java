package sample;

public class Game {

    public String gameNumber;
    public String time;
    public String homeTeam;
    public String guestTeam;
    public String homeTeamScore = "";
    public String guestTeamScore = "";
    public String versus = ":";

    public Game(String gameNumber, String time, String homeTeam, String guestTeam) {
        this.gameNumber = gameNumber;
        this.time = time;
        this.homeTeam = homeTeam;
        this.guestTeam = guestTeam;
    }

    public String getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(String gameNumber) {
        this.gameNumber = gameNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public String getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(String homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public String getGuestTeamScore() {
        return guestTeamScore;
    }

    public void setGuestTeamScore(String guestTeamScore) {
        this.guestTeamScore = guestTeamScore;
    }

    public String getVersus() {
        return versus;
    }

    public void setVersus(String versus) {
        this.versus = versus;
    }
}
