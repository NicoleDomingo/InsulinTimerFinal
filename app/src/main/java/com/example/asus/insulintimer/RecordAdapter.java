package com.example.asus.insulintimer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Mharjorie Sandel on 03/04/2018.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {

    private Record r;
    private List<Record> recordList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView bs;

        public MyViewHolder(View itemView) {
            super(itemView);
            bs = (TextView) itemView.findViewById(R.id.tv_bs);



        }
    }
    public RecordAdapter(List<Record> recordList) {
        this.recordList = recordList;
    }

    @Override
    public RecordAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloodsugar_layout, parent, false);
        return new RecordAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(RecordAdapter.MyViewHolder holder, int position) {
        r = recordList.get(position);
        holder.bs.setText(r.getBs());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
