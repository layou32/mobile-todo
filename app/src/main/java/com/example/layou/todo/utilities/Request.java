package com.example.layou.todo.utilities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class Request {

    //Post
    InputStream peticionServidor(JSONObject parametros, String servicio) throws IOException {
        Log.d("PeticionServidor","Peticion del servicio " + servicio);

        InputStream iInputStream = null;
        String sParametrosPost = "";

        Iterator<String> oParametros = parametros.keys();
        while (oParametros.hasNext()) {
            String key = oParametros.next();
            String value = "";
            try {
                value = parametros.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sParametrosPost += key + "=" + value + "&";
        }
        // Remueve el ultimo "&" que deja el ciclo while
        sParametrosPost = sParametrosPost.substring(0, sParametrosPost.length() -1);

        try {
            String sUrl = "http://192.168.0.26/b/sif-web-api/" + servicio;

            URL url = new URL(sUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setReadTimeout(5000);
            urlCon.setConnectTimeout(5000);
            urlCon.setRequestMethod("POST");
            urlCon.setDoInput(true);
            urlCon.connect();

            DataOutputStream doutput = new DataOutputStream(urlCon.getOutputStream());

            doutput.writeBytes(sParametrosPost);
            doutput.flush();
            doutput.close();

            int respuesta = urlCon.getResponseCode();

            if(respuesta == HttpURLConnection.HTTP_OK) {
                iInputStream = urlCon.getInputStream();
            }
        } catch (Exception e) {
            Log.e("PeticionServidor", "Error in getdata", e);
        }

        return iInputStream;
    }

    //Get
    InputStream obtenerServidor(String servicio, String parametro) {
        InputStream inSt = null;
        String fullParameter = "http://192.168.0.26/b/sif-web-api/" +
                servicio + "/" + parametro;

        Log.d("obtenerServidor", "Url: " + fullParameter);
        try {
            URL url = new URL(fullParameter);
            HttpURLConnection cc = (HttpURLConnection) url.openConnection();

            //set timeout for reading InputStream
            cc.setReadTimeout(5000);
            // set timeout for connection
            cc.setConnectTimeout(5000);
            //set HTTP method to GET
            cc.setRequestMethod("GET");
            //set it to true as we are connecting for input
            cc.setDoInput(true);

            //reading HTTP response code
            int response = cc.getResponseCode();

            inSt = null;
            //if response code is 200 / OK then read Inputstream
            if (response == HttpURLConnection.HTTP_OK) {
                inSt = cc.getInputStream();
                Log.d("obtenerServidor", "Peticion get exitosa: " + servicio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inSt;
    }

    String convertStreamToString (InputStream stream) {
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder response = new StringBuilder();

        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                response.append(line);
                Log.d("empresasUsuario", "Linea " + line.toString());
            }

        } catch (IOException e) {
            Log.e("ConvertirStreamString", "Error in ConvertStreamToString", e);
        } catch (Exception e) {
            Log.e("ConvertirStreamString", "Error in ConvertStreamToString", e);
        } finally {

            try {
                stream.close();

            } catch (IOException e) {
                Log.e("ConvertirStreamString", "Error in ConvertStreamToString", e);

            } catch (Exception e) {
                Log.e("ConvertirStreamString", "Error in ConvertStreamToString", e);
            }
        }
        Log.d("empresasUsuario", "response " + response.toString());
        return response.toString();
    }

}
