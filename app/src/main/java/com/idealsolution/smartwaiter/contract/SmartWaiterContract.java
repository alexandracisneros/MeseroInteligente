package com.idealsolution.smartwaiter.contract;

import android.net.Uri;

public class SmartWaiterContract {
    /**
     * Query parameter to create a distinct query.
     */
    public static final String QUERY_PARAMETER_DISTINCT = "distinct";

    public static final String TAG="SmartWaiter";
    interface PedidoCabeceraColumns{
         String ID  = "_id";
         int ID_COL = 0;

         String FECHA  = "fecha";
         int FECHA_COL = 1;

         String NRO_MESA  = "nro_mesa";
         int NRO_MESA_COL = 2;

         String AMBIENTE  = "ambiente";
         int AMBIENTE_COL = 3;

         String CODIGO_USUARIO  = "codigo_usuario";
         int CODIGO_USUARIO_COL = 4;

         String CODIGO_CLIENTE  = "codigo_cliente";
         int CODIGO_COL = 5;

         String TIPO_VENTA  = "tipo_venta";
         int TIPO_VENTA_COL = 6;

         String TIPO_PAGO  = "tipo_pago";
         int TIPO_PAGO_COL = 7;

        String MONEDA="moneda";
        int MONEDA_COL=8;

         String MONTO_TOTAL  = "monto_total";
         int MONTO_TOTAL_COL = 9;

         String MONTO_RECIBIDO  = "moneda_recibido";
         int MONTO_RECIBIDO_COL = 10;

         String ESTADO  = "estado";
         int ESTADO_COL = 11;
    }
    interface  PedidoDetalleColumns{
         String ID  = "_id";
         int ID_COL = 0;

         String PEDIDO_ID  = "pedido_id";
         int PEDIDO_ID_COL = 1;

         String COD_ART  = "cod_articulo";
         int COD_ART_COL = 2;

         String CANTIDAD  = "cantidad";
         int CANTIDAD_COL = 3;

         String PRECIO    = "precio";
         int PRECIO_COL   = 4;

         String TIPO_ART  ="tipo_articulo";
         int TIPO_ART_COL   = 5;

         String COD_ART_PRINCIPAL    ="cod_art_principal";
         int COD_ART_PRINCIPAL_COL   = 6;

         String COMENTARIO    ="comentario";
         int COMENTARIO_COL   = 7;

         String ESTADO_ART    ="estado_articulo";
         int ESTADO_ART_COL   = 8;
    }
    interface FamiliaColumns{
         String ID  = "_id";
         int ID_COL = 0;

         String CODIGO  = "codigo";
         int CODIGO_COL = 1;

         String DESCRIPCION  = "descripcion";
         int DESCRIPCION_COL = 2;

         String URL="url";
         int URL_COL=3;
    }
    interface PrioridadColumns{
         String ID  = "_id";
         int ID_COL = 0;

         String CODIGO  = "codigo";
         int CODIGO_COL = 1;

         String DESCRIPCION  = "descripcion";
         int DESCRIPCION_COL = 2;
    }
    interface ClienteColumns{
         String ID  = "_id";
         int ID_COL = 0;

         String RAZON_SOCIAL  = "razon_social";
         int RAZON_SOCIAL_COL = 1;

         String RAZON_SOCIAL_NORM  = "razon_social_norm";
         int RAZON_SOCIAL_NORM_COL = 2;

         String TIPO_PERSONA  = "tipo_persona";
         int TIPO_PERSONA_COL = 3;

         String NRO_DOCUMENTO  = "nro_documento";
         int NRO_DOCUMENTO_COL = 4;

         String DIRECCION  = "direccion";
         int DIRECCION_COL = 5;
    }
    interface MesaPisoColumns{
        String ID  = "_id";
        int ID_COL = 0;

        String NRO_PISO  = "nro_piso";
        int NRO_PISO_COL = 1;

        String COD_AMBIENTE  = "cod_ambiente";
        int COD_AMBIENTE_COL = 2;

        String DESC_AMBIENTE  = "desc_ambiente";
        int DESC_AMBIENTE_COL = 3;

        String NRO_MESA  = "nro_mesa";
        int NRO_MESA_COL = 4;

        String NRO_ASIENTOS  = "nro_asientos";
        int NRO_ASIENTOS_COL = 5;

        String COD_ESTADO_MESA  = "cod_estado_mesa";
        int COD_ESTADO_MESA_COL = 6;

        String DESC_ESTADO_MESA  = "desc_estado_mesa";
        int DESC_ESTADO_MESA_COL = 7;

        String COD_RESERVA  = "cod_reserva";
        int COD_RESERVA_COL = 8;
    }
    interface CartaColumns{
         String ID  = "_id";
         int ID_COL = 0;

         String COD_FAMILIA  = "cod_familia";
         int COD_FAMILIA_COL = 1;

         String COD_PRIORIDAD  = "cod_prioridad";
         int COD_PRIORIDAD_COL = 2;

         String COD_ARTICULO  = "cod_articulo";
         int COD_ARTICULO_COL = 3;

         String COD_ARTICULO_PRINC  = "cod_articulo_princ";
         int COD_ARTICULO_PRINC_COL = 4;
    }
    interface ArticuloColumns{
         String ID  = "_id";
         int ID_COL = 0;

         String DESCRIPCION  = "descripcion";
         int DESCRIPCION_COL = 1;

         String DESCRIPCION_NORM  = "descripcion_norm";
         int DESCRIPCION_NORM_COL = 2;

         String UM  = "um";
         int UM_COL = 3;

         String UM_DESC  = "um_desc";
         int UM_DESC_COL = 4;

         String PRECIO  = "um_precio";
         int PRECIO_COL = 5;

        String COD_LISTAPRECIO="cod_lista_precio";
        int COD_LISTAPRECIO_COL=6;

        String URL="url";
        int URL_COL=7;
    }

    public static final String CONTENT_AUTHORITY = "com.idealsolution.smartwaiter.provider";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    private static final String PATH_PEDIDO_CABECERAS = "pedido_cabeceras";
    private static final String PATH_PEDIDO_DETALLES = "pedido_detalles";
    private static final String PATH_FAMILIAS = "familias";
    private static final String PATH_PRIORIDADES = "prioridades";
    private static final String PATH_CLIENTES = "clientes";
    private static final String PATH_MESA_PISOS = "mesa_pisos";
    private static final String PATH_AMBIENTES = "ambientes";
    private static final String PATH_PISOS = "pisos";
    private static final String PATH_CARTAS = "cartas";
    private static final String PATH_ARTICULOS = "articulos";
    private static final String PATH_ARTICULOS_FAMILIA = "familia";


    public static class PedidoCabecera implements PedidoCabeceraColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PEDIDO_CABECERAS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.idealsolution.pedido_cabecera";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.idealsolution.pedido_cabecera";
        /** Build {@link Uri} for requested {link PedidoCabecera.ID}. */
        public static Uri buildPedidoCabeceraUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }
    public static class PedidoDetalle implements PedidoDetalleColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PEDIDO_DETALLES).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.idealsolution.pedido_detalle";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.idealsolution.pedido_detalle";
        /** Build {@link Uri} for requested {link PedidoDetalle.ID}. */
        public static Uri buildPedidoDetalleUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

    }
    public static class Familia implements FamiliaColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAMILIAS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.idealsolution.familia";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.idealsolution.familia";

        /** Read {@link #ID} from {@link Familia} {@link Uri}. */
        public static String getFamiliaId(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }
    public static class Prioridad implements PrioridadColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRIORIDADES).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.idealsolution.prioridad";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.idealsolution.prioridad";

    }
    public static class Cliente implements ClienteColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLIENTES).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.idealsolution.cliente";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.idealsolution.cliente";
    }
    public static class MesaPiso implements MesaPisoColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESA_PISOS).build();
        public static final Uri CONTENT_AMBIENTE_URI =
                CONTENT_URI.buildUpon().appendPath(PATH_AMBIENTES)
                        .appendQueryParameter(SmartWaiterContract.QUERY_PARAMETER_DISTINCT,"true") // SELECT with DISTINCT
                        .build();
        public static final Uri CONTENT_PISO_URI =
                CONTENT_URI.buildUpon().appendPath(PATH_PISOS)
                        .appendQueryParameter(SmartWaiterContract.QUERY_PARAMETER_DISTINCT,"true") // SELECT with DISTINCT
                        .build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.idealsolution.mesa_piso";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.idealsolution.mesa_piso";

        // Used to fetch ambientes by a specific piso
        public static final String AMBIENTES_POR_PISO_SELECTION =
                MesaPiso.NRO_PISO + "= ? ";
        // Used to fetch mesas by a specific piso and ambiente
        public static final String MESAS_POR_PISO_AMBIENTE_SELECTION=
                MesaPiso.NRO_PISO + "=? and " + MesaPiso.COD_AMBIENTE + "=? ";
    }
    public static class Carta implements CartaColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARTAS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.idealsolution.carta";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.idealsolution.carta";
    }
    public static class Articulo implements ArticuloColumns{
        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICULOS).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.idealsolution.articulo";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.idealsolution.articulo";
        /**
         * Build {@link Uri} that references any {@link Articulo} associated
         * with the requested {@link #ID}. ID is equals to Familia.ID
         */
        public static Uri buildArticuloFamiliaUri(int familiaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_ARTICULOS_FAMILIA)
                    .appendPath(String.valueOf(familiaId))
                    .appendQueryParameter(SmartWaiterContract.QUERY_PARAMETER_DISTINCT,"true") // SELECT with DISTINCT
                    .build();
        }

    }
}
