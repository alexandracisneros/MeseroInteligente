package com.idealsolution.smartwaiter.io;

import android.content.Context;
import android.content.SharedPreferences;
import org.apache.http.NameValuePair;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Usuario on 21/12/2014.
 */
public class RestUtil {
    public static final String URLServer="http://siempresoftqa.cloudapp.net/PruebaMovilAlex/api/restaurante/";
    //http://siempresoftqa.cloudapp.net/PruebaMovilAlex/api/restaurante/ObtenerDatosIniciales/?codCia=001&cadenaConexion=Initial%20Catalog=ABR
//    public static final String obtainURLServer(Context ctxt) {
//        SharedPreferences prefConextion;
//
//        prefConextion = ctxt.getSharedPreferences(PREF_Conexion.NAME, 0);
//        String server = prefConextion.getString(PREF_Conexion.SERVIDOR, "");
//        String aplicacion = prefConextion.getString(PREF_Conexion.APLICACION,"");
//
//        String url = "http://" + server + "/" + aplicacion + "/api/";
//        return url;
//    }
//
//    public static final String obtainUrlRoot(Context ctxt){
//        SharedPreferences prefConextion;
//
//        prefConextion = ctxt.getSharedPreferences(PREF_Conexion.NAME, 0);
//        String server = prefConextion.getString(PREF_Conexion.SERVIDOR, "");
//
//        String url = "http://" + server ;
//        return url;
//    }
//
//    public static final Boolean datosConexionCompletos(Context ctxt){
//        SharedPreferences prefConextion;
//        prefConextion = ctxt.getSharedPreferences(PREF_Conexion.NAME, 0);
//
//        Boolean datosConexCompletos=prefConextion.getBoolean(PREF_Conexion.DATOS_COMPLETOS, false);
//        return datosConexCompletos;
//    }

    public static final RestConnector obtainGetConnection(String url)
            throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url)
                .openConnection());

        connection.setReadTimeout(60000);
        connection.setConnectTimeout(150000);
        connection.setDoInput(true);

        RestConnector connector = new RestConnector(connection);
        return connector;

    }

    public static final RestConnector obtainFormPostConnection(String url,
                                                        List<NameValuePair> formData) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(url))
                .openConnection();

        connection.setReadTimeout(60000);
        connection.setConnectTimeout(150000);
        connection.setDoOutput(true);

        RestConnector connector = new RestConnector(connection);
        connector.setFormBody(formData);

        return connector;
    }

}
