package com.ict.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;


public class AlarmSettingsFragment extends Fragment {
    public ImageButton btn_back;
    private SeekBar chat_s, chat_v, drive_s, drive_v;

    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm_settings, container, false);
        btn_back = v.findViewById(R.id.btn_back_setting);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity =(MainActivity) getActivity();
                mainActivity.commit_MenuFragment();
                closeFragment();
            }
        });
        sharedPreferences = requireActivity().getSharedPreferences("user_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        chat_s = v.findViewById(R.id.chat_Sound);
        chat_v = v.findViewById(R.id.chat_Vib);
        drive_s = v.findViewById(R.id.drive_Sound);
        drive_v = v.findViewById(R.id.drive_Vib);

        chat_s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("chat_sound",seekBar.getProgress());
                editor.apply();
            }
        });
        chat_v.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("chat_Vib",seekBar.getProgress());
                editor.apply();
            }
        });

        drive_s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("drive_sound",seekBar.getProgress());
                editor.apply();
            }
        });
        drive_v.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("drive_Vib",seekBar.getProgress());
                editor.apply();
            }
        });


        return v;
    }
    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
}