package com.tracking.constants;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adapter.smarttrack.LegTypeAdapter;
import com.models.SubLocationModel;
import com.tracking.smarttrack.R;

import java.util.ArrayList;
import java.util.List;

public class LegDialog extends Dialog {


    public interface LegTypeListener {
        public void LegTypeReady(int position);
    }

    List<SubLocationModel> legList;
    private LegTypeListener readyListener;
    ListView legTypeListView;


    public LegDialog(Context context, List<SubLocationModel> list, LegTypeListener readyListener) {
        super(context);
        this.legList = list;
        this.readyListener = readyListener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.popup_legtype);
       // setCancelable(false);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        legTypeListView = (ListView) findViewById(R.id.legTypeListView);

        LegTypeAdapter adapter = new LegTypeAdapter(getContext(), legList );
        legTypeListView.setAdapter(adapter);


        // Spinner click listener
        legTypeListView.setOnItemClickListener(new LegFieldListener());

      //  btnLoadingJob.setOnClickListener(new VehicleFieldListener());
    }


    private class LegFieldListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            readyListener.LegTypeReady(position);
        }
    }
}
