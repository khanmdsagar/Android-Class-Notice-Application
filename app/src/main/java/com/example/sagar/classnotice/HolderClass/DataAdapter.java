package com.example.sagar.classnotice.HolderClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sagar.classnotice.EditActivity;
import com.example.sagar.classnotice.ModelClass.NoticeModel;
import com.example.sagar.classnotice.R;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Context context;
    List<NoticeModel> notice;

    public DataAdapter(Context context, List<NoticeModel> notice) {
        this.context = context;
        this.notice = notice;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.notice_layout,viewGroup,false);
        return new DataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final NoticeModel noticeModel = notice.get(i);

        viewHolder.time.setText(noticeModel.getTime());
        viewHolder.date.setText(noticeModel.getDate());
        viewHolder.text.setText(noticeModel.getText());

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = noticeModel.getId();
                String count = String.valueOf(noticeModel.getCount());
                String date,time,txt;

                date = viewHolder.date.getText().toString();
                time = viewHolder.time.getText().toString();
                txt = viewHolder.text.getText().toString();

                Intent intent = new Intent(context,EditActivity.class);
                intent.putExtra("Text",txt);
                intent.putExtra("Date",date);
                intent.putExtra("Time",time);
                intent.putExtra("Id",id);
                intent.putExtra("Count",count);

                context.startActivity(intent);
                //((Activity)context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return notice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView time,date,text;
        Button edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            text = itemView.findViewById(R.id.text);
            edit = itemView.findViewById(R.id.edit);

        }
    }

}
