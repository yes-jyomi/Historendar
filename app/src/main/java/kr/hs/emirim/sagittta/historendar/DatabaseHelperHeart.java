package kr.hs.emirim.sagittta.historendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static android.content.ContentValues.TAG;
public class DatabaseHelperHeart extends SQLiteOpenHelper {

    static final String TABLE_NAME = "LIKEY";

    public DatabaseHelperHeart(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d(TAG, "DataBaseHelper 생성자 호출");
    }

    //create table heart (num int, heart int);
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Table Create");
        String createQuery = "CREATE TABLE " + TABLE_NAME +
                "(NUM INTEGER, " +
                "HEART INTEGER);"+"";
        sqLiteDatabase.execSQL(createQuery);
//        dbInsert("LIKEY");
        for(int i=1;i<=8195;i++){
            sqLiteDatabase.execSQL("insert into LIKEY values("+i+",0);");
            Log.d(TAG, "id: " + i);
        }
        Log.d("sowon","끝");
    }
    //8195
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "Table onUpgrade");
        String createQuery = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(createQuery);
    }

}

