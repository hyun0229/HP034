package com.ict.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getHashKey();

        KakaoSdk.init(this, "4c54b5840220d45a6978fe74f3363dee");
        updateKakaoLoginUi();
        commit_MenuFragment();

    }

    public void updateKakaoLoginUi(){ //로그인 여부 확인
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null){

                }
                else   {
                    LoginFragment kaKaoLogin  = new LoginFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,kaKaoLogin,"kakao_fragment").commit();
                }
                return null;
            }
        });
    }
    public void commit_MenuFragment(){
        MenuFragment menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, menuFragment,"menuFragment").commit();
    }
    public void commit_ChatFragment(){
        ChatFragment chatFragment = new ChatFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, chatFragment,"chatFragment").commit();
    }
    public void commit_DrivingFragment(){
        DrivingFragment drivingFragment = new DrivingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, drivingFragment,"drivingFragment").commit();
    }
    public void commit_ProfileFragment(){
        ProfileFragment profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment,"profileFragment").commit();
    }
    public void commit_AlarmSettingFragment(){
        AlarmSettingsFragment alarmSettingsFragment =new AlarmSettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,alarmSettingsFragment,"alarmSettingsFragment").commit();
    }
     /*private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }*/

}