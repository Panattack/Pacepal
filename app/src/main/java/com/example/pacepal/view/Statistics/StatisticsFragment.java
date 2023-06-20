package com.example.pacepal.view.Statistics;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pacepal.R;

public class StatisticsFragment extends Fragment implements StatisticsView {
    StatisticsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new StatisticsPresenter(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            presenter.receiveStatistics();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void alertBox(String title, String message) {
        new AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null).create().show();
    }
}