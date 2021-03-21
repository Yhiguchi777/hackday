package com.example.hackday;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {
    static SecondFragment newInstance(String result){
        // Fragemnt01 インスタンス生成
        SecondFragment secondFragment = new SecondFragment();

        // Bundle にパラメータを設定
        Bundle args = new Bundle();
        args.putString("result", result);
        secondFragment.setArguments(args);

        return secondFragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String resultTxt = args.getString("result");

        TextView textView = view.findViewById(R.id.result);
        textView.setText(resultTxt);

    }

}
