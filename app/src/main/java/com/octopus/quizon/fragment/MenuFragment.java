package com.octopus.quizon.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octopus.quizon.R;

public class MenuFragment extends Fragment {
    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    public interface MenuListener {
        void onPlay();
        void onSettings();
        void onProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Button mPlayButton = view.findViewById(R.id.play);
        final Button mSettingButton = view.findViewById(R.id.settings);
        final Button mProfileButton = view.findViewById(R.id.account);
        mPlayButton.setOnClickListener(v -> {
            ((MenuListener) getActivity()).onPlay();
        });
        mProfileButton.setOnClickListener(v -> {
            ((MenuListener) getActivity()).onProfile();
        });
        mSettingButton.setOnClickListener(v -> {
            ((MenuListener) getActivity()).onSettings();
        });
    }
}
