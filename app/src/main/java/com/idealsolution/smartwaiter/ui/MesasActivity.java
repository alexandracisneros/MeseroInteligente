package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Spinner;
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.model.MesaPisoObject;
import com.idealsolution.smartwaiter.model.MesaPisoHelper;
import com.idealsolution.smartwaiter.model.SpinnerObject;
import com.idealsolution.smartwaiter.util.InsetDecoration;

import java.util.ArrayList;

public class MesasActivity extends Activity implements
        MesaItemAdapter.OnItemClickListener{
    private Spinner mPisosSpinner;
    private Spinner mAmbienteSpinner;

    private RecyclerView mRecylerView;
    private MesaItemAdapter mMesasAdapter;

    /* Layout Manager */
    private GridLayoutManager mVerticalGridManager;


    private ArrayList<SpinnerObject> mListaAmbientes;
    private ArrayList<SpinnerObject> mListaPisos;
    private ArrayList<MesaPisoObject> mListaObjectMesas;
    // The ScheduleHelper is responsible for feeding data in a format suitable to the Adapter.
    private MesaPisoHelper mDataHelper;

    public MesasActivity() {
        mDataHelper = new MesaPisoHelper(this);

    }

    public ArrayList<SpinnerObject> getListaPisos() {
        return mListaPisos;
    }

    public ArrayList<SpinnerObject> geListaAmbientes() {
        return mListaAmbientes;
    }

    public ArrayList<MesaPisoObject> getListaObjectMesas() {
        return mListaObjectMesas;
    }

    public Spinner getPisosSpinner() {
        return mPisosSpinner;
    }

    public Spinner getAmbienteSpinner() {
        return mAmbienteSpinner;
    }

    public RecyclerView getRecylerView() {
        return mRecylerView;
    }

    public MesaItemAdapter getMesasAdapter() {
        return mMesasAdapter;
    }

    public GridLayoutManager getVerticalGridManager() {
        return mVerticalGridManager;
    }

    public void setMesasAdapter(MesaItemAdapter mMesasAdapter) {
        this.mMesasAdapter = mMesasAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);
        mPisosSpinner = (Spinner) findViewById(R.id.pisos_spinner);
        mAmbienteSpinner = (Spinner) findViewById(R.id.ambientes_spinner);

        mRecylerView= (RecyclerView) findViewById(R.id.mesas_recycler_view);
        mRecylerView.setHasFixedSize(true);
        mVerticalGridManager=new GridLayoutManager(this,
                2,/* Number of grid columns*/
                LinearLayoutManager.VERTICAL,
                /* Orient grid vertically */
                false);
        //Apply margins decoration to all collections
        mRecylerView.addItemDecoration(new InsetDecoration(this));
        mRecylerView.setLayoutManager(mVerticalGridManager);

        loadPisosSpinner();
    }

    public void loadAmbienteSpinner(final int nroPiso) {
        mListaAmbientes = new ArrayList<SpinnerObject>();
        mDataHelper.getAmbientesAsync(nroPiso);
    }

    private void loadPisosSpinner() {
        mListaPisos = new ArrayList<SpinnerObject>();
        mDataHelper.getPisosAsync();
//        mDataHelper.getArticuloPorFamiliaAsync(0);
    }
    public void loadMesasObject(int nroPiso, int codAmbiente) {
        mListaObjectMesas=new ArrayList<MesaPisoObject>();
        mDataHelper.getMesasAsync(nroPiso, codAmbiente);
    }
    /** OnItemClickListener Method */
    @Override
    public void onItemClick(MesaItemAdapter.ItemHolder item, int position) {
        Toast.makeText(this, item.getNro(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,TakeOrderActivity.class));
    }
}
