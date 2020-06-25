package kr.hs.emirim.sagittta.historendar.mypage;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.hs.emirim.sagittta.historendar.DB.DbOpenHelper;
import kr.hs.emirim.sagittta.historendar.DB.User;
import kr.hs.emirim.sagittta.historendar.R;

public class CustomAdapterMypage extends RecyclerView.Adapter<CustomAdapterMypage.CustomViewHolder>{


    DbOpenHelper mdb;
    private CustomAdapterMypage.OnItemClickListener mListener = null ;
    SQLiteDatabase db;
    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }


    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(CustomAdapterMypage.OnItemClickListener listener) {
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


    public CustomAdapterMypage(List<User> list) {
        this.mList = list;

    }



    @Override
    public CustomAdapterMypage.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomAdapterMypage.CustomViewHolder viewHolder = new CustomAdapterMypage.CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomAdapterMypage.CustomViewHolder viewholder, final int position) {


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


        viewholder.heartBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }



}
