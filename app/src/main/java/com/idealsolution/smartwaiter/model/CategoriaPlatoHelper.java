package com.idealsolution.smartwaiter.model;


import android.database.Cursor;
import android.os.AsyncTask;

import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Familia;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Articulo;
import com.idealsolution.smartwaiter.ui.CategoryActivity;


import static com.idealsolution.smartwaiter.util.LogUtils.makeLogTag;

public class CategoriaPlatoHelper {
    private static final String TAG = makeLogTag(CategoriaPlatoHelper.class);
    private CategoryActivity mContext;

    public CategoriaPlatoHelper(CategoryActivity context) {
        this.mContext = context;
    }

    public void getCategoriasAsync() {

        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                return mContext.getApplicationContext().getContentResolver().query(
                        Familia.CONTENT_URI,
                        CategoriasQuery.PROJECTION,
                        null,
                        null,
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                while (cursor.moveToNext()) {
                    CategoriaObject item = new CategoriaObject();
                    item.setId(cursor.getInt(CategoriasQuery.ID));
                    item.setCodigo(cursor.getString(CategoriasQuery.CODIGO));
                    item.setDescripcion(cursor.getString(CategoriasQuery.DESCRIPCION));
                    item.setUrl(cursor.getString(CategoriasQuery.URL));
                    //mContext.getListaObjectMesas().add(item);
                }
                cursor.close();
//                mContext.setMesasAdapter(new MesaItemAdapter(mContext,mContext.getListaObjectMesas()));
//                mContext.getMesasAdapter().setOnItemClickListener(mContext);
//                mContext.getRecylerView().setAdapter(mContext.getMesasAdapter());

            }
        }.execute();
    }

    private interface CategoriasQuery {
        String[] PROJECTION = {
                Familia.ID,
                Familia.CODIGO,
                Familia.DESCRIPCION,
                Familia.URL
        };
        int ID = 0;
        int CODIGO = 1;
        int DESCRIPCION = 2;
        int URL = 3;
    }
    private interface PlatosQuery{
        String[] PROJECTION={
                Articulo.ID,
                Articulo.DESCRIPCION_NORM,
                Articulo.UM,
                Articulo.UM_DESC,
                Articulo.PRECIO,
                Articulo.URL
        };
        int ARTICULO_ID=0;
        int ARTICULO_DESC=1;
        int ARTICULO_UM=2;
        int ARTICULO_UM_DESC=3;
        int ARTICULO_PRECIO=4;
        int ARTICULO_URL=5;
    }

}
