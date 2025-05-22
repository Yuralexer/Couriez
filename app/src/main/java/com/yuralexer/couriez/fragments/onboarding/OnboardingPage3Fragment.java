package com.yuralexer.couriez.fragments.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yuralexer.couriez.R;

public class OnboardingPage3Fragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_TEXT = "text";
    private static final String ARG_IMAGE_RES_ID = "imageResId";

    public static OnboardingPage3Fragment newInstance(String title, String text, @DrawableRes int imageResId) {
        OnboardingPage3Fragment fragment = new OnboardingPage3Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_page, container, false);

        TextView titleTextView = view.findViewById(R.id.tvOnboardingTitle);
        TextView textTextView = view.findViewById(R.id.tvOnboardingText);
        ImageView imageView = view.findViewById(R.id.ivOnboardingImage);

        if (getArguments() != null) {
            titleTextView.setText(getArguments().getString(ARG_TITLE));
            textTextView.setText(getArguments().getString(ARG_TEXT));
            imageView.setImageResource(getArguments().getInt(ARG_IMAGE_RES_ID));
        }
        return view;
    }
}