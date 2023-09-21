package com.ict.test;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MenuFragment extends Fragment {
    public LinearLayout btn_setting, btn_chatbot, btn_usermode, btn_bellsetting;
    public TextView txt_weatherData, txt_weatherData2;
    public String date, time;

    WeatherCodeInfo wci;
    WeatherData weatherData ;

    public List<String> weatherInfo = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_weatherData = view.findViewById(R.id.txt_Weather);
        txt_weatherData2 = view.findViewById(R.id.txt_Weather2);
        setCurrentDateTime();
        wci = new WeatherCodeInfo();
        btn_chatbot = view.findViewById(R.id.btn_ChatBot);
        btn_chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_ChatFragment();
                closeFragment();
            }
        });
        btn_usermode = view.findViewById(R.id.btn_UserMode);
        btn_usermode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_DrivingFragment();
                closeFragment();
            }
        });
        btn_setting = view.findViewById(R.id.btn_Setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_ProfileFragment();
                closeFragment();
            }
        });
        btn_bellsetting = view.findViewById(R.id.btn_BellSetting);
        btn_bellsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_AlarmSettingFragment();
                closeFragment();
            }
        });
        weatherData = new WeatherData(date, time);
        System.out.println(date + time);
        new FetchDataTask().execute();
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


            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(weatherData.fetchData())));

                NodeList items = doc.getElementsByTagName("item");
                StringBuilder displayText = new StringBuilder();

                for (int i = 0; i < items.getLength(); i++) {
                    Node item = items.item(i);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) item;
                        String category = element.getElementsByTagName("category").item(0).getTextContent();
                        String fcstValue = element.getElementsByTagName("fcstValue").item(0).getTextContent();
                        System.out.println("!!"+fcstValue);
                        String info = wci.getreturn(category, fcstValue);

                        if (!info.equals(" ")) {
                            displayText.append(info + "\n");
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

            System.out.println("zzz" + result);

            if (!weatherInfo.isEmpty() && weatherInfo.get(0) != null) {
                String a = String.format("%-22s%n%-20s%n%s", weatherInfo.get(0), weatherInfo.get(1), weatherInfo.get(4));
                String b = String.format("%s%n%s", weatherInfo.get(2), weatherInfo.get(3));
                txt_weatherData.setText(a);
                txt_weatherData2.setText(b);
            }
        }
    }

    public void setCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm", Locale.getDefault());

        date = dateFormat.format(calendar.getTime());
        time = timeFormat.format(calendar.getTime());

        int t = Integer.parseInt(time) / 100 * 100;
        System.out.println("sadasdas" + t);
        int d = Integer.parseInt(date);
        if (t < 200) {
            d -= 1;
        }
        date = String.valueOf(d);
        if (t < 1000) {
            time = "0" + String.valueOf(t);
        } else {
            time = String.valueOf(t);
        }
        time = timeChange(time);
    }

    public String timeChange(String time) {
        switch (time) {
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
}
