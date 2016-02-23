package com.cloudsolution.cloudaps;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mamun on 2/23/16.
 */
public class JsonParsing {

    private static final String JSON_URL = "http://developer.android.com/reference/org/json/JSONObject.html";
    private static final String JSON_ARRAY_TAG = "apps";
    private static final String APP_NAME_TAG = "appName";
    private static final String APP_URL_TAG = "appLink";

    private Context context;
    private static String[][] cslAPP;

    public JsonParsing(Context context) {
        this.context = context;
    }

    public String[][] getCslApps(){
        sendRequest();
        return cslAPP;
    }

    private void sendRequest(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JsonObject", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Objectrequesterror", error.toString());
                    }
                });

        StringRequest stringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ServerResponse", response);
                        parseJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ServerError",error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        requestQueue.add(jsonObjectRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jo = new JSONObject(response);
            JSONArray ja = jo.getJSONArray(JSON_ARRAY_TAG);
            cslAPP = new String[ja.length()][2];
            for (int i = 0; i<ja.length(); i++){
                JSONObject jsonObject = ja.getJSONObject(i);
                cslAPP[i][0] = jsonObject.getString(APP_NAME_TAG);
                cslAPP[i][1] = jsonObject.getString(APP_URL_TAG);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
