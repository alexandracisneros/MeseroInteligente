package com.idealsolution.smartwaiter.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idealsolution.smartwaiter.R;

/**
 * Created by Usuario on 24/04/2015.
 */
public class OrderFragment extends Fragment {
    private String mArticuloSelected;
    private TextView mDescTextView;
    public void loadDescripcion(String desc){
        this.mArticuloSelected=desc;
        mDescTextView.setText(mArticuloSelected);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_order_details,parent,false);
        mDescTextView= (TextView) v.findViewById(R.id.hello_world_textview);
        return v;
    }

}
