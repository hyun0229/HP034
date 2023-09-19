package com.ict.test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.POST;

public class Report_Api {
    private String server_url;


    public Report_Api(String server_url){
        this.server_url = server_url;
    }
    public interface ApiService {
        @POST("save_report.php") // 이 부분에 PHP 스크립트의 경로를 입력하세요
        Call<Void> insertData(@Field("uid") int uid,
                              @Field("dialog") String dialog,
                              @Field("report_image") String report_image);
    }

    public void report_image(int uid, String dialog, String report_image){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server_url) // PHP 서버의 기본 URL을 입력합니다.
                .addConverterFactory(GsonConverterFactory.create())
                .build();

// ApiService 인터페이스의 인스턴스 생성
        ApiService apiService = retrofit.create(ApiService.class);

// Retrofit2를 사용하여 데이터 전송
        Call<Void> call = apiService.insertData(uid, dialog, report_image);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("성공");
                } else {
                    // 요청이 실패했습니다. 오류 처리를 수행하세요.
                    System.out.println("실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 네트워크 오류 또는 서버 오류 발생 시 호출됩니다.
                System.out.println("네트워크오류");
            }
        });
    }



}
