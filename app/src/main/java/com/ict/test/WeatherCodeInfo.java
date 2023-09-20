package com.ict.test;

import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class WeatherCodeInfo {
    public String[] PTY = {"없음","비","비/눈", "눈", "소나기"};
    public String[] SKY = {"맑음","구름조금","구름많음", "흐림"};

    public String getreturn(String category, String fcstValue){
        String name = getCategoryName(category);
        if (name == "1시간 기온"){
            return "기온   : "+fcstValue +"℃";
        } else if (name == "하늘상태") {
            return name+" : "+SKY[Integer.parseInt(fcstValue)-1];
        } else if (name == "강수형태") {
            return name+" : "+PTY[Integer.parseInt(fcstValue)];
        } else if (name == "풍속") {
            return "풍속   : "+fcstValue+"m/s";
        } else if (name == "파고") {
            return "파고   : "+fcstValue+"M";
        }
        else return " ";
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
