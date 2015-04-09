package com.idealsolution.smartwaiter.model;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.RemoteException;


import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Articulo;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.PedidoCabecera;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.PedidoDetalle;
import com.idealsolution.smartwaiter.ui.ArticuloItemAdapter;
import com.idealsolution.smartwaiter.ui.CategoryActivity;


import java.util.ArrayList;

import static com.idealsolution.smartwaiter.util.LogUtils.makeLogTag;

/**
 * Created by Usuario on 02/04/2015.
 */
public class ArticuloHelper {
    private static final String TAG = makeLogTag(CategoriaHelper.class);
    private CategoryActivity mContext;

    public ArticuloHelper(CategoryActivity context) {
        this.mContext = context;
    }

    public void getArticuloPorFamiliaAsync(final int familiaId) {

        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                return mContext.getApplicationContext().getContentResolver().query(
                        Articulo.buildArticuloFamiliaUri(familiaId),
                        ArticulosQuery.PROJECTION,
                        null,
                        null,
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                while (cursor.moveToNext()) {
                    ArticuloObject item = new ArticuloObject();
                    item.setId(cursor.getInt(ArticulosQuery.ID));
                    item.setDescripcionNorm(cursor.getString(ArticulosQuery.DESCRIPCION_NORM));
                    item.setUm(cursor.getString(ArticulosQuery.UM));
                    item.setUmDescripcion(cursor.getString(ArticulosQuery.UM_DESC));
                    item.setPrecio(cursor.getFloat(ArticulosQuery.PRECIO));
                    item.setUrl(cursor.getString(ArticulosQuery.URL));

                    mContext.getListaArticulos().add(item);
                }
                cursor.close();
                mContext.setAdapterPlatos(new ArticuloItemAdapter(mContext, mContext.getListaArticulos()));
                mContext.getRecyclerViewPlatos().setAdapter(mContext.getAdapterPlatos());

            }
        }.execute();
    }

    public void PedidoDetalleApplyBatch(final PedidoCabObject pedido) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        ContentValues cvPedido = new ContentValues();
        //ContentValues cvDetalle;
        cvPedido.put(PedidoCabecera.FECHA, pedido.getFecha());
        cvPedido.put(PedidoCabecera.NRO_MESA, pedido.getNro_mesa());
        cvPedido.put(PedidoCabecera.AMBIENTE, pedido.getAmbiente());
        cvPedido.put(PedidoCabecera.CODIGO_USUARIO, pedido.getCod_usuario());
        cvPedido.put(PedidoCabecera.CODIGO_CLIENTE, pedido.getCod_cliente());
        cvPedido.put(PedidoCabecera.TIPO_VENTA, pedido.getTipo_venta());
        cvPedido.put(PedidoCabecera.TIPO_PAGO, pedido.getTipo_pago());
        cvPedido.put(PedidoCabecera.MONEDA, pedido.getMoneda());
        cvPedido.put(PedidoCabecera.MONTO_TOTAL, pedido.getMonto_total());
        cvPedido.put(PedidoCabecera.MONTO_RECIBIDO, pedido.getMonto_recibido());
        cvPedido.put(PedidoCabecera.ESTADO, pedido.getEstado());

        operations.add(
                ContentProviderOperation.newInsert(PedidoCabecera.CONTENT_URI)
                        .withValues(cvPedido)
                        .build());
        for (PedidoDetObject det : pedido.getDetalle()) {
            operations.add(ContentProviderOperation.newInsert(PedidoDetalle.CONTENT_URI)
                    .withValueBackReference(PedidoDetalle.PEDIDO_ID, 0) //This field references back the returned value of the first element
                    .withValue(PedidoDetalle.COD_ART, det.getCod_articulo())
                    .withValue(PedidoDetalle.CANTIDAD, det.getCantidad())
                    .withValue(PedidoDetalle.PRECIO, det.getPrecio())
                    .withValue(PedidoDetalle.TIPO_ART, det.getTipo_articulo())
                    .withValue(PedidoDetalle.COD_ART_PRINCIPAL, det.getCod_art_principal())
                    .withValue(PedidoDetalle.COMENTARIO, det.getComentario())
                    .withValue(PedidoDetalle.ESTADO_ART, det.getEstado_articulo())
                    .build());

        }
        try {
            mContext.getApplicationContext().getContentResolver().applyBatch(SmartWaiterContract.CONTENT_AUTHORITY, operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }

    }

    private interface ArticulosQuery {
        String[] PROJECTION = {
                Articulo.ID,
                Articulo.DESCRIPCION_NORM,
                Articulo.UM,
                Articulo.UM_DESC,
                Articulo.PRECIO,
                Articulo.URL
        };
        int ID = 0;
        int DESCRIPCION_NORM = 1;
        int UM = 2;
        int UM_DESC = 3;
        int PRECIO = 4;
        int URL = 5;
    }
}
