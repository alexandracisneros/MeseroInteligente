package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Spinner;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.model.MesaPisoHelper;
import com.idealsolution.smartwaiter.model.SpinnerObject;

import java.util.ArrayList;

public class CombosActivity extends Activity {
    private Spinner mPisosSpinner;
    private Spinner mAmbienteSpinner;
    private Spinner mMesasSpinner;

    private ArrayList<SpinnerObject> mListaAmbientes;
    private ArrayList<SpinnerObject> mListaPisos;
    private ArrayList<SpinnerObject> mListaMesas;
    // The ScheduleHelper is responsible for feeding data in a format suitable to the Adapter.
    private MesaPisoHelper mDataHelper;

    public CombosActivity() {
        mDataHelper = new MesaPisoHelper(this);

    }

    public ArrayList<SpinnerObject> getListaMesas() {
        return this.mListaMesas;
    }

    public ArrayList<SpinnerObject> getListaPisos() {
        return mListaPisos;
    }

    public ArrayList<SpinnerObject> geListaAmbientes() {
        return mListaAmbientes;
    }

    public Spinner getMesasSpinner() {
        return mMesasSpinner;
    }

    public Spinner getPisosSpinner() {
        return mPisosSpinner;
    }

    public Spinner getAmbienteSpinner() {
        return mAmbienteSpinner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combos);
        mPisosSpinner = (Spinner) findViewById(R.id.pisos_spinner);
        mAmbienteSpinner = (Spinner) findViewById(R.id.ambientes_spinner);
        mMesasSpinner = (Spinner) findViewById(R.id.mesas_spinner);

        loadPisosSpinner();
    }

    public void loadAmbienteSpinner(final int nroPiso) {
        mListaAmbientes = new ArrayList<SpinnerObject>();
        mDataHelper.getAmbientesAsync(nroPiso);
    }

    private void loadPisosSpinner() {
        mListaPisos = new ArrayList<SpinnerObject>();
        mDataHelper.getPisosAsync();
    }

    public void loadMesasSpinner(int nroPiso, int codAmbiente) {
        mListaMesas = new ArrayList<SpinnerObject>();
        mDataHelper.getMesasDataAsync(nroPiso, codAmbiente);
    }

}
