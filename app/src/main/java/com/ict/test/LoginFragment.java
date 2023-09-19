package com.ict.test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;


public class LoginFragment extends  Fragment {
    private View loginBtn;

//
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        loginBtn = v.findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(requireContext())) {
                    System.out.println("login available in onClick Y");
                    UserApiClient.getInstance().loginWithKakaoTalk(requireContext(), callback);
                }
                // 카카오톡이 없다면 카카오 계정 로그인으로 넘어가서 로그인하게 됨
                else {
                    System.out.println("login available in onClick N");
                    UserApiClient.getInstance().loginWithKakaoAccount(requireContext(), callback);
                }
            }

        });

        updateKakaoLoginUi();


        return v;
    }
    private void updateKakaoLoginUi(){ //로그인 여부 확인
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null){
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.commit_MenuFragment();
                    closeFragment();
                }
                else   {

                }
                return null;
            }
        });
    }
    Function2<OAuthToken, Throwable, Unit> callback =
            new Function2<OAuthToken, Throwable, Unit>() {
                @Override
                public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                    if (oAuthToken != null) {
                        System.out.println("token Y");
                    }
                    if (throwable != null) {
                        System.out.println("message s");
                        throwable.printStackTrace();
                        throwable.getMessage();
                        System.out.println("throwable Y");
                    }
                    updateKakaoLoginUi();
                    return null;
                }
            };



    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

}