package com.octopus.quizon.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.octopus.quizon.R;
import com.octopus.quizon.viewmodel.MenuViewModel;

public class ProfileFragment extends Fragment {

    private MenuViewModel mViewModel;
    public interface ToMain {
        void clickLastResult();
    }
    private Button mToLastResultButton;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View profileFragment = inflater.inflate(R.layout.profile_fragment, container, false);
        mToLastResultButton = profileFragment.findViewById(R.id.last_result);
        return profileFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        mToLastResultButton.setOnClickListener((v) -> ((ProfileFragment.ToMain) getActivity()).clickLastResult());
        // TODO: Use the ViewModel
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
