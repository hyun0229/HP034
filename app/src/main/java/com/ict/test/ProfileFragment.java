package com.ict.test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;


public class ProfileFragment extends Fragment {

    public TextView userName, userEmail, userPhone;
    public Button logoutBtn;
    public ImageView userProfile;
    public ImageButton btn_back;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        userName = v.findViewById(R.id.UserName);
        userEmail = v.findViewById(R.id.UserEmail);
        userPhone = v.findViewById(R.id.UserPhoneNumber);
        logoutBtn = v.findViewById(R.id.logoutButton);
        userProfile = v.findViewById(R.id.avatar);
        btn_back = v.findViewById(R.id.btn_back_profile);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity =(MainActivity) getActivity();
                mainActivity.commit_MenuFragment();
                closeFragment();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        closeFragment();
                        return null;
                    }
                });
            }
        });
        setUserProfile();



        return v;
    }

    private void setUserProfile(){ //로그인 여부 확인
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null){
                    userName.setText(user.getKakaoAccount().getProfile().getNickname());
                    userEmail.setText(user.getKakaoAccount().getEmail());
                    Glide.with(userProfile).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(userProfile);
                }
                else   {
                    closeFragment();
                }
                return null;
            }
        });
    }
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateKakaoLoginUi();
        }
    }
    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

}