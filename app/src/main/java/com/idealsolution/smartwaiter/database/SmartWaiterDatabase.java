package com.idealsolution.smartwaiter.database;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;

import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.*;
import com.idealsolution.smartwaiter.provider.SmartWaiterProvider;

import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;


import static com.idealsolution.smartwaiter.util.LogUtils.LOGV;
import static com.idealsolution.smartwaiter.util.LogUtils.makeLogTag;


/**
 * Created by Usuario on 16/12/2014.
 */
public class SmartWaiterDatabase extends SQLiteOpenHelper {
    // database constants
    public static final String DB_NAME = "SmartWaiter.db";
    public static final int DB_VERSION = 1;
    private final Context mContext;
    private static SmartWaiterDatabase sSingleton = null;
    private SQLiteDatabase db;

    public static synchronized SmartWaiterDatabase getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new SmartWaiterDatabase(context);
        }
        return sSingleton;
    }

    public SmartWaiterDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext=context;
    }
    public void openDatabase(boolean writable) {
        db= writable ? getWritableDatabase() : getReadableDatabase();
    }



    public interface Tables {
        static final String PEDIDO_CABECERA="pedido_cabecera";
        static final String PEDIDO_DETALLE="pedido_detalle";
        static final String FAMILIA="familia";
        static final String PRIORIDAD = "prioridad";
        static final String CLIENTE = "cliente";
        static final String MESA_PISO = "mesa_piso";
        static final String CARTA = "carta";
        static final String ARTICULO = "articulo";

        static final String ARTICULOS_JOIN_CARTA=ARTICULO + " JOIN " + CARTA
                + " ON " + ARTICULO + "." + Articulo.ID + " = " + CARTA + "." + Carta.COD_ARTICULO
                + " AND " + CARTA + "." + Carta.COD_FAMILIA + "=? " ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + Tables.PEDIDO_CABECERA + " ("
                + PedidoCabecera.ID				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PedidoCabecera.FECHA			+ " TEXT NOT NULL,"
                + PedidoCabecera.NRO_MESA 		+ " INTEGER NOT NULL,"
                + PedidoCabecera.AMBIENTE 		+ " INTEGER NOT NULL,"
                + PedidoCabecera.CODIGO_USUARIO + " TEXT NOT NULL,"
                + PedidoCabecera.CODIGO_CLIENTE	+ " INTEGER,"
                + PedidoCabecera.TIPO_VENTA		+ " TEXT,"
                + PedidoCabecera.TIPO_PAGO 		+ " TEXT,"
                + PedidoCabecera.MONEDA         + " TEXT,"
                + PedidoCabecera.MONTO_TOTAL  	+ " REAL NOT NULL,"
                + PedidoCabecera.MONTO_RECIBIDO + " REAL,"
                + PedidoCabecera.ESTADO  		+ " INTEGER NOT NULL"
                + " )"
        );
        db.execSQL("CREATE TABLE "
                + Tables.PEDIDO_DETALLE + " ("
                + PedidoDetalle.ID				    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PedidoDetalle.PEDIDO_ID   	    + " INTEGER NOT NULL,"
                + PedidoDetalle.COD_ART	            + " INTEGER NOT NULL,"
                + PedidoDetalle.CANTIDAD   	        + " REAL NOT NULL,"
                + PedidoDetalle.PRECIO              + " REAL NOT NULL,"
                + PedidoDetalle.TIPO_ART    	    + " INTEGER NOT NULL,"
                + PedidoDetalle.COD_ART_PRINCIPAL   + " INTEGER,"
                + PedidoDetalle.COMENTARIO          + " TEXT,"
                + PedidoDetalle.ESTADO_ART          + " INTEGER NOT NULL"
                + " )"
        );
        db.execSQL("CREATE TABLE "
                + Tables.FAMILIA + " ("
                + Familia.ID				 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Familia.CODIGO			 + " TEXT NOT NULL,"
                + Familia.DESCRIPCION    	 + " TEXT,"
                + Familia.URL                + " TEXT "
                + " )"
        );
        db.execSQL("CREATE TABLE "
                + Tables.PRIORIDAD + " ("
                + Prioridad.ID				 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Prioridad.CODIGO			 + " TEXT NOT NULL,"
                + Prioridad.DESCRIPCION    	 + " TEXT"
                + " )"
        );
        db.execSQL("CREATE TABLE "
                + Tables.CLIENTE + " ("
                + Cliente.ID				       + " INTEGER PRIMARY KEY, "
                + Cliente.RAZON_SOCIAL			   + " TEXT ,"
                + Cliente.RAZON_SOCIAL_NORM    	   + " TEXT,"
                + Cliente.TIPO_PERSONA    	       + " TEXT,"
                + Cliente.NRO_DOCUMENTO    	       + " TEXT,"
                + Cliente.DIRECCION    	           + " TEXT"
                + " )"
        );
        db.execSQL("CREATE TABLE "
                + Tables.MESA_PISO + " ("
                + MesaPiso.ID				     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MesaPiso.NRO_PISO			     + " INTEGER NOT NULL,"
                + MesaPiso.COD_AMBIENTE    	     + " INTEGER,"
                + MesaPiso.DESC_AMBIENTE    	 + " TEXT,"
                + MesaPiso.NRO_MESA    	         + " INTEGER,"
                + MesaPiso.NRO_ASIENTOS    	     + " INTEGER,"
                + MesaPiso.COD_ESTADO_MESA       + " TEXT,"
                + MesaPiso.DESC_ESTADO_MESA      + " TEXT,"
                + MesaPiso.COD_RESERVA    	     + " INTEGER"
                + " )"
        );
        db.execSQL("CREATE TABLE "
                + Tables.CARTA + " ("
                + Carta.ID				        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Carta.COD_FAMILIA		        + " TEXT NOT NULL,"
                + Carta.COD_PRIORIDAD           + " TEXT,"
                + Carta.COD_ARTICULO    	    + " INTEGER,"
                + Carta.COD_ARTICULO_PRINC      + " INTEGER"
                + " )"
        );
        db.execSQL( "CREATE TABLE "
                + Tables.ARTICULO + " ("
                + Articulo.ID				    + " INTEGER PRIMARY KEY , "
                + Articulo.DESCRIPCION          + " TEXT,"
                + Articulo.DESCRIPCION_NORM    	+ " TEXT,"
                + Articulo.UM                   + " TEXT,"
                + Articulo.UM_DESC              + " TEXT,"
                + Articulo.PRECIO               + " REAL,"
                + Articulo.COD_LISTAPRECIO      + " INTEGER,"
                + Articulo.URL                  + " TEXT"
                + " )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "	+ Tables.PEDIDO_CABECERA);
        db.execSQL("DROP TABLE IF EXISTS "	+ Tables.PEDIDO_DETALLE);
        db.execSQL("DROP TABLE IF EXISTS "	+ Tables.FAMILIA);
        db.execSQL("DROP TABLE IF EXISTS "	+ Tables.PRIORIDAD);
        db.execSQL("DROP TABLE IF EXISTS "	+ Tables.CLIENTE);
        db.execSQL("DROP TABLE IF EXISTS "	+ Tables.MESA_PISO);
        db.execSQL("DROP TABLE IF EXISTS "	+ Tables.CARTA);
        db.execSQL("DROP TABLE IF EXISTS "	+ Tables.ARTICULO);
    }

    //region FAMILIAS
    public int InsertFamilias(ContentValues[] values) {
        int numInserted = 0;
        String insertQuery = "INSERT INTO "	+ Tables.FAMILIA +
                "(" + Familia.CODIGO + "," + Familia.DESCRIPCION + "," + Familia.URL +") VALUES (?,?,?)";
        try {
            this.openDatabase(true); //true=getWritableDatabase
            SQLiteStatement statement=db.compileStatement(insertQuery);
            Log.d(SmartWaiterContract.TAG, "LLegue a InsertFamilias. Nro Items in FamiliaArray:" + values.length);
            db.beginTransaction();
            for (int i=0; i<values.length;i++) {
                statement.clearBindings();
                statement.bindString(1,values[i].getAsString(Familia.CODIGO));
                statement.bindString(2,values[i].getAsString(Familia.DESCRIPCION));
                statement.bindString(3,values[i].getAsString(Familia.URL));
                statement.execute();
            }
            db.setTransactionSuccessful();
            numInserted = values.length;
        } finally {
            db.endTransaction();
        }
        return numInserted;
    }
    //endregion

    //region PRIORIDAD
    public int InsertPrioridades(ContentValues[] values){
        int numInserted=0;
        String insertQuery="INSERT INTO " + Tables.PRIORIDAD +
                " (" + Prioridad.CODIGO + "," + Prioridad.DESCRIPCION + ") VALUES (?,?)";
        try{
            this.openDatabase(true); //true=getWritableDatabase
            SQLiteStatement statement=db.compileStatement(insertQuery);
            db.beginTransaction();
            for(int i=0;i<values.length;i++){
                statement.clearBindings();
                statement.bindString(1,values[i].getAsString(Prioridad.CODIGO));
                statement.bindString(2,values[i].getAsString(Prioridad.DESCRIPCION));
                statement.execute();
            }
            db.setTransactionSuccessful();
            numInserted=values.length;
        }finally {
            db.endTransaction();
        }
        return numInserted;
    }
    //endregion

    //region CLIENTES
    public int InsertClientes(ContentValues[] values){
        int numInserted=0;
        String insertQuery="INSERT INTO " + Tables.CLIENTE + "( "+
                Cliente.ID + "," +
                Cliente.RAZON_SOCIAL + "," + Cliente.RAZON_SOCIAL_NORM + "," +
                Cliente.TIPO_PERSONA + "," + Cliente.NRO_DOCUMENTO + "," +
                Cliente.DIRECCION + ") "+
                "VALUES (?,?,?,?,?,?)";
        Log.d(SmartWaiterContract.TAG,"query:" + insertQuery);
        try{
            this.openDatabase(true);
            SQLiteStatement statement=db.compileStatement(insertQuery);
            db.beginTransaction();
            for(int i=0;i<values.length;i++){
                statement.clearBindings();
                statement.bindLong(1, values[i].getAsLong(Cliente.ID));
                statement.bindString(2,values[i].getAsString(Cliente.RAZON_SOCIAL));
                String stringToStore = Normalizer.normalize(values[i].getAsString(Cliente.RAZON_SOCIAL)
                                .toLowerCase(Locale.getDefault()),
                        Normalizer.Form.NFC);
                statement.bindString(3,stringToStore);
                statement.bindString(4,values[i].getAsString(Cliente.TIPO_PERSONA));
                statement.bindString(5,values[i].getAsString(Cliente.NRO_DOCUMENTO));
                statement.bindString(6,values[i].getAsString(Cliente.DIRECCION));
                statement.execute();
            }
            db.setTransactionSuccessful();
            numInserted=values.length;

        }finally {
            db.endTransaction();
        }
        return numInserted;
    }
    //endregion

    //region MESAPISOS
    public int InsertMesaPisos(ContentValues[] values){
        int numInserted=0;
        String insertQuery="INSERT INTO " + Tables.MESA_PISO + "( "+
                MesaPiso.NRO_PISO + "," + MesaPiso.COD_AMBIENTE + "," +
                MesaPiso.DESC_AMBIENTE + "," + MesaPiso.NRO_MESA + "," +
                MesaPiso.NRO_ASIENTOS + "," + MesaPiso.COD_ESTADO_MESA + "," +
                MesaPiso.DESC_ESTADO_MESA + "," + MesaPiso.COD_RESERVA + " ) "+
                "VALUES (?,?,?,?,?,?,?,?)";
        try{
            this.openDatabase(true);
            SQLiteStatement statement=db.compileStatement(insertQuery);
            db.beginTransaction();
            for(int i=0;i<values.length;i++){
                statement.clearBindings();
                statement.bindLong(1, values[i].getAsInteger(MesaPiso.NRO_PISO));
                statement.bindLong(2, values[i].getAsInteger(MesaPiso.COD_AMBIENTE));
                statement.bindString(3,values[i].getAsString(MesaPiso.DESC_AMBIENTE));
                statement.bindLong(4, values[i].getAsInteger(MesaPiso.NRO_MESA));
                statement.bindLong(5, values[i].getAsInteger(MesaPiso.NRO_ASIENTOS));
                statement.bindString(6,values[i].getAsString(MesaPiso.COD_ESTADO_MESA));
                statement.bindString(7,values[i].getAsString(MesaPiso.DESC_ESTADO_MESA));
                statement.bindLong(8, values[i].getAsInteger(MesaPiso.COD_RESERVA));
                statement.execute();
            }
            db.setTransactionSuccessful();
            numInserted=values.length;

        }finally {
            db.endTransaction();
        }
        return numInserted;
    }
    //endregion

    //region CARTA
    public int InsertCarta(ContentValues[] values){
        int numInserted=0;
        String insertQuery="INSERT INTO " + Tables.CARTA + "( "+
                Carta.COD_FAMILIA + "," + Carta.COD_PRIORIDAD + "," +
                Carta.COD_ARTICULO + "," + Carta.COD_ARTICULO_PRINC + ") " +
                "VALUES (?,?,?,?)";
        try{
            this.openDatabase(true);
            SQLiteStatement statement=db.compileStatement(insertQuery);
            db.beginTransaction();
            for(int i=0;i<values.length;i++){
                statement.clearBindings();
                statement.bindString(1, values[i].getAsString(Carta.COD_FAMILIA));
                statement.bindString(2, values[i].getAsString(Carta.COD_PRIORIDAD));
                statement.bindLong(3,values[i].getAsLong(Carta.COD_ARTICULO));
                statement.bindLong(4, values[i].getAsLong(Carta.COD_ARTICULO_PRINC));
                statement.execute();
            }
            db.setTransactionSuccessful();
            numInserted=values.length;

        }finally {
            db.endTransaction();
        }
        return numInserted;
    }
    //endregion

    //region ARTICULOS
    public int InsertArticulos(ContentValues[] values){
        int numInserted=0;
        String insertQuery="INSERT INTO " + Tables.ARTICULO + "( "+
                Articulo.ID + "," + Articulo.DESCRIPCION + "," +
                Articulo.DESCRIPCION_NORM + "," + Articulo.UM + "," +
                Articulo.UM_DESC + "," + Articulo.PRECIO + "," +
                Articulo.COD_LISTAPRECIO + "," + Articulo.URL + ") " +
                "VALUES (?,?,?,?,?,?,?,?)";
        try{
            this.openDatabase(true);
            SQLiteStatement statement=db.compileStatement(insertQuery);
            db.beginTransaction();
            for(int i=0;i<values.length;i++){
                statement.clearBindings();
                statement.bindLong(1, values[i].getAsLong(Articulo.ID));
                statement.bindString(2, values[i].getAsString(Articulo.DESCRIPCION));
                String stringToStore = Normalizer.normalize(values[i].getAsString(Articulo.DESCRIPCION)
                                .toLowerCase(Locale.getDefault()),
                        Normalizer.Form.NFC);
                statement.bindString(3,stringToStore);
                statement.bindString(4, values[i].getAsString(Articulo.UM));
                statement.bindString(5,values[i].getAsString(Articulo.UM_DESC));
                statement.bindDouble(6, values[i].getAsDouble(Articulo.PRECIO));
                statement.bindLong(7,values[i].getAsLong(Articulo.COD_LISTAPRECIO));
                statement.bindString(8,values[i].getAsString(Articulo.URL));
                statement.execute();
            }
            db.setTransactionSuccessful();
            numInserted=values.length;

        }finally {
            db.endTransaction();
        }
        return numInserted;
    }
    //endregion
    public ContentProviderResult[] applyBatch(SmartWaiterProvider context,ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        this.openDatabase(true);
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(context, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }
    public long insertOrThrow(String table,  String nullColumnHack, ContentValues values) {
            return db.insertOrThrow(table,nullColumnHack,values);
    }

    /**
     * Execute query using the current internal state as {@code WHERE} clause.
     */
    public Cursor query(boolean distinct,String table, String[] columns,
                        String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit) {
        try {
            this.openDatabase(false);
            return db.query(distinct,table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        finally {

        }
    }

//    /**
//     * Execute update using the current internal state as {@code WHERE} clause.
//     */
//    public int update(SQLiteDatabase db, ContentValues values) {
//        assertTable();
//        LOGV(TAG, "update() " + this);
//        return db.update(mTable, values, getSelection(), getSelectionArgs());
//    }
//
//    /**
//     * Execute delete using the current internal state as {@code WHERE} clause.
//     */
//    public int delete(SQLiteDatabase db) {
//        assertTable();
//        LOGV(TAG, "delete() " + this);
//        return db.delete(mTable, getSelection(), getSelectionArgs());
//    }

}
