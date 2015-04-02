package com.idealsolution.smartwaiter.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.model.CategoriaObject;
import com.idealsolution.smartwaiter.model.CategoriaPlatoHelper;
import com.idealsolution.smartwaiter.model.MesaPisoHelper;
import com.idealsolution.smartwaiter.model.MesaPisoObject;

import java.util.ArrayList;

public class CategoryActivity extends ActionBarActivity {
    private ArrayList<CategoriaObject> mListaCategorias;
    // The ScheduleHelper is responsible for feeding data in a format suitable to the Adapter.
    private CategoriaPlatoHelper mDataHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }
    public void loadMesasObject() {
        mListaCategorias=new ArrayList<CategoriaObject>();
        mDataHelper.getCategoriasAsync();
    }
}
