package com.ict.test;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private ArrayList<ReportItem> reportItems;
    TextView rdate, rcontent;
    ImageView rimage;


    // 어댑터 생성자
    public ReportAdapter(ArrayList<ReportItem> reportItems,TextView rcontent, TextView rdate, ImageView rimage) {
        this.reportItems = reportItems;
        this.rcontent = rcontent;
        this.rdate = rdate;
        this.rimage = rimage;
    }


    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reportTitleTextView;
        public TextView reportDate;
        public LinearLayout btn_report;

        public ViewHolder(View itemView) {
            super(itemView);
            reportTitleTextView = itemView.findViewById(R.id.reportcontent);
            reportDate = itemView.findViewById(R.id.reportdate);
            btn_report = itemView.findViewById(R.id.btn_Report);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReportItem item = reportItems.get(position);
        holder.reportTitleTextView.setText(item.getReportContent());
        holder.reportDate.setText(item.reportDate);
        holder.btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdate.setText(item.getReportDate());

                rcontent.setText(item.getReportContent());

                rimage.setImageURI(item.getImage()); //이 부분 수정해야함
            }
        });
    }
    public void setReportItems(ArrayList<ReportItem> reportItems){
        this.reportItems = reportItems;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (reportItems != null) {
            return reportItems.size();
        } else {
            return 0; // 또는 다른 기본값을 반환
        }
    }
}