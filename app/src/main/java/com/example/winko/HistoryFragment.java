package com.example.winko;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.winko.DB.DB;
import com.example.winko.DB.models.WineModel;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    Context ctx;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        ctx = container.getContext();
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DB db = new DB(ctx);

        ListAdapter adapter = new ListAdapter(ctx, new ArrayList<WineModel>());
        ListView listView = (ListView) view.findViewById(R.id.wine_list);
        listView.setAdapter(adapter);

        ArrayList<WineModel> wines = (ArrayList) db.showAll();
        adapter.addAll(wines);
    }
}