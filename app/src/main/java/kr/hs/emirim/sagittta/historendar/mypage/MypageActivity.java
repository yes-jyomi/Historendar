package kr.hs.emirim.sagittta.historendar.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import kr.hs.emirim.sagittta.historendar.DB.DataAdapter;
import kr.hs.emirim.sagittta.historendar.DB.User;
import kr.hs.emirim.sagittta.historendar.DatabaseHelperHeart;
import kr.hs.emirim.sagittta.historendar.R;
import kr.hs.emirim.sagittta.historendar.Search.CustomAdapter;

public class MypageActivity extends AppCompatActivity {

    public List<User> userList;
    public ArrayList<String> searchList=new ArrayList<String>();
    SQLiteDatabase db;
    private CustomAdapterMypage mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Button backBtn =(Button)findViewById(R.id.backBtn);
        DatabaseHelperHeart databaseHelper = new DatabaseHelperHeart(this, "DB", null, 1);
        db = databaseHelper.getWritableDatabase();
        DBSearch();

        initLoadDB(searchList);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }//oncreate


    void DBSearch() {
        Cursor cursor = null;

        try {
            cursor=db.rawQuery("SELECT * FROM LIKEY WHERE heart%2!=0;",null);
            Log.d("sowon","query");
            if (cursor != null) {

                while (cursor.moveToNext()) {
                    int num = cursor.getInt(cursor.getColumnIndex("NUM"));
                    int heart = cursor.getInt(cursor.getColumnIndex("HEART"));

                    Log.d("sowon", "num: " + num + ", heart: " + heart);
                    searchList.add(num+"");
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


    }//DBSearch


    private void initLoadDB(List<String>heartList) {

        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        userList = mDbHelper.getHeartData(heartList);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewMy);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CustomAdapterMypage(userList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mDbHelper.close();
    }

}