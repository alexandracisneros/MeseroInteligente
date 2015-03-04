package com.idealsolution.smartwaiter.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.*;
import com.idealsolution.smartwaiter.io.RestUtil;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;



import java.util.ArrayList;
import java.util.List;


public class SincronizarService extends IntentService implements FutureCallback<JsonObject> {
    private static final String NAME = "SincronizarService";
    public static final String ACTION_SYNC_DATA="com.idealsolution.smartwaiter.SYNC_DATA";
    private Exception miExcepcion=null;
    private boolean exito=false;
    private String mensaje = "";
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

            if (networkInfo != null && networkInfo.isConnected()) {
                Ion.with(getApplicationContext())
                    .load(RestUtil.URLServer
                        + "ObtenerDatosIniciales/?codCia=001&cadenaConexion=Initial%20Catalog=ABR")
                        .asJsonObject().setCallback(this);
            }
            else {
                mensaje="Imposible conectarse a Internet.";
                exito=false;
                enviarNotificacion();
            }
    }

    private boolean saveSyncDataToDB(JsonObject jsonObjectResponse) throws Exception {

        JsonArray jsonArray;
        jsonArray= jsonObjectResponse.getAsJsonArray("tablaFamilia");
        exito=saveFamiliaData(jsonArray);
        jsonArray=jsonObjectResponse.getAsJsonArray("tablaPrioridad");
        exito=savePrioridadData(jsonArray);
        jsonArray=jsonObjectResponse.getAsJsonArray("tablaMesa");
        exito=saveMesaData(jsonArray);
        jsonArray=jsonObjectResponse.getAsJsonArray("tablaCarta");
        exito=saveCartaData(jsonArray);
        jsonArray=jsonObjectResponse.getAsJsonArray("tablaCliente");
        exito=saveClienteData(jsonArray);
        jsonArray=jsonObjectResponse.getAsJsonArray("tablaArticuloPrecio");
        exito=saveArticuloPrecioData(jsonArray);

        return exito;
    }

    private boolean saveFamiliaData(JsonArray jsonArrayFamilia) throws Exception {

         if (jsonArrayFamilia.size() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayFamilia.size(); i++) {
                JsonObject jsonObjItem = jsonArrayFamilia.get(i).getAsJsonObject();
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Familia.CODIGO,
                        jsonObjItem.get("codelemento").getAsString());
                mNewValues.put(Familia.DESCRIPCION,
                        jsonObjItem.get("descripcion").getAsString());
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
    private boolean savePrioridadData(JsonArray jsonArrayPrioridad) throws Exception {


        if (jsonArrayPrioridad.size() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayPrioridad.size(); i++) {
                JsonObject jsonObjItem = jsonArrayPrioridad.get(i).getAsJsonObject();
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Prioridad.CODIGO,
                        jsonObjItem.get("codelemento").getAsString());
                mNewValues.put(Prioridad.DESCRIPCION,
                        jsonObjItem.get("descripcion").getAsString());
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
    private boolean saveMesaData(JsonArray jsonArrayMesa) throws Exception {
        if (jsonArrayMesa.size() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayMesa.size(); i++) {
                JsonObject jsonObjItem = jsonArrayMesa.get(i).getAsJsonObject();
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(MesaPiso.NRO_PISO,
                        jsonObjItem.get("NROPISO").getAsInt());
                mNewValues.put(MesaPiso.COD_AMBIENTE,
                        jsonObjItem.get("CAMBIENTE").getAsInt());
                mNewValues.put(MesaPiso.DESC_AMBIENTE,
                        jsonObjItem.get("DAMBIENTE").getAsString());
                mNewValues.put(MesaPiso.NRO_MESA,
                        jsonObjItem.get("NROMESA").getAsString());
                mNewValues.put(MesaPiso.NRO_ASIENTOS,
                        jsonObjItem.get("NROASIENTOS").getAsString());
                mNewValues.put(MesaPiso.COD_ESTADO_MESA,
                        jsonObjItem.get("CEMESA").getAsString());
                mNewValues.put(MesaPiso.DESC_ESTADO_MESA,
                        jsonObjItem.get("DEMESA").getAsString());
                mNewValues.put(MesaPiso.COD_RESERVA,
                        jsonObjItem.get("CODRESERVA").getAsString());
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
    private boolean saveCartaData(JsonArray jsonArrayCarta) throws Exception {
        if (jsonArrayCarta.size() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayCarta.size(); i++) {
                JsonObject jsonObjItem = jsonArrayCarta.get(i).getAsJsonObject();
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Carta.COD_FAMILIA,
                        jsonObjItem.get("CODTIPO").getAsString());
                mNewValues.put(Carta.COD_PRIORIDAD,
                        jsonObjItem.get("CODPRE").getAsString());
                mNewValues.put(Carta.COD_ARTICULO,
                        jsonObjItem.get("CODART").getAsInt());
                mNewValues.put(Carta.COD_ARTICULO_PRINC,
                        jsonObjItem.get("CODPRIN").getAsInt());
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
    private boolean saveClienteData(JsonArray jsonArrayCliente) throws Exception {
        if (jsonArrayCliente.size() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayCliente.size(); i++) {
                JsonObject jsonObjItem = jsonArrayCliente.get(i).getAsJsonObject();
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Cliente.ID,
                        jsonObjItem.get("CODCLI").getAsLong());
                mNewValues.put(Cliente.RAZON_SOCIAL,
                        jsonObjItem.get("RAZONSOCIAL").getAsString());
                mNewValues.put(Cliente.RAZON_SOCIAL_NORM,
                        jsonObjItem.get("RAZONSOCIAL").getAsString());
                mNewValues.put(Cliente.TIPO_PERSONA,
                        jsonObjItem.get("TIPOPERSONA").getAsString());
                mNewValues.put(Cliente.NRO_DOCUMENTO,
                        jsonObjItem.get("NROID").getAsString());
                mNewValues.put(Cliente.DIRECCION,
                        jsonObjItem.get("DIRECCION").getAsString());
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
    private boolean saveArticuloPrecioData(JsonArray jsonArrayArticulo) throws Exception {
        if (jsonArrayArticulo.size() > 0) {
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int i = 0; i < jsonArrayArticulo.size(); i++) {
                JsonObject jsonObjItem = jsonArrayArticulo.get(i).getAsJsonObject();
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(Articulo.ID,
                        jsonObjItem.get("codart").getAsLong());
                mNewValues.put(Articulo.DESCRIPCION,
                        jsonObjItem.get("desart").getAsString());
                mNewValues.put(Articulo.DESCRIPCION_NORM,
                        jsonObjItem.get("desart").getAsString());
                mNewValues.put(Articulo.UM,
                        jsonObjItem.get("um").getAsString());
                mNewValues.put(Articulo.UM_DESC,
                        jsonObjItem.get("desum").getAsString());
                mNewValues.put(Articulo.PRECIO,
                        jsonObjItem.get("precio").getAsDouble());
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


    @Override
    public void onCompleted(Exception e, JsonObject result) {
        if(e!=null){
            exito=false;
            mensaje=e.getMessage();
        }
        if(result!=null){
            try {
                exito=saveSyncDataToDB(result);
                Log.d(SmartWaiterContract.TAG,"Sincronizacion Correcta");
                exito=true;
            } catch (Exception ex) {
                exito=false;
                mensaje=ex.getMessage();
            }
        }
        enviarNotificacion();
    }
    private void enviarNotificacion(){
        Intent broadcastIntent=new Intent();
        broadcastIntent.setAction(SincronizarService.ACTION_SYNC_DATA);
        broadcastIntent.putExtra("exito",exito);
        broadcastIntent.putExtra("resultado",2);
        broadcastIntent.putExtra("mensaje",mensaje);

        sendOrderedBroadcast(broadcastIntent,null);
    }
}
