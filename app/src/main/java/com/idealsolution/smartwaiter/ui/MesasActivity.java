package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.model.MesaPisoObject;
import com.idealsolution.smartwaiter.model.MesaPisoHelper;
import com.idealsolution.smartwaiter.model.PedidoCabObject;
import com.idealsolution.smartwaiter.model.SpinnerObject;
import com.idealsolution.smartwaiter.util.InsetDecoration;

import java.util.ArrayList;
import java.util.Objects;

public class MesasActivity extends BaseActivity implements
        MesaItemAdapter.OnItemClickListener{


    public static final String EXTRA_MESA_SELECCIONADA="pedido_mesa_seleccionada";
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
        super.onCreate(savedInstanceState,R.layout.opcion_selec_mesa);
        overridePendingTransition(0,0);
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

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_SELEC_MESA;
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
        MesaPisoObject selectedTable=new MesaPisoObject();
        SpinnerObject objPiso=getListaPisos().get(mPisosSpinner.getSelectedItemPosition());
        SpinnerObject objAmbiente=geListaAmbientes().get(mAmbienteSpinner.getSelectedItemPosition());

        Log.d(MesasActivity.class.toString(), "Ambiente.Codigo=" + objAmbiente.getCodigo() + " Ambiente.Descripcion=" + objAmbiente.getDescripcion());
        Log.d(MesasActivity.class.toString(), "Nro Mesa=" + item.getNroMesa() + " Mesas = " + item.getNro().toString());

        selectedTable.setNro_piso(objPiso.getCodigo());
        selectedTable.setCod_ambiente(objAmbiente.getCodigo());
        selectedTable.setNro_mesa(item.getNroMesa());
        Gson gson = new Gson();
        String mesaJSON = gson.toJson(selectedTable);

        Intent intent = new Intent(this, TakeOrderActivity.class);
        intent.putExtra(EXTRA_MESA_SELECCIONADA,mesaJSON);
        //TakeOrderActivity.SavedPreferenceOrder(pedido, this);
        Log.d(MesasActivity.class.toString(), mesaJSON);
        startActivity(intent);
    }

}
