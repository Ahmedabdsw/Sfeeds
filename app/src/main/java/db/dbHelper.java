package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Objects;


/**
 * Created by Ahmed on 3/6/2018.
 */

public class dbHelper extends SQLiteOpenHelper {
    private static final String dbName = "sfeeds.db";
    private static final String col1 = "id";
    private static final String col2 = "title";
    private static final String col3 = "link";
    private static final String col4 = "image";
    private static final String col5 = "horaire";

    private static final String table1 = "CREATE TABLE IF NOT EXISTS sites ("+col1+" integer ,"+col2+" TEXT ,"+col3+" TEXT NOT NULL UNIQUE ,"+col4+" TEXT ,"+col5+" INTEGER )";
    private static final String tabla2 = "CREATE TABLE IF NOT EXISTS visits (idv INTEGER , visit INTEGER )";
    private static final String view1 = " CREATE VIEW IF NOT EXISTS myvisits AS SELECT idv,COUNT(idv) as vi FROM visits GROUP BY(idv)";
    private static final String table3 = " CREATE VIRTUAL TABLE IF NOT EXISTS searchquery USING FTS4(id,title , link) ";
     private static final String table4 = " CREATE TABLE notifications (idn INTEGER , title TEXT ,vue TEXT,norder INTEGER PRIMARY KEY AUTOINCREMENT)";
    private static final String trigger = "CREATE TRIGGER IF NOT EXISTS sitesTR   \n" +
            "   AFTER INSERT  \n" +
            " ON sites FOR EACH ROW \n" +
            "  " +
            "  BEGIN  \n" +
            "  INSERT INTO searchquery VALUES (new.id,new.title,new.link); \n" +
            "  " +
            "   END; ";

    public dbHelper(Context context) {
        super(context, dbName, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table1);
        db.execSQL(tabla2);
        db.execSQL(view1);
        db.execSQL(table3);
        db.execSQL(table4);
        db.execSQL(trigger);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sites");
        onCreate(db);
    }

    public boolean insertData(int id, String title, String link, String image, String horaire){
       if (link != null )
       {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ct = new ContentValues();
        ct.put(col1,id);
        ct.put(col2,title);
        ct.put(col3,link);
        ct.put(col4, image);
        ct.put(col5,horaire);

           long result = db.insert("sites",null,ct);
        if(result == -1)
            return false;
        else

        return true;
    }
    return false;
    }

    public boolean insertData(int id){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues ct = new ContentValues();
            ct.put("idv",id);
            ct.put("visit",1);
            long result = db.insert("visits",null,ct);
            if(result == -1)
                return false;
            else
                return true;
        }

    public boolean insertData(int id,String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ct = new ContentValues();
        ct.put("idn",id);
        ct.put("title",title);
        ct.put("vue","no");
        long result = db.insert("notifications",null,ct);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updatenotification(int norder) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues ct = new ContentValues();
            ct.put("vue","yes");
            long result = db.update("notifications", ct, "norder = ? ", new String[]{norder + ""});
            if (result == -1)
                return false;
            else
                return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean updateData(int id, String title, String link, String image, String horaire) {
        // RssReader r = new RssReader("");
        if (!Objects.equals(title,"غير متاح ..................................................................................") ){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues ct = new ContentValues();
                    ct.put(col1, id);
                    ct.put(col2, title);
                    ct.put(col3, link);
                    ct.put(col4, image);
                    ct.put(col5,horaire);
                    long result = db.update("sites", ct, col1 + " = ? ", new String[]{id + ""});
                    if (result == -1)
                        return false;
                    else
                        return true;
        }
        return false;
    }

    public String getNotifiycations(){
        SQLiteDatabase db = this.getWritableDatabase();
        long h = System.currentTimeMillis();

        Cursor res = db.rawQuery(" select count(idn) from notifications where vue = 'no' ",null);
        String s = null;
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                s = res.getString(0) + "";
            }
        }
        res.close();
                return s;
    }

    public void resetNotifiycations(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select norder from notifications where vue = 'no' ",null);
        String s = null;
        int i = 0;
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                s = res.getString(0) + "";
                i =Integer.parseInt(s);
                updatenotification(i);
            }
        }
        }

    public String[][] getNotifiycations1(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select distinct idn,notifications.title,link,norder from notifications left join sites on idn =id and notifications.title = sites.title order by(norder) desc limit 100 ",null);
        String[][] s = new String[res.getCount()][4];
        int i =0;
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                s[i][0] = res.getString(0) + "";
                s[i][1] = res.getString(1) + "";
                s[i][2] = res.getString(2) + "";
                s[i][3] = res.getString(3) + "";

                i++;
            }
        }
        res.close();
        return s;
    }

    public void deleteAllNotifications(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(" notifications", null, null);
    }
    
    public String[][] getAllSite(){
        String[][] rssfeeds = new String[0][];
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT id,title,link,horaire FROM (SELECT id,title,link,horaire,image FROM sites GROUP BY(id) ORDER BY(horaire)  ) LEFT JOIN myvisits ON id=idv ORDER BY(vi) DESC",null);
         if (res.getCount() != 0) {
            rssfeeds = new String[res.getCount()][4];
            int i = 0;
            while (res.moveToNext()) {
                rssfeeds[i][0] = res.getString(0) + "";
                rssfeeds[i][1] = res.getString(1) + "";
                rssfeeds[i][2] = res.getString(2) + "";
                rssfeeds[i][3] = res.getString(3) + "";

                i++;
            }
        }
        res.close();
        return rssfeeds;
    }

    /*
    public Cursor getVisits(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM myvisits WHERE id = "+id+" ",null);
        return res;
    }

    public Cursor getRawData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT id,"+col2+",link,"+col4+" FROM sites WHERE id = "+id+" ORDER BY horaire DESC limit 1",null);
        return res;
    }
*/
   public String[][] SearchLink(String query){
        SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("SELECT id,title,link FROM searchquery WHERE link MATCH '"+query+"'" , null);
            int i = 0;
            String rep[][] = new String[res.getCount()][3];
            if (res.getCount() != 0) {
                while (res.moveToNext()) {
                    rep[i][0] = res.getString(0) + "";
                    rep[i][1] = res.getString(1) + "";
                    rep[i][2] = res.getString(2) + "";
                    i++;
                }
            }
       res.close();
        return rep;
    }

    public String[][] SearchTitle(String query) {
        String[] q = query.split(" ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        Log.v("long",q.length+"");

if(q.length == 1)
    res = db.rawQuery("SELECT id,title,link FROM searchquery WHERE searchquery MATCH \'" + query+ "\'", null);
  else if (q.length == 2)
        res = db.rawQuery("SELECT id,title,link FROM searchquery WHERE searchquery MATCH \'" + query + "\'" +" UNION SELECT id,title,link FROM searchquery WHERE title MATCH \'" +q[0]+"'"+" UNION SELECT id,title,link FROM searchquery WHERE title MATCH \'" +q[1]+"'", null);
  else if (q.length == 3)
    res = db.rawQuery("SELECT id,title,link FROM searchquery WHERE searchquery MATCH \'" + query + "\'" +" UNION SELECT id,title,link FROM searchquery WHERE title MATCH \'" +q[0]+"'"+" UNION SELECT id,title,link FROM searchquery WHERE title MATCH \'" +q[1]+"'"+" UNION SELECT id,title,link FROM searchquery WHERE title MATCH \'" +q[2]+"'", null);
else if (q.length >=4)
    res = db.rawQuery("SELECT id,title,link FROM searchquery WHERE searchquery MATCH \'" + query + "\'" +" UNION SELECT id,title,link FROM searchquery WHERE title MATCH \'" +q[0]+"'"+" UNION SELECT id,title,link FROM searchquery WHERE title MATCH \'" +q[1]+"'"+" UNION SELECT id,title,link FROM searchquery WHERE title MATCH \'" +q[2]+"'"+" UNION SELECT id,title,link FROM searchquery WHERE title MATCH \'" +q[q.length-1]+"'", null);

        int i = 0;
        String rep[][] = new String[res.getCount()][3];
        if (res.getCount() != 0) {
            while (res.moveToNext()){
            rep[i][0] = res.getString(0) + "";
            rep[i][1] = res.getString(1) + "";
            rep[i][2] = res.getString(2) + "";
            i++;
        }
    }
        res.close();
        return rep;
    }



}
