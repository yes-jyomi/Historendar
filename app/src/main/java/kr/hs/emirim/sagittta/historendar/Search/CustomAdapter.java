package kr.hs.emirim.sagittta.historendar.Search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kr.hs.emirim.sagittta.historendar.DB.User;
import kr.hs.emirim.sagittta.historendar.R;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>  {

    private List<User> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView date_this;
        protected TextView event_this;

        public CustomViewHolder(View view) {
            super(view);
            this.date_this= (TextView) view.findViewById(R.id.date);
            this.event_this= (TextView) view.findViewById(R.id.event);

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
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

//        viewholder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//        viewholder.event.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        viewholder.date_this.setGravity(Gravity.LEFT);
        viewholder.event_this.setGravity(Gravity.LEFT);

        viewholder.date_this.setText(mList.get(position).getDAY01());
        viewholder.event_this.setText(mList.get(position).getEVENT());

    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}