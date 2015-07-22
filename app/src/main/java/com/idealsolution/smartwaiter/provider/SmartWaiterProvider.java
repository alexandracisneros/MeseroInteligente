package com.idealsolution.smartwaiter.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.MesaPiso;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Familia;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Articulo;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.PedidoCabecera;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.PedidoDetalle;
import com.idealsolution.smartwaiter.database.SmartWaiterDatabase;
import com.idealsolution.smartwaiter.database.SmartWaiterDatabase.Tables;
import com.idealsolution.smartwaiter.util.SelectionBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import static com.idealsolution.smartwaiter.util.LogUtils.LOGV;
import static com.idealsolution.smartwaiter.util.LogUtils.makeLogTag;


public class SmartWaiterProvider extends ContentProvider {
    private static final String TAG = makeLogTag(SmartWaiterProvider.class);
    private SmartWaiterDatabase mDB;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

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
    private static final int MESA_PISOS_PISOS = 602;
    private static final int MESA_PISOS_AMBIENTES = 603;

    private static final int CARTAS = 700;
    private static final int CARTAS_ID = 701;

    private static final int ARTICULOS = 800;
    private static final int ARTICULOS_ID = 801;
    private static final int ARTICULOS_ID_FAMILIA = 802;
    //endregion

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SmartWaiterContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "pedido_cabeceras", PEDIDO_CABECERAS);
        matcher.addURI(authority, "pedido_cabeceras/#", PEDIDO_CABECERAS_ID);

        matcher.addURI(authority, "pedido_detalles", PEDIDO_DETALLES);
        matcher.addURI(authority, "pedido_detalles/#", PEDIDO_DETALLES_ID);

        matcher.addURI(authority, "familias", FAMILIAS);
        matcher.addURI(authority, "familias/#", FAMILIAS_ID);

        matcher.addURI(authority, "prioridades", PRIORIDADES);
        matcher.addURI(authority, "prioridades/#", PRIORIDADES_ID);

        matcher.addURI(authority, "clientes", CLIENTES);
        matcher.addURI(authority, "clientes/#", CLIENTES_ID);

        matcher.addURI(authority, "mesa_pisos", MESA_PISOS);
        matcher.addURI(authority, "mesa_pisos/#", MESA_PISOS_ID);
        matcher.addURI(authority, "mesa_pisos/pisos", MESA_PISOS_PISOS);
        matcher.addURI(authority, "mesa_pisos/ambientes", MESA_PISOS_AMBIENTES);

        matcher.addURI(authority, "cartas", CARTAS);
        matcher.addURI(authority, "cartas/#", CARTAS_ID);

        matcher.addURI(authority, "articulos", ARTICULOS);
        matcher.addURI(authority, "articulos/#", ARTICULOS_ID);
        matcher.addURI(authority, "articulos/familia/#", ARTICULOS_ID_FAMILIA);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        // there should always be minimum operations inside
        // the onCreate as it runs on the main thread
        mDB = new SmartWaiterDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        //String tagsFilter = uri.getQueryParameter(Sessions.QUERY_PARAMETER_TAG_FILTER);
        final int match = sUriMatcher.match(uri);

        // avoid the expensive string concatenation below if not loggable
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            LOGV(TAG, "uri=" + uri + " match=" + match + " proj=" + Arrays.toString(projection) +
                    " selection=" + selection + " args=" + Arrays.toString(selectionArgs) + ")");
        }


        switch (match) {
            default: {
                // Most cases are handled with simple SelectionBuilder
                final SelectionBuilder builder = buildExpandedSelection(uri, match);

                // If a special filter was specified, try to apply it
//                if (!TextUtils.isEmpty(tagsFilter)) {
//                    addTagsFilter(builder, tagsFilter);
//                }

                boolean distinct = !TextUtils.isEmpty(
                        uri.getQueryParameter(SmartWaiterContract.QUERY_PARAMETER_DISTINCT));

                Cursor cursor = builder
                        .where(selection, selectionArgs)
                        .query(mDB, distinct, projection, sortOrder, null);

                Context context = getContext();
                if (null != context) {
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                }
                return cursor;
            }
//            case SEARCH_SUGGEST: {
//                final SelectionBuilder builder = new SelectionBuilder();
//
//                // Adjust incoming query to become SQL text match
//                selectionArgs[0] = selectionArgs[0] + "%";
//                builder.table(Tables.SEARCH_SUGGEST);
//                builder.where(selection, selectionArgs);
//                builder.map(SearchManager.SUGGEST_COLUMN_QUERY,
//                        SearchManager.SUGGEST_COLUMN_TEXT_1);
//
//                projection = new String[] {
//                        BaseColumns._ID,
//                        SearchManager.SUGGEST_COLUMN_TEXT_1,
//                        SearchManager.SUGGEST_COLUMN_QUERY
//                };
//
//                final String limit = uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT);
//                return builder.query(db, false, projection, SearchSuggest.DEFAULT_SORT, limit);
//            }
        }
    }

    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case PEDIDO_CABECERAS:{
                return builder.table(Tables.PEDIDO_CABECERA);
            }
            case PEDIDO_DETALLES:{
                return builder.table(Tables.PEDIDO_DETALLE);
            }
            case FAMILIAS: {
                return builder.table(Tables.FAMILIA);
            }
            case MESA_PISOS: {
                return builder.table(Tables.MESA_PISO);
            }
            case MESA_PISOS_PISOS: {
                return builder.table(Tables.MESA_PISO);
            }
            case MESA_PISOS_AMBIENTES: {
                return builder.table(Tables.MESA_PISO);
            }
            case ARTICULOS_ID_FAMILIA: {
                final String familiaId = Familia.getFamiliaId(uri);
                return builder.table(Tables.ARTICULOS_JOIN_CARTA, familiaId)
                        .mapToTable(Articulo.ID, Tables.ARTICULO);

            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PEDIDO_CABECERAS:
                return PedidoCabecera.CONTENT_TYPE;
            case PEDIDO_DETALLES:
                return PedidoDetalle.CONTENT_TYPE;
            case PEDIDO_DETALLES_ID:
                return PedidoDetalle.CONTENT_ITEM_TYPE;
            case FAMILIAS:
                return Familia.CONTENT_TYPE;
            //Mesa Piso - Inicio
            case MESA_PISOS:
                return MesaPiso.CONTENT_TYPE;
            case MESA_PISOS_ID:
                return MesaPiso.CONTENT_ITEM_TYPE;
            case MESA_PISOS_PISOS:
                return MesaPiso.CONTENT_TYPE;
            case MESA_PISOS_AMBIENTES:
                return MesaPiso.CONTENT_TYPE;
            //Mesa Piso - Fin
            case ARTICULOS_ID_FAMILIA:
                return Articulo.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        Log.d(SmartWaiterContract.TAG, "LLegue a INSERT del ContentProvider. URL:" + uri.toString());
        long insertedRecordID;
        Log.d(SmartWaiterContract.TAG, "LLegue a match=" + match);
        switch (match) {
            case PEDIDO_CABECERAS: {
                insertedRecordID = mDB.insertOrThrow(Tables.PEDIDO_CABECERA, null, values);
                //notifyChange(uri);
                return PedidoCabecera.buildPedidoCabeceraUri(String.valueOf(insertedRecordID));
            }
            case PEDIDO_DETALLES: {
                insertedRecordID = mDB.insertOrThrow(Tables.PEDIDO_DETALLE, null, values);
                //notifyChange(uri);
                return PedidoDetalle.buildPedidoDetalleUri(String.valueOf(insertedRecordID));
            }
            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
       int deleteCount;
        final int match=sUriMatcher.match(uri);
        switch (match){
            case PEDIDO_CABECERAS:
                deleteCount=mDB.delete(Tables.PEDIDO_CABECERA,selection,selectionArgs);
                break;
            case PEDIDO_DETALLES:
                deleteCount=mDB.delete(Tables.PEDIDO_DETALLE,selection,selectionArgs);
                break;
            case PEDIDO_DETALLES_ID:
                String detalleID = uri.getLastPathSegment();
                String where = PedidoDetalle.ID + " = " + detalleID;
                if(!TextUtils.isEmpty(selection)){
                    where +=" AND " + selection;
                }
                deleteCount=mDB.delete(Tables.PEDIDO_DETALLE,where,selectionArgs);
                break;
            default:
                throw  new UnsupportedOperationException("Unknown delete uri: " + uri);
        }
        if(deleteCount>0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return  deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    //TODO Correct this part. You don't have to have method for each insert operation. Take a look to the insert in the IO 2014
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        Log.d(SmartWaiterContract.TAG, "LLegue a bulkInsert del ContentProvider. URL:" + uri.toString());
        int numInserted;
        Log.d(SmartWaiterContract.TAG, "LLegue a match=" + match);
        switch (match) {
            case FAMILIAS: {
                numInserted = mDB.InsertFamilias(values);
                return numInserted;
            }
            case PRIORIDADES: {
                numInserted = mDB.InsertPrioridades(values);
                return numInserted;
            }
            case CLIENTES: {
                numInserted = mDB.InsertClientes(values);
                return numInserted;
            }
            case MESA_PISOS: {
                numInserted = mDB.InsertMesaPisos(values);
                return numInserted;
            }
            case CARTAS: {
                numInserted = mDB.InsertCarta(values);
                return numInserted;
            }
            case ARTICULOS: {
                numInserted = mDB.InsertArticulos(values);
                return numInserted;
            }
            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }


    /**
     * Apply the given set of {@link ContentProviderOperation}, executing inside
     * a {link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        return mDB.applyBatch(this, operations);
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAMILIAS: {
                return builder.table(Tables.FAMILIA);
            }
            case PRIORIDADES: {
//                final String blockId = Blocks.getBlockId(uri);
//                return builder.table(SmartWaiterDatabase.Tables.BLOCKS)
//                        .where(Blocks.BLOCK_ID + "=?", blockId);
                return builder.table(Tables.PRIORIDAD);
            }
            case CLIENTES: {
                return builder.table(Tables.CLIENTE);
            }
            case MESA_PISOS: {
//                final String tagId = Tags.getTagId(uri);
//                return builder.table(Tables.TAGS)
//                        .where(Tags.TAG_ID + "=?", tagId);
                return builder.table(Tables.MESA_PISO);
            }
            case MESA_PISOS_PISOS: {
                return builder.table(Tables.MESA_PISO);
            }
            case MESA_PISOS_AMBIENTES: {
                return builder.table(Tables.MESA_PISO);
            }
            case CARTAS: {
//                return builder.table(Tables.ROOMS);
                return builder.table(Tables.CARTA);
            }
            case ARTICULOS: {
//                final String roomId = Rooms.getRoomId(uri);
//                return builder.table(Tables.ROOMS)
//                        .where(Rooms.ROOM_ID + "=?", roomId);
                return builder.table(Tables.ARTICULO);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri for " + match + ": " + uri);
            }
        }
    }
}
