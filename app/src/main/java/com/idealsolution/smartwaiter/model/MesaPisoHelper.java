package com.idealsolution.smartwaiter.model;


import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import com.idealsolution.smartwaiter.contract.SmartWaiterContract.MesaPiso;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Articulo;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.ui.MesasActivity;
import com.idealsolution.smartwaiter.ui.MesaItemAdapter;


import java.util.ArrayList;
import java.util.List;

import static com.idealsolution.smartwaiter.util.LogUtils.LOGD;
import static com.idealsolution.smartwaiter.util.LogUtils.makeLogTag;


public class MesaPisoHelper {
    private static final String TAG = makeLogTag(MesaPisoHelper.class);
    private MesasActivity mContext;

    public MesaPisoHelper(MesasActivity context) {
        this.mContext = context;
    }

    public void getPisosAsync() {
        new AsyncTask<Void, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Void... params) {
                return mContext.getApplicationContext().getContentResolver().query(
                        MesaPiso.CONTENT_PISO_URI,
                        PisosQuery.PROJECTION,
                        null,
                        null,
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                while (cursor.moveToNext()) {
                    SpinnerObject item = new SpinnerObject();
                    item.setCodigo(cursor.getInt(PisosQuery.PISO_NRO_PISO));
                    item.setDescripcion("PISO " + cursor.getInt(PisosQuery.PISO_NRO_PISO));
                    mContext.getListaPisos().add(item);
                }
                cursor.close();
                ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, mContext.getListaPisos());
                mContext.getPisosSpinner().setAdapter(adapter);

                OnItemSelectedListener pisosSelectedListener = new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> spinner, View container,
                                               int position, long id) {
                        int nroPiso = mContext.getListaPisos().get(position).getCodigo();
                        //Toast.makeText(CombosActivity.this,"Piso: " + nroPiso,Toast.LENGTH_SHORT).show();
                        mContext.loadAmbienteSpinner(nroPiso);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                };
                //http://stackoverflow.com/questions/2562248/how-to-keep-onitemselected-from-firing-off-on-a-newly-instantiated-spinner
                //mPisosSpinner.setSelection(0,false);
                // Setting ItemClick Handler for Spinner Widget
                mContext.getPisosSpinner().setOnItemSelectedListener(pisosSelectedListener);
            }
        }.execute();
    }

    public void getAmbientesAsync(final int nroPiso) {
        new AsyncTask<Void, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Void... params) {
                return mContext.getApplicationContext().getContentResolver().query(
                        MesaPiso.CONTENT_AMBIENTE_URI,
                        AmbientesQuery.PROJECTION,
                        MesaPiso.AMBIENTES_POR_PISO_SELECTION,
                        new String[]{String.valueOf(nroPiso)},
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                while (cursor.moveToNext()) {
                    SpinnerObject item = new SpinnerObject();
                    item.setCodigo(cursor.getInt(AmbientesQuery.AMBIENTE_COD_AMBIENTE));
                    item.setDescripcion(cursor.getString(AmbientesQuery.AMBIENTE_DESC_AMBIENTE));
                    mContext.geListaAmbientes().add(item);
                }
                cursor.close();
                ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, mContext.geListaAmbientes());
                mContext.getAmbienteSpinner().setAdapter(adapter);

                OnItemSelectedListener ambientesSelectedListener = new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> spinner, View container,
                                               int position, long id) {
                        int codAmbiente = mContext.geListaAmbientes().get(position).getCodigo();
                        //mContext.loadMesasSpinner(nroPiso, codAmbiente);
                        mContext.loadMesasObject(nroPiso, codAmbiente);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                };
                // Setting ItemClick Handler for Spinner Widget
                mContext.getAmbienteSpinner().setOnItemSelectedListener(ambientesSelectedListener);
            }
        }.execute();
    }
    public void getMesasAsync(final int nroPiso, final int codAmbiente) {

        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                return mContext.getApplicationContext().getContentResolver().query(
                        MesaPiso.CONTENT_URI,
                        MesasQuery.PROJECTION,
                        MesaPiso.MESAS_POR_PISO_AMBIENTE_SELECTION,
                        new String[]{String.valueOf(nroPiso), String.valueOf(codAmbiente)},
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                while (cursor.moveToNext()) {
                    MesaPisoObject item = new MesaPisoObject();
                    item.setId(cursor.getInt(MesasQuery.MESA_ID));
                    item.setNro_piso(cursor.getInt(MesasQuery.MESA_NRO_PISO));
                    item.setCod_ambiente(cursor.getInt(MesasQuery.MESA_COD_AMBIENTE));
                    item.setNro_mesa(cursor.getInt(MesasQuery.MESA_NRO_MESA));
                    item.setNro_asientos(cursor.getInt(MesasQuery.MESA_NRO_ASIENTOS));
                    item.setCod_estado(cursor.getString(MesasQuery.MESA_COD_ESTADO));
                    item.setDesc_estado(cursor.getString(MesasQuery.MESA_DESC_ESTADO));
                    item.setCod_reserva(cursor.getInt(MesasQuery.MESA_COD_RESERVA));
                    mContext.getListaObjectMesas().add(item);
                }
                cursor.close();
                mContext.setMesasAdapter(new MesaItemAdapter(mContext,mContext.getListaObjectMesas()));
                mContext.getMesasAdapter().setOnItemClickListener(mContext);
                mContext.getRecylerView().setAdapter(mContext.getMesasAdapter());

            }
        }.execute();
    }

    private interface PisosQuery {
        String[] PROJECTION = {
                MesaPiso.NRO_PISO
        };
        int PISO_NRO_PISO = 0;
    }

    private interface AmbientesQuery {
        String[] PROJECTION = {
                MesaPiso.COD_AMBIENTE,
                MesaPiso.DESC_AMBIENTE
        };

        int AMBIENTE_COD_AMBIENTE = 0;
        int AMBIENTE_DESC_AMBIENTE = 1;
    }

    private interface MesasQuery {
        String[] PROJECTION = {
                MesaPiso.ID,
                MesaPiso.NRO_PISO,
                MesaPiso.COD_AMBIENTE,
                MesaPiso.NRO_MESA,
                MesaPiso.NRO_ASIENTOS,
                MesaPiso.COD_ESTADO_MESA,
                MesaPiso.DESC_ESTADO_MESA,
                MesaPiso.COD_RESERVA
        };
        int MESA_ID = 0;
        int MESA_NRO_PISO = 1;
        int MESA_COD_AMBIENTE = 2;
        int MESA_NRO_MESA = 3;
        int MESA_NRO_ASIENTOS = 4;
        int MESA_COD_ESTADO = 5;
        int MESA_DESC_ESTADO = 6;
        int MESA_COD_RESERVA = 7;
    }
}
