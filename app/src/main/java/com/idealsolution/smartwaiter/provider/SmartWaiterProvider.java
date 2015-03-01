package com.idealsolution.smartwaiter.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.database.SmartWaiterDatabase;

/**
 * Created by Usuario on 19/12/2014.
 */
public class SmartWaiterProvider extends ContentProvider{

    private SmartWaiterDatabase mDB;
    private static final UriMatcher sUriMatcher=buildUriMatcher();

    //region ConstantsForURIs
    private static final int PEDIDO_CABECERAS = 100;
    private static final int PEDIDO_CABECERAS_ID = 101;

    private static final int PEDIDO_DETALLES = 200;
    private static final int PEDIDO_DETALLES_ID = 201;

    private static final int FAMILIAS = 300;
    private static final int FAMILIAS_ID = 301;

    private static final int PRIORIDADES = 400;
    private static final int PRIORIDADES_ID = 401;

    private static final int CLIENTES = 500;
    private static final int CLIENTES_ID = 501;

    private static final int MESA_PISOS = 600;
    private static final int MESA_PISOS_ID = 601;

    private static final int CARTAS = 700;
    private static final int CARTAS_ID = 701;

    private static final int ARTICULOS = 800;
    private static final int ARTICULOS_ID = 801;
    //endregion

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority= SmartWaiterContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,"pedido_cabeceras",PEDIDO_CABECERAS);
        matcher.addURI(authority,"pedido_cabeceras/#",PEDIDO_CABECERAS_ID);

        matcher.addURI(authority,"pedido_detalles",PEDIDO_DETALLES);
        matcher.addURI(authority,"pedido_detalles/#",PEDIDO_DETALLES_ID);

        matcher.addURI(authority,"familias",FAMILIAS);
        matcher.addURI(authority,"familias/#",FAMILIAS_ID);

        matcher.addURI(authority,"prioridades",PRIORIDADES);
        matcher.addURI(authority,"prioridades/#",PRIORIDADES_ID);

        matcher.addURI(authority,"clientes",CLIENTES);
        matcher.addURI(authority,"clientes/#",CLIENTES_ID);

        matcher.addURI(authority,"mesa_pisos",MESA_PISOS);
        matcher.addURI(authority,"mesa_pisos/#",MESA_PISOS_ID);

        matcher.addURI(authority,"cartas",CARTAS);
        matcher.addURI(authority,"cartas/#",CARTAS_ID);

        matcher.addURI(authority,"articulos",ARTICULOS);
        matcher.addURI(authority,"articulos/#",ARTICULOS_ID);

        return matcher;
    }
    @Override
    public boolean onCreate() {
        // there should always be minimum operations inside
        // the onCreate as it runs on the main thread
        mDB=new SmartWaiterDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match=sUriMatcher.match(uri);
        Log.d(SmartWaiterContract.TAG,"LLegue a bulkInsert del ContentProvider. URL:" + uri.toString());
        int numInserted;
        Log.d(SmartWaiterContract.TAG,"LLegue a match=" + match);
        switch (match){
            case FAMILIAS:{
                numInserted=mDB.InsertFamilias(values);
                return numInserted;
            }
            case PRIORIDADES:{
                numInserted=mDB.InsertPrioridades(values);
                return numInserted;
            }
            case CLIENTES:{
                numInserted=mDB.InsertClientes(values);
                return numInserted;
            }
            case MESA_PISOS:{
                numInserted=mDB.InsertMesaPisos(values);
                return numInserted;
            }
            case CARTAS:{
                numInserted=mDB.InsertCarta(values);
                return numInserted;
            }
            case ARTICULOS:{
                numInserted=mDB.InsertArticulos(values);
                return numInserted;
            }
            default:{
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }
}
