package com.example.abortion;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abortion.databinding.FragmentResource2Binding;

public class ResourceFragment2 extends Fragment {
    private TextView textView;
    private FragmentResource2Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResource2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        textView = view.findViewById(R.id.textView); // Initialize the textView using view.findViewById()
        return view;
    }

//    private void setDarkView() {
//        View rootView = requireActivity().getWindow().getDecorView().getRootView();
//        // Check if the current background color matches the desired color
//        Drawable background = rootView.getBackground();
//        int currentColor = 0;
//        if (background instanceof ColorDrawable) {
//            currentColor = ((ColorDrawable) background).getColor();
//        }
//        if (textView != null) {
//            if (currentColor == Color.parseColor("#e9e0c9")) {
//                rootView.setBackgroundColor(Color.parseColor("#414a4c"));
//                textView.setTextColor(Color.parseColor("#e9e0c9"));
//            } else {
//                rootView.setBackgroundColor(Color.parseColor("#e9e0c9"));
//                textView.setTextColor(Color.parseColor("#414a4c"));
//            }
//        }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
