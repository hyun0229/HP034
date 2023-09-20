package com.ict.test;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MenuFragment extends Fragment {
    public LinearLayout btn_setting,btn_chatbot, btn_usermode,btn_bellsetting;
    public TextView txt_weatherData,txt_weatherData2;
    public String date, time;

    WeatherCodeInfo wci;

    public List<String> weatherInfo = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        txt_weatherData = v.findViewById(R.id.txt_Weather);
        txt_weatherData2 = v.findViewById(R.id.txt_Weather2);
        setCurrentDateTime();
        wci = new WeatherCodeInfo();
        btn_chatbot = v.findViewById(R.id.btn_ChatBot);
        btn_chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_ChatFragment();
                closeFragment();

            }
        });
        btn_usermode = v.findViewById(R.id.btn_UserMode);
        btn_usermode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_DrivingFragment();
                closeFragment();

            }
        });
        btn_setting = v.findViewById(R.id.btn_Setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_ProfileFragment();
                closeFragment();
            }
        });
        btn_bellsetting = v.findViewById(R.id.btn_BellSetting);
        btn_bellsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_AlarmSettingFragment();
                closeFragment();
            }
        });
        new FetchDataTask().execute();


        return v;
    }
    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
    private class FetchDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // ApiExplorer 클래스를 사용하여 데이터 가져오기

            WeatherData weatherData = new WeatherData(date,time);
            System.out.println(date+time);

            try {
                // XML 파싱 및 표시
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(weatherData.fetchData())));

                // XML 요소 추출
                NodeList items = doc.getElementsByTagName("item");
                StringBuilder displayText = new StringBuilder();

                for (int i = 0; i < items.getLength(); i++) {
                    Node item = items.item(i);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) item;
                        String category = element.getElementsByTagName("category").item(0).getTextContent();
                        String fcstValue = element.getElementsByTagName("fcstValue").item(0).getTextContent();

                        // 원하는 정보만 추출하여 표시

                        String info = wci.getreturn(category,fcstValue);
                        if (info!=" "){
                            displayText.append(info+"\n");
                            weatherInfo.add(info);
                        }
                    }
                }

                return displayText.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error parsing XML: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // 네트워크 호출 결과를 UI로 업데이트

            System.out.println(result);
            String a = String.format("%-22s%n%-20s%n%s", weatherInfo.get(0), weatherInfo.get(1), weatherInfo.get(4));
            String b = String.format("%s%n%s",weatherInfo.get(2),weatherInfo.get(3));
            txt_weatherData.setText(a);
            txt_weatherData2.setText(b);


        }
    }

    public String timeChange(String time)
    {
        // 현재 시간에 따라 데이터 시간 설정(3시간 마다 업데이트) //
        switch(time) {

            case "0200":
            case "0300":
            case "0400":
                time = "0200";
                break;
            case "0500":
            case "0600":
            case "0700":
                time = "0500";
                break;
            case "0800":
            case "0900":
            case "1000":
                time = "0800";
                break;
            case "1100":
            case "1200":
            case "1300":
                time = "1100";
                break;
            case "1400":
            case "1500":
            case "1600":
                time = "1400";
                break;
            case "1700":
            case "1800":
            case "1900":
                time = "1700";
                break;
            case "2000":
            case "2100":
            case "2200":
                time = "2000";
                break;
            case "2300":
            case "0000":
            case "0100":
                time = "2300";

        }
        return time;
    }
    public void setCurrentDateTime() {
        // 현재 시간을 가져옵니다.
        Calendar calendar = Calendar.getInstance();


        // 날짜 형식 지정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm", Locale.getDefault());

        // 현재 날짜와 시간을 문자열로 변환하여 반환
        date = dateFormat.format(calendar.getTime());
        time = timeFormat.format(calendar.getTime());

        int t = Integer.parseInt(time)/100*100;
        System.out.println("sadasdas"+t);
        int d = Integer.parseInt(date);
        if (t<200){
            d-=1;
        }
        date =String.valueOf(d);
        if (t<1000){
            time= "0"+String.valueOf(t);
        }
        else {
            time = String.valueOf(t);
        }
        time= timeChange(time);

    }



}