package com.pktworld.taskthrough.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pktworld.taskthrough.R;
import com.pktworld.taskthrough.db.DatabaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabhat on 27/03/16.
 */
public class TaskAdapter extends BaseAdapter {

    private static final String TAG = TaskAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private List<DatabaseModel> categoryList = null;

    public TaskAdapter(Context context,
                               List<DatabaseModel> categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
        this.categoryList = new ArrayList<DatabaseModel>();
        this.categoryList.addAll(categoryList);
    }

    public class ViewHolder {
        TextView txtTitle,txtTime;
        LinearLayout llContaner;

    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public DatabaseModel getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_task, null);
            holder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            holder.txtTime = (TextView) view.findViewById(R.id.txtTime);
            holder.llContaner = (LinearLayout) view.findViewById(R.id.llContaner);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtTitle.setText(categoryList.get(position).getTitle());
        holder.txtTime.setText("Time : "+categoryList.get(position).getDateTime());
        holder.llContaner.setBackgroundColor(mContext.getResources().getColor(R.color.trans_blue));


        return view;
    }


}
