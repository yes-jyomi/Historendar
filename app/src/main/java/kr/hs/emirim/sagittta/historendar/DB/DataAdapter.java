package kr.hs.emirim.sagittta.historendar.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class DataAdapter
{
    protected static final String TAG = "DataAdapter";

    // TODO : TABLE 이름을 명시해야함
    protected static final String TABLE_NAME = "historender";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DataAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase 데이터불러오기실패");
            throw new Error("UnableToCreateDatabase 데이터불러오기실패");
        }
        return this;
    }

    public DataAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public List getTableData()
    {
        try
        {
            // Table 이름 -> antpool_bitcoin 불러오기
            String sql ="SELECT * FROM " + TABLE_NAME;

            // 모델 넣을 리스트 생성
            List userList = new ArrayList();

            // TODO : 모델 선언
            User user = null;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                // 칼럼의 마지막까지
                while( mCur.moveToNext() ) {

                    // TODO : 커스텀 모델 생성
                    user = new User();

                    // TODO : Record 기술
                    // id, name, account, privateKey, secretKey, Comment
                    user.setNUM(mCur.getInt(0));
                    user.setEVENT(mCur.getString(1));
                    user.setDAY01(mCur.getString(2));
                    user.setDAY02(mCur.getString(3));

                    // 리스트에 넣기
                    userList.add(user);
                }

            }
            return userList;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public List getSearchData(String SEARCHTEXT)
    {
        try
        {
            String sql ="SELECT * FROM " + TABLE_NAME+" WHERE EVENT LIKE "+"'%"+SEARCHTEXT+"%'";

            // 모델 넣을 리스트 생성
            List userList = new ArrayList();

            // TODO : 모델 선언
            User user = null;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                // 칼럼의 마지막까지
                while( mCur.moveToNext() ) {

                    // TODO : 커스텀 모델 생성
                    user = new User();

                    // TODO : Record 기술
                    // id, name, account, privateKey, secretKey, Comment
                    user.setNUM(mCur.getInt(0));
                    user.setEVENT(mCur.getString(1));
                    user.setDAY01(mCur.getString(2));
                    user.setDAY02(mCur.getString(3));

                    Log.d("sowon",user.DAY01);
                    Log.d("sowon",user.EVENT);
                    // 리스트에 넣기
                    userList.add(user);
                }

            }
            return userList;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }


    public List getNotiData(int month, int dayOfMonth)
    {
        try
        {
            SimpleDateFormat format1 = new SimpleDateFormat ( "MM");
            SimpleDateFormat format2 = new SimpleDateFormat ( "dd");

//            String format_time1 = format1.format (System.currentTimeMillis());
//            String format_time2 = format2.format (System.currentTimeMillis());
//            Log.v("현재 날짜:"+format_time1,"현재 날짜:"+format_time1);

            Log.v("현재 날짜:"+month,"현재 날짜:"+month);
            String strMonth;
            String strDay;
            if (month < 10)
                strMonth = "0" + month;
            else
                strMonth = String.valueOf(month);
            if (dayOfMonth < 10)
                strDay = "0" + dayOfMonth;
            else
                strDay = String.valueOf(dayOfMonth);

            String sql="SELECT * FROM "+ TABLE_NAME+" where DAY01 like '%-"+strMonth+"-"+strDay+"%'";
//            String sql="SELECT * FROM historender ";

            Log.d("jyomi",sql);
            //
//            String sql ="SELECT * FROM " + TABLE_NAME+" WHERE EVENT LIKE '%조선%'";

            // 모델 넣을 리스트 생성
            List userList = new ArrayList();

            // TODO : 모델 선언
            User user = null;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                // 칼럼의 마지막까지
                while( mCur.moveToNext() ) {

                    // TODO : 커스텀 모델 생성
                    user = new User();

                    // TODO : Record 기술
                    // id, name, account, privateKey, secretKey, Comment
                    user.setNUM(mCur.getInt(0));
                    user.setEVENT(mCur.getString(1));
                    user.setDAY01(mCur.getString(2));
                    user.setDAY02(mCur.getString(3));
                    Log.d("sowon",user.DAY01);
                    Log.d("sowon",user.EVENT);
                    // 리스트에 넣기
                    userList.add(user);
                }

            }
            return userList;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public List getNotiData()
    {
        try
        {
            SimpleDateFormat format1 = new SimpleDateFormat ( "MM");
            SimpleDateFormat format2 = new SimpleDateFormat ( "dd");

            String format_time1 = format1.format (System.currentTimeMillis());
            String format_time2 = format2.format (System.currentTimeMillis());
            Log.v("jyomi"+format_time1,"현재 날짜:"+format_time1);

            String sql="SELECT * FROM "+ TABLE_NAME+" where DAY01 like '%-"+format_time1+"-"+format_time2+"%'";
//            String sql="SELECT * FROM historender ";

            Log.d("sowon",sql);
            //
//            String sql ="SELECT * FROM " + TABLE_NAME+" WHERE EVENT LIKE '%조선%'";

            // 모델 넣을 리스트 생성
            List userList = new ArrayList();

            // TODO : 모델 선언
            User user = null;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                // 칼럼의 마지막까지
                while( mCur.moveToNext() ) {

                    // TODO : 커스텀 모델 생성
                    user = new User();

                    // TODO : Record 기술
                    // id, name, account, privateKey, secretKey, Comment
                    user.setNUM(mCur.getInt(0));
                    user.setEVENT(mCur.getString(1));
                    user.setDAY01(mCur.getString(2));
                    user.setDAY02(mCur.getString(3));
                    Log.d("sowon",user.DAY01);
                    Log.d("sowon",user.EVENT);
                    // 리스트에 넣기
                    userList.add(user);
                }

            }
            return userList;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

}
