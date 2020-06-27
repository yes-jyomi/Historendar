package kr.hs.emirim.sagittta.historendar.Search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kr.hs.emirim.sagittta.historendar.DB.DbOpenHelper;
import kr.hs.emirim.sagittta.historendar.DB.User;
import kr.hs.emirim.sagittta.historendar.DatabaseHelperHeart;
import kr.hs.emirim.sagittta.historendar.R;

import static android.content.ContentValues.TAG;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>  {
    DbOpenHelper mdb;
    private OnItemClickListener mListener = null ;
    SQLiteDatabase db;
    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }


    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }


    private List<User> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected Button heartBtn;
        protected TextView date_this;
        protected TextView event_this;
        protected ImageView Image;

        public final View mView;

        public CustomViewHolder(View view) {
            super(view);
            mView = itemView;
            this.date_this= (TextView) view.findViewById(R.id.date);
            this.event_this= (TextView) view.findViewById(R.id.event);
            this.Image=(ImageView) view.findViewById(R.id.EventImage);
            this.heartBtn=(Button) view.findViewById(R.id.like);
        }
    }


    public CustomAdapter(List<User> list) {
        this.mList = list;

    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, final int position) {

        Context context = viewholder.mView.getContext();
        DatabaseHelperHeart databaseHelper = new DatabaseHelperHeart(context, "DB", null, 1);
        db = databaseHelper.getWritableDatabase();

        viewholder.date_this.setGravity(Gravity.LEFT);
        viewholder.event_this.setGravity(Gravity.LEFT);

        viewholder.date_this.setText(mList.get(position).getDAY01());
        viewholder.event_this.setText(mList.get(position).getEVENT());
            if(mList.get(position).getPHOTO()!=null){
                Bitmap bm = BitmapFactory.decodeByteArray(mList.get(position).getPHOTO(),0,mList.get(position).getPHOTO().length) ;
                viewholder.Image.setImageBitmap(bm);
            }else {
                viewholder.Image.setImageBitmap(null);
            }


        Cursor cursor = null;

        try {
            cursor=db.rawQuery("SELECT * FROM LIKEY WHERE heart%2!=0 and num="+mList.get(position).getNUM()+";",null);
            Log.d("sowon","query");
            if (cursor != null) {
                while (cursor.moveToNext()){
                    Log.d("sowon",cursor.getInt(cursor.getColumnIndex("NUM"))+"");
                    viewholder.heartBtn.setBackgroundResource(R.drawable.ic_heart);
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        viewholder.heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Log.d("sowon ",mList.get(position).getNUM()+"");
                DatabaseHelperHeart databaseHelper = new DatabaseHelperHeart(context, "DB", null, 1);
                db = databaseHelper.getWritableDatabase();
                dbHeartUpdate("LIKEY",mList.get(position).getNUM());

//                DBSearch();

                Cursor cursor = null;

                try {
                    cursor=db.rawQuery("SELECT * FROM LIKEY WHERE heart%2!=0 and num="+mList.get(position).getNUM()+";",null);
                    Log.d("sowon","query 온클릭 들어옴");
                    viewholder.heartBtn.setBackgroundResource(R.drawable.ic_emptyheart);
                    if (cursor != null) {
                        while (cursor.moveToNext()){
                            Log.d("sowon 찬칸","찬칸");
                            Log.d("sowon",cursor.getInt(cursor.getColumnIndex("NUM"))+"");
                            viewholder.heartBtn.setBackgroundResource(R.drawable.ic_heart);
                        }
                    }

                } finally {
                    if (cursor != null) {
                        db.close();
                        databaseHelper.close();
                    }
                }

            }

    });


    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    void dbHeartUpdate(String tableName,int num) {
        try{
            db.execSQL("update LIKEY set HEART=HEART+1 WHERE NUM="+num+";");
        }
        catch (Exception e){
            Log.d("sowon customAdapter",e.toString());
        }
    }

//    void DBSearch() {
//        Cursor cursor = null;
//
//        try {
//            cursor=db.rawQuery("SELECT * FROM LIKEY WHERE heart%2!=0;",null);
//            Log.d("sowon","query");
//            if (cursor != null) {
//
//                while (cursor.moveToNext()) {
//                    int num = cursor.getInt(cursor.getColumnIndex("NUM"));
//                    int heart = cursor.getInt(cursor.getColumnIndex("HEART"));
//                    Log.d("sowon", "num: " + num + ", heart: " + heart);
//
//                }
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//
//    }//DBSearch

}