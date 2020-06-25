package kr.hs.emirim.sagittta.historendar.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import kr.hs.emirim.sagittta.historendar.DB.DataAdapter;
import kr.hs.emirim.sagittta.historendar.DB.User;
import kr.hs.emirim.sagittta.historendar.DatabaseHelperHeart;
import kr.hs.emirim.sagittta.historendar.R;

import static android.content.ContentValues.TAG;

public class SearchMainActivity extends AppCompatActivity {

    public List<User> userList;
    public String search;
    Intent intent;
    private CustomAdapter mAdapter;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        Button SearchBtn=(Button)findViewById(R.id.GoSearchBtn);
        Button BackBtn=(Button)findViewById(R.id.backBtn);
        final EditText SearchEditText=(EditText)findViewById(R.id.searchText);





        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SearchText=SearchEditText.getText().toString();
                Log.d("sowon",SearchEditText.getText().toString());

                initLoadDB(SearchText);
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }//oncreate

    private void initLoadDB(String SEARCH_TEXT) {

        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        userList = mDbHelper.getSearchData(SEARCH_TEXT);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CustomAdapter(userList);
        mRecyclerView.setAdapter(mAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mDbHelper.close();
    }

    void dbHeartUpdate(String tableName,int num, int heart) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("HEART", heart);

        String nameArr[] = {num+""};

        // 리턴값: 업데이트한 수
        int n = db.update(tableName, contentValues, "num = ?", nameArr);

        Log.d(TAG, "n: " + n);
    }




    // SELECT * FROM People WHERE age < "age" ORDER BY NAME



}
