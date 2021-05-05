package ca.on.conestogac.kmarshalldlacelle.matchgame;

public class MatchObject {

    public MatchObject(int _score, String _date, String _name) {
        setScore(_score);
        setDate(_date);
        setName(_name);
    }

    public MatchObject(int _id, int _score, String _date, String _name) {
        setScore(_score);
        setDate(_date);
        setId(_id);
        setName(_name);
    }

    private int Id;
    private String Name;
    private int Score;
    private String Date;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
