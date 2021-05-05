package ca.on.conestogac.kmarshalldlacelle.matchgame;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MatchGameApplication extends Application {

    private final String DB_NAME = "MatchDB";
    private final int DB_VERSION = 1;

    private SQLiteOpenHelper helper;

    @Override
    public void onCreate() {

        helper = new SQLiteOpenHelper(this,DB_NAME,null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS tbl_match(" +
                        "matchId INTEGER PRIMARY KEY," +
                        "name TEXT," +
                        "score INTEGER," +
                        "date TEXT);");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    //todo: implement if updating application
            }
        };
        super.onCreate();
    }

    public void AddMatch(MatchObject mo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO tbl_match (name, score, date) VALUES(?,?,?);",
                new String[] {mo.getName(), Integer.toString(mo.getScore()), mo.getDate()});
    }

    public MatchObject GetMatch(int id) {
        MatchObject mo;
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM tbl_match WHERE id = ?"
                , new String[] {Integer.toString(id)});
        c.moveToLast();

        mo = new MatchObject(c.getInt(0), c.getInt(1),
                c.getString(2), c.getString(3));
        return mo;
    }

    public ArrayList<MatchObject> GetAllMatches() {
        ArrayList<MatchObject> mos = new ArrayList<MatchObject>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tbl_match ORDER BY score DESC", null ); c.moveToFirst();

        c.moveToFirst();
        while(!c.isAfterLast()) {
            mos.add(new MatchObject(c.getInt(0), c.getInt(2),
                    c.getString(3), c.getString(1)));
            c.moveToNext();
        }
        return mos;
    }

    public void DeleteAllMatches() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM tbl_match;");
    }
}
