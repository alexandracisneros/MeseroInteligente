package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import static com.idealsolution.smartwaiter.contract.SmartWaiterContract.MesaPiso;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.model.SpinnerObject;

import java.util.ArrayList;

public class CombosActivity extends Activity {
    private Spinner mPisosSpinner;
    private Spinner mAmbienteSpinner;
    private Spinner mMesasSpinner;

    private ArrayList<SpinnerObject> mListaAmbientes;
    private ArrayList<SpinnerObject> mListaPisos;
    private ArrayList<SpinnerObject> mListaMesas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combos);
        mPisosSpinner= (Spinner) findViewById(R.id.pisos_spinner);
        mAmbienteSpinner= (Spinner) findViewById(R.id.ambientes_spinner);
        mMesasSpinner= (Spinner) findViewById(R.id.mesas_spinner);

        loadPisosSpinner();
        OnItemSelectedListener pisosSelectedListener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {
                    int nroPiso=mListaPisos.get(position).getCodigo();
                    //Toast.makeText(CombosActivity.this,"Piso: " + nroPiso,Toast.LENGTH_SHORT).show();
                    loadAmbienteSpinner(nroPiso);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        //http://stackoverflow.com/questions/2562248/how-to-keep-onitemselected-from-firing-off-on-a-newly-instantiated-spinner
        //mPisosSpinner.setSelection(0,false);
        // Setting ItemClick Handler for Spinner Widget
        mPisosSpinner.setOnItemSelectedListener(pisosSelectedListener);



    }
    private void loadAmbienteSpinner(int nroPiso){
        final int piso=nroPiso;
        mListaAmbientes=new ArrayList<SpinnerObject>();
        Cursor cursor = getApplicationContext().getContentResolver().query(
                MesaPiso.CONTENT_AMBIENTE_URI,
                AmbientesQuery.PROJECTION,
                MesaPiso.AMBIENTES_POR_PISO_SELECTION,
                new String[]{String.valueOf(piso)},
                null);
        while (cursor.moveToNext()) {
            SpinnerObject item = new SpinnerObject();
            item.setCodigo(cursor.getInt(AmbientesQuery.AMBIENTE_COD_AMBIENTE));
            item.setDescripcion(cursor.getString(AmbientesQuery.AMBIENTE_DESC_AMBIENTE));
            mListaAmbientes.add(item);
        }
        cursor.close();
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, mListaAmbientes);
        mAmbienteSpinner.setAdapter(adapter);


        OnItemSelectedListener ambientesSelectedListener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {
                int codAmbiente=mListaAmbientes.get(position).getCodigo();
                loadMesasSpinner(piso,codAmbiente);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };
        // Setting ItemClick Handler for Spinner Widget
        mAmbienteSpinner.setOnItemSelectedListener(ambientesSelectedListener);
    }
    private void loadPisosSpinner(){
        mListaPisos=new ArrayList<SpinnerObject>();
        Cursor cursor=getApplicationContext().getContentResolver().query(
                MesaPiso.CONTENT_PISO_URI,
                PisosQuery.PROJECTION,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            SpinnerObject item = new SpinnerObject();
            item.setCodigo(cursor.getInt(PisosQuery.PISO_NRO_PISO));
            item.setDescripcion("PISO " + cursor.getInt(PisosQuery.PISO_NRO_PISO));
            mListaPisos.add(item);
        }
        cursor.close();
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, mListaPisos);
        mPisosSpinner.setAdapter(adapter);
    }
    private void loadMesasSpinner(int nroPiso, int codAmbiente){
        mListaMesas=new ArrayList<SpinnerObject>();
        Cursor cursor=getApplicationContext().getContentResolver().query(
                MesaPiso.CONTENT_URI,
                MesasQuery.PROJECTION,
                MesaPiso.MESAS_POR_PISO_AMBIENTE_SELECTION,
                new String[]{String.valueOf(nroPiso), String.valueOf(codAmbiente)},
                null);
        while (cursor.moveToNext()) {
            SpinnerObject item = new SpinnerObject();
            item.setCodigo(cursor.getInt(MesasQuery.MESA_ID));
            item.setDescripcion("MESA " + cursor.getInt(MesasQuery.MESA_NRO_MESA));
            mListaMesas.add(item);
        }
        cursor.close();
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, mListaMesas);
        mMesasSpinner.setAdapter(adapter);
    }
    private interface AmbientesQuery {
        String[] PROJECTION = {
                SmartWaiterContract.QUERY_DISTINCT + MesaPiso.COD_AMBIENTE
               ,MesaPiso.DESC_AMBIENTE
        };

        int AMBIENTE_COD_AMBIENTE = 0;
        int AMBIENTE_DESC_AMBIENTE = 1;
    }
    private interface PisosQuery{
        String[] PROJECTION={
            SmartWaiterContract.QUERY_DISTINCT + MesaPiso.NRO_PISO
        };
        int PISO_NRO_PISO=0;
    }
    private interface MesasQuery{
        String[] PROJECTION={
                MesaPiso.ID,
                MesaPiso.NRO_PISO,
                MesaPiso.COD_AMBIENTE,
                MesaPiso.NRO_MESA,
                MesaPiso.NRO_ASIENTOS,
                MesaPiso.COD_ESTADO_MESA,
                MesaPiso.DESC_ESTADO_MESA,
                MesaPiso.COD_RESERVA
        };
        int MESA_ID=0;
        int MESA_NRO_PISO=1;
        int MESA_COD_AMBIENTE=2;
        int MESA_NRO_MESA=3;
        int MESA_NRO_ASIENTOS=4;
        int MESA_COD_ESTADO=5;
        int MESA_DESC_ESTADO=6;
        int MESA_COD_RESERVA=7;
    }


}
