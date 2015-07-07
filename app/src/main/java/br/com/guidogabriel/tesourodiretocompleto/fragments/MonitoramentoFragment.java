package br.com.guidogabriel.tesourodiretocompleto.fragments;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.lang.reflect.Method;

import br.com.guidogabriel.tesourodiretocompleto.MainActivity;
import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.VolleySingleton;
import br.com.guidogabriel.tesourodiretocompleto.util.AndroidSystemUtil;


public class MonitoramentoFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {
    EditText ed_menor_que;
    EditText ed_maior_que;
    TextView tv_titulo;
    private String regId;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000; //Codigo que o google da pra fazer a chamda de instalacao do play services
    private String SENDER_ID = "337000216816"; //Id do projeto obtido la no console do google developers
    private GoogleCloudMessaging gcm;
    public static final String TAG = "Tesouro Direto";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.w(TAG, "Entrei no onCreated()");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ed_menor_que = (EditText) getActivity().findViewById(R.id.et_menor_que);
        ed_maior_que = (EditText) getActivity().findViewById(R.id.et_maior_que);
        tv_titulo = (TextView) getActivity().findViewById(R.id.tv_titulo);

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(getActivity());
            regId = AndroidSystemUtil.getRegistrationId(getActivity());

            if (regId.trim().length() == 0) {
                registerIdInBackground();
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_monitoramento, null);
        return layout;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
    @Override
    public void onResponse(String s) {

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }



    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()); //Verifica se o play service esta disponivel

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                Toast.makeText(getActivity(), "PlayServices sem suporte", Toast.LENGTH_SHORT).show();
                //TODO deve fechar o fragment

            }
            return (false);
        }
        return (true);
    }
    public void registerIdInBackground() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getActivity());
                    }
                    Log.e(TAG, "-----------------------------------------------------------Antes do register()");
                    regId = gcm.register(SENDER_ID);

                    Log.e(TAG, "-----------------------------------------------------------depois do register(). regId: "+regId);

                    msg = "Register Id: " + regId;

                    chamarRequestCrtl();
                    //String feedback = HttpConnectionUtil.sendRegistrationIdToBackend(regId);
                    //Log.i(TAG, "Esse eh o feedback --> "+feedback);

                    AndroidSystemUtil.storeRegistrationId(getActivity(), regId);
                } catch (IOException e) {
                    Log.i(TAG, e.getMessage());
                }

                return msg;
            }

            @Override
            public void onPostExecute(Object msg) {
                Log.e(TAG, "Antes do onpostexcute");
                if (tv_titulo == null)
                    Log.w(TAG, "tv_titulo eh igual a null");
                //tv_titulo.setText((String) msg);
                Log.e(TAG, "Depois do onpostexcute");
            }

        }.execute(null, null, null);
    }
/*
    public void registerIdInBackground() {
        new AsyncTask() {
            String msg = "";
            if(gcm==null)

            {
                gcm = GoogleCloudMessaging.getInstance(getActivity());
            }
            //Log.e("Tesouro Direto", "-----------------------------------------------------------Cheguei aqui no 1: ");
            try

            {

                regId = gcm.register(SENDER_ID);

            }

            catch(
            IOException e
            )

            {
                Log.i("Tesouro Direto", "Erro no register: " + e.getMessage());
                e.printStackTrace();
            }

            msg="Register Id: "+regId;


            chamarRequestCrtl();

            //queue.add(request);
            AndroidSystemUtil.storeRegistrationId(

            getActivity(),regId

            );

            tv_titulo.setText((String)msg);

        }
    }
*/
    public void chamarRequestCrtl(){
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        String url = "http://guidogabriel.16mb.com/ctrl/CtrlGcm.php?method=save-gcm-registration-id&reg-id=" + regId;
        StringRequest request = new StringRequest(Request.Method.GET,url,this, this);
        queue.add(request);
    }


}
