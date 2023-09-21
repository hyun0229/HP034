package com.ict.test;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ReportFragment extends Fragment {

    public ArrayList<ReportItem> reportItems = new ArrayList<>();
    private ImageButton btn_back;
    public RecyclerView recyclerView;
    public ReportAdapter reportAdapter;

    public TextView rcontnet,rdate;
    public ImageView rimage;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        btn_back = view.findViewById(R.id.btn_back_report);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_MenuFragment();
                closeFragment();
            }
        });
        rcontnet = view.findViewById(R.id.rcontent);
        rdate = view.findViewById(R.id.rdate);
        rimage = view.findViewById(R.id.riamge);
        recyclerView = view.findViewById(R.id.recycler_view_report);
        reportAdapter =new ReportAdapter(reportItems,rcontnet,rdate,rimage);
        recyclerView.setAdapter(reportAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        Uri byteArray = null;
        int resourceId = getResources().getIdentifier("testimg1", "drawable", "com.ict.test");
        Uri uri= null;
        if (resourceId != 0) {
            uri = Uri.parse("android.resource://" + "com.ict.test" + "/" + resourceId);
        }
        resourceId = getResources().getIdentifier("testimg2", "drawable", "com.ict.test");
        Uri uri2= null;
        if (resourceId != 0) {
            uri2 = Uri.parse("android.resource://" + "com.ict.test" + "/" + resourceId);
        }
        resourceId = getResources().getIdentifier("testimg3", "drawable", "com.ict.test");
        Uri uri3= null;
        if (resourceId != 0) {
            uri3 = Uri.parse("android.resource://" + "com.ict.test" + "/" + resourceId);
        }

        reportItems.add(new ReportItem("다리가 무너졌습니다","09월 21일",uri));
        reportItems.add(new ReportItem("도로에 싱크홀이 났어요","08월 04일",uri2));
        reportItems.add(new ReportItem("나무가 쓰러졌있어요","07월 24일",byteArray));
        reportItems.add(new ReportItem("가로등이 휘었었요","07월 14일",byteArray));
        reportItems.add(new ReportItem("표지판이 쓰러졌어요","06월 14일",uri3));
        reportItems.add(new ReportItem("벽에 금이갔어요","06월 04일",byteArray));
        reportItems.add(new ReportItem("책상이 망가졌습니다.","06월 02일",byteArray));


        reportAdapter.setReportItems(reportItems);

        return view;
    }

    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
}