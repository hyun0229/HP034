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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MenuFragment extends Fragment {
    public LinearLayout btn_setting,btn_chatbot, btn_usermode,btn_bellsetting;
    public TextView txt_weatherData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
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
        txt_weatherData = v.findViewById(R.id.txt_Weather);
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

            WeatherData weatherData = new WeatherData("20230918","1700");

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
                        String info = getCategoryName(category) + ": " + fcstValue + "\n";
                        displayText.append(info);
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
            txt_weatherData.setText(result);
        }
    }
    private String getCategoryName(String category) {
        switch (category) {
            case "POP":
                return "강수확률";
            case "PTY":
                return "강수형태";
            case "PCP":
                return "1시간 강수량";
            case "REH":
                return "습도";
            case "SNO":
                return "1시간 신적설";
            case "SKY":
                return "하늘상태";
            case "TMP":
                return "1시간 기온";
            case "TMN":
                return "일 최저기온";
            case "TMX":
                return "일 최고기온";
            case "UUU":
                return "풍속(동서성분)";
            case "VVV":
                return "풍속(남북성분)";
            case "WAV":
                return "파고";
            case "VEC":
                return "풍향";
            case "WSD":
                return "풍속";
            default:
                return "";
        }
    }



}