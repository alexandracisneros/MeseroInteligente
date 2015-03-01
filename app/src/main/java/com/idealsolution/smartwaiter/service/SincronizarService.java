package com.idealsolution.smartwaiter.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.*;
import com.idealsolution.smartwaiter.io.RestConnector;
import com.idealsolution.smartwaiter.io.RestUtil;
import com.idealsolution.smartwaiter.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 21/12/2014.
 */
public class SincronizarService extends IntentService {
    private static final String NAME = "SincronizarService";
    public static final String ACTION_SYNC_DATA="com.idealsolution.smartwaiter.SYNC_DATA";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Param NAME Used to name the worker thread, important only for debugging.
     */
    public SincronizarService() {
        super(NAME);
        // We don’t want intents redelivered
        // in case we’re shut down unexpectedly
        setIntentRedelivery(false);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get NetworkInfo object
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // if network is connected, download data
        boolean exito = false;
        String mensaje = "";
        try{
            if (networkInfo != null && networkInfo.isConnected()) {
                Object requestObject = null;
                String resultado;
                String url= RestUtil.URLServer
                        + "ObtenerDatosIniciales/?codCia=001&cadenaConexion=Initial%20Catalog=ABR";
                RestConnector getConnector=RestUtil.obtainGetConnection(url);
                requestObject=getConnector.doRequest(url);
                if(requestObject instanceof String){
                    // Only if the request was successful parse the returned
                    // value otherwise re-throw the exception
                    resultado = (String) requestObject;
                    JSONObject jsonObjectResponse = (new JSONObject(resultado));
                    exito=saveSyncDataToDB(jsonObjectResponse);
                    Log.d(SmartWaiterContract.TAG,"Sincronizacion Correcta");
                    exito=true;

                }else if(requestObject instanceof Exception){
                    throw new Exception((Exception)requestObject);
                }
            }
            else {
                throw new Exception("Imposible conectarse a Internet.");

            }
        }catch (Exception ex){
            mensaje=ex.getMessage();
            exito=false;
        }
        Intent broadcastIntent=new Intent();
        broadcastIntent.setAction(SincronizarService.ACTION_SYNC_DATA);
        broadcastIntent.putExtra("exito",exito);
        broadcastIntent.putExtra("resultado",2);
        broadcastIntent.putExtra("mensaje",mensaje);

        sendOrderedBroadcast(broadcastIntent,null);


    }

    private boolean saveSyncDataToDB(JSONObject jsonObjectResponse) throws Exception {
        boolean exito=false;
        JSONArray jsonArray;
        jsonArray= jsonObjectResponse.getJSONArray("tablaFamilia");
        exito=saveFamiliaData(jsonArray);
        jsonArray=jsonObjectResponse.getJSONArray("tablaPrioridad");
        exito=savePrioridadData(jsonArray);
        jsonArray=jsonObjectResponse.getJSONArray("tablaMesa");
        exito=saveMesaData(jsonArray);
        jsonArray=jsonObjectResponse.getJSONArray("tablaCarta");
        exito=saveCartaData(jsonArray);
        jsonArray=jsonObjectResponse.getJSONArray("tablaCliente");
        exito=saveClienteData(jsonArray);
        jsonArray=jsonObjectResponse.getJSONArray("tablaArticuloPrecio");
        exito=saveArticuloPrecioData(jsonArray);
        return exito;
    }

    private boolean saveFamiliaData(JSONArray jsonArrayFamilia) throws Exception {
        if (jsonArrayFamilia.length() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayFamilia.length(); i++) {
                JSONObject jsonObjItem = jsonArrayFamilia.getJSONObject(i);
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Familia.CODIGO,
                        jsonObjItem.getString("codelemento"));
                mNewValues.put(Familia.DESCRIPCION,
                        jsonObjItem.getString("descripcion"));
                mValueList.add(mNewValues);
            }
            ContentValues[] mValueArray = new ContentValues[mValueList.size()];
            mValueArray= mValueList.toArray(mValueArray);
            boolean operationResult = (getApplication().getContentResolver().bulkInsert(
                    Familia.CONTENT_URI, mValueArray) > 0);
            return operationResult;
        } else {
            throw new Exception("No hay 'Familias'.");
        }
    }
    private boolean savePrioridadData(JSONArray jsonArrayPrioridad) throws Exception {
        if (jsonArrayPrioridad.length() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayPrioridad.length(); i++) {
                JSONObject jsonObjItem = jsonArrayPrioridad.getJSONObject(i);
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Prioridad.CODIGO,
                        jsonObjItem.getString("codelemento"));
                mNewValues.put(Prioridad.DESCRIPCION,
                        jsonObjItem.getString("descripcion"));
                mValueList.add(mNewValues);
            }
            ContentValues[] mValueArray = new ContentValues[mValueList.size()];
            mValueArray= mValueList.toArray(mValueArray);
            boolean operationResult = (getApplication().getContentResolver().bulkInsert(
                    Prioridad.CONTENT_URI, mValueArray) > 0);
            return operationResult;
        } else {
            throw new Exception("No hay 'Prioridades'.");
        }
    }
    private boolean saveMesaData(JSONArray jsonArrayMesa) throws Exception {
        if (jsonArrayMesa.length() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayMesa.length(); i++) {
                JSONObject jsonObjItem = jsonArrayMesa.getJSONObject(i);
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(MesaPiso.NRO_PISO,
                        jsonObjItem.getInt("NROPISO"));
                mNewValues.put(MesaPiso.COD_AMBIENTE,
                        jsonObjItem.getInt("CAMBIENTE"));
                mNewValues.put(MesaPiso.DESC_AMBIENTE,
                        jsonObjItem.getString("DAMBIENTE"));
                mNewValues.put(MesaPiso.NRO_MESA,
                        jsonObjItem.getString("NROMESA"));
                mNewValues.put(MesaPiso.NRO_ASIENTOS,
                        jsonObjItem.getString("NROASIENTOS"));
                mNewValues.put(MesaPiso.COD_ESTADO_MESA,
                        jsonObjItem.getString("CEMESA"));
                mNewValues.put(MesaPiso.DESC_ESTADO_MESA,
                        jsonObjItem.getString("DEMESA"));
                mNewValues.put(MesaPiso.COD_RESERVA,
                        jsonObjItem.getString("CODRESERVA"));
                mValueList.add(mNewValues);
            }
            ContentValues[] mValueArray = new ContentValues[mValueList.size()];
            mValueArray= mValueList.toArray(mValueArray);
            boolean operationResult = (getApplication().getContentResolver().bulkInsert(
                    MesaPiso.CONTENT_URI, mValueArray) > 0);
            return operationResult;
        } else {
            throw new Exception("No hay 'Mesas'.");
        }
    }
    private boolean saveCartaData(JSONArray jsonArrayCarta) throws Exception {
        if (jsonArrayCarta.length() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayCarta.length(); i++) {
                JSONObject jsonObjItem = jsonArrayCarta.getJSONObject(i);
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Carta.COD_FAMILIA,
                        jsonObjItem.getString("CODTIPO"));
                mNewValues.put(Carta.COD_PRIORIDAD,
                        jsonObjItem.getString("CODPRE"));
                mNewValues.put(Carta.COD_ARTICULO,
                        jsonObjItem.getInt("CODART"));
                mNewValues.put(Carta.COD_ARTICULO_PRINC,
                        jsonObjItem.getInt("CODPRIN"));
                mValueList.add(mNewValues);
            }
            ContentValues[] mValueArray = new ContentValues[mValueList.size()];
            mValueArray= mValueList.toArray(mValueArray);
            boolean operationResult = (getApplication().getContentResolver().bulkInsert(
                    Carta.CONTENT_URI, mValueArray) > 0);
            return operationResult;
        } else {
            throw new Exception("No hay 'Carta'.");
        }
    }
    private boolean saveClienteData(JSONArray jsonArrayCliente) throws Exception {
        if (jsonArrayCliente.length() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayCliente.length(); i++) {
                JSONObject jsonObjItem = jsonArrayCliente.getJSONObject(i);
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Cliente.ID,
                        jsonObjItem.getLong("CODCLI"));
                mNewValues.put(Cliente.RAZON_SOCIAL,
                        jsonObjItem.getString("RAZONSOCIAL"));
                mNewValues.put(Cliente.RAZON_SOCIAL_NORM,
                        jsonObjItem.getString("RAZONSOCIAL"));
                mNewValues.put(Cliente.TIPO_PERSONA,
                        jsonObjItem.getString("TIPOPERSONA"));
                mNewValues.put(Cliente.NRO_DOCUMENTO,
                        jsonObjItem.getString("NROID"));
                mNewValues.put(Cliente.DIRECCION,
                        jsonObjItem.getString("DIRECCION"));
                mValueList.add(mNewValues);
            }
            ContentValues[] mValueArray = new ContentValues[mValueList.size()];
            mValueArray= mValueList.toArray(mValueArray);
            boolean operationResult = (getApplication().getContentResolver().bulkInsert(
                    Cliente.CONTENT_URI, mValueArray) > 0);
            return operationResult;
        } else {
            throw new Exception("No hay 'Clientes'.");
        }
    }
    private boolean saveArticuloPrecioData(JSONArray jsonArrayArticulo) throws Exception {
        if (jsonArrayArticulo.length() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayArticulo.length(); i++) {
                JSONObject jsonObjItem = jsonArrayArticulo.getJSONObject(i);
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Articulo.ID,
                        jsonObjItem.getLong("codart"));
                mNewValues.put(Articulo.DESCRIPCION,
                        jsonObjItem.getString("desart"));
                mNewValues.put(Articulo.DESCRIPCION_NORM,
                        jsonObjItem.getString("desart"));
                mNewValues.put(Articulo.UM,
                        jsonObjItem.getString("um"));
                mNewValues.put(Articulo.UM_DESC,
                        jsonObjItem.getString("desum"));
                mNewValues.put(Articulo.PRECIO,
                        jsonObjItem.getDouble("precio"));
                mValueList.add(mNewValues);
            }
            ContentValues[] mValueArray = new ContentValues[mValueList.size()];
            mValueArray= mValueList.toArray(mValueArray);
            boolean operationResult = (getApplication().getContentResolver().bulkInsert(
                    Articulo.CONTENT_URI, mValueArray) > 0);
            return operationResult;
        } else {
            throw new Exception("No hay 'Articulos'.");
        }
    }


}
