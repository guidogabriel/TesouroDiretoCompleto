package br.com.guidogabriel.tesourodiretocompleto.fragments;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.guidogabriel.tesourodiretocompleto.Monitor;
import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouro;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouroHttp;
import br.com.guidogabriel.tesourodiretocompleto.VolleySingleton;
import br.com.guidogabriel.tesourodiretocompleto.adapter.MonitorAdapter;
import br.com.guidogabriel.tesourodiretocompleto.util.AndroidSystemUtil;

public class MonitorFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener, View.OnClickListener {
    public static final String TAG = "Tesouro Direto";
    private RecyclerView mRecyclerView;
    private Button bt_salvar;
    private List<TituloTesouro> mListTituloTesouro;
    private List<Monitor> mListMonitor;
    private List<Monitor> listaMonitor;
    MonitorAdapter adapter;
    private GoogleCloudMessaging gcm;
    private String regId;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000; //Codigo que o google da pra fazer a chamda de instalacao do play services
    private String SENDER_ID = "337000216816"; //Id do projeto obtido la no console do google developers

    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(getActivity());
            regId = AndroidSystemUtil.getRegistrationId(getActivity());
            //Log.i(TAG, "Momento 1. Esse eh o regId: " + regId + ".");

            if (regId.trim().length() == 0) {
                registerIdInBackground();
            }
        }

        //bt_salvar.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View layout = inflater.inflate(R.layout.fragment_monitor, null);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        String urlTitulos = "http://guidogabriel.16mb.com/Controller/TituloTesouroController.php";
        solicitarTitulosNuvem(urlTitulos);
        String urlMonitor = "http://guidogabriel.16mb.com/Controller/MonitorController.php";
        solicitarMonitorNuvem(urlMonitor);

        bt_salvar = (Button) getActivity().findViewById(R.id.bt_salvar);
        bt_salvar.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        mListMonitor = new ArrayList<>();
        mListTituloTesouro = new ArrayList<>();
        //if (mList == null) {}


    }

    public void solicitarTitulosNuvem(String url) {
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        StringRequest requestTitulos = new StringRequest(
                Request.Method.POST,
                url, //LivroHttp.LIVROS_URL_JSON,  // url da requisocao
                this,
                this
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("metodo", "busca-todos");
                //params.put("email","email@email.com");
                //params.put("registrationid","12345666232");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(requestTitulos);
    }

    public void solicitarMonitorNuvem(String url) {
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url, //LivroHttp.LIVROS_URL_JSON,  // url da requisocao
                this,
                this
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("metodo", "busca-por-registration-id");
                params.put("registration-id", regId);
                Log.i(TAG,"registration id = "+regId);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public void onResponse(String s) {
        //Log.i(TAG, "onResponse");
        //Log.i(TAG, "Essa eh a resposta da nuvem: " + s);
        JSONObject json = null;
        try {
            json = new JSONObject(s);

            String[] partes = s.split("\"");
            switch (partes[1]) {
                case "monitor":
                    mListMonitor.addAll(TituloTesouroHttp.lerJsonMonitor(json));
                    break;
                case "titulostesouro":
                    mListTituloTesouro.addAll(TituloTesouroHttp.lerJsonTitulosTesouro(json));
            }

            long idRemoto = 0;
            listaMonitor = new ArrayList<>();

            if (mListMonitor.size() > 0 && mListTituloTesouro.size() > 0) {
                for (TituloTesouro itemTituloTesouro : mListTituloTesouro) {

                    for (Monitor itemMonitor : mListMonitor) {

                        if (itemMonitor.getIdTituloRemoto() == itemTituloTesouro.getIdRemoto()) {
                            Monitor monitorTaxas = new Monitor();
                            monitorTaxas.setIdMonitorRemoto(itemMonitor.getIdMonitorRemoto());
                            monitorTaxas.setIdTituloRemoto(itemMonitor.getIdTituloRemoto());
                            monitorTaxas.setNomeTitulo(itemTituloTesouro.getNome());
                            monitorTaxas.setIdUsuario(itemMonitor.getIdUsuario());
                            monitorTaxas.setTaxaCompraMenorQue(itemMonitor.getTaxaCompraMenorQue());
                            monitorTaxas.setTaxaCompraMaiorQue(itemMonitor.getTaxaCompraMaiorQue());
                            listaMonitor.add(monitorTaxas);
                            idRemoto = itemMonitor.getIdTituloRemoto();

                        } else { //Percorrendo a listagem de monitor. Se itTituloRemoto do titulo for diferente do idtitulo do monitor

                        }

                    }
                    if (itemTituloTesouro.getIdRemoto() != idRemoto) {
                        Monitor monitorSemTaxas = new Monitor();
                        monitorSemTaxas.setIdTituloRemoto(itemTituloTesouro.getIdRemoto());
                        monitorSemTaxas.setNomeTitulo(itemTituloTesouro.getNome());
                        monitorSemTaxas.setIdMonitorRemoto(null);
                        monitorSemTaxas.setIdUsuario(null);
                        monitorSemTaxas.setTaxaCompraMenorQue(null);
                        monitorSemTaxas.setTaxaCompraMaiorQue(null);
                        listaMonitor.add(monitorSemTaxas);
                        //cont++;
                        //Log.i(TAG, "Imprimindo a listaMonitor...");
                        //for (Monitor item : listaMonitor){
                        //  Log.i(TAG, "Valor de idTituloRemoto: " + item.getIdTituloRemoto() + ". Nome do titulo: " + item.getNomeTitulo() + ". Tamanho da lista: "+listaMonitor.size());
                        //}
                    }
                }
                //Log.i(TAG, "O valor de cont eh: " + cont);
                mListMonitor.clear();
                mListMonitor.addAll(listaMonitor);
                //adapter.notifyDataSetChanged();

                adapter = new MonitorAdapter(getActivity(), mListMonitor);
                //adapter.setRecyclerViewOnClickListenerHack(this);
                mRecyclerView.setAdapter(adapter);
            }

            Collections.sort(listaMonitor, new Comparator<Monitor>() {
                @Override
                public int compare(Monitor lhs, Monitor rhs) {
                    Monitor m1 = lhs;
                    Monitor m2 = rhs;
                    return m1.getIdTituloRemoto() < m2.getIdTituloRemoto() ? -1 : (m1.getIdTituloRemoto() > m2.getIdTituloRemoto() ? +1 : 0);
                }
            });


            //  adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.w("Tesouro Direto", e.getMessage() + " --> Erro no metodo onResponse do Volley");
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        //Log.i(TAG, "onErrorResponse");
        Log.w("Tesouro Direto", volleyError.getMessage() + " Erro no OnErrorResponse do MonitorFragment.java");
        volleyError.printStackTrace();

    }

    public boolean checkPlayServices() {
        Log.i(TAG, "Entrei no checkPlayServices");
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()); //Verifica se o play service esta disponivel
        Log.i(TAG, "Valor de resultCode: " + resultCode);
        Log.i(TAG, "Valor de SUCCESS: " + ConnectionResult.SUCCESS);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i(TAG, "Abrir Dialogo de erro");
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                Toast.makeText(getActivity(), "PlayServices sem suporte", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "PlayServices Sem Suporte");
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

                    regId = gcm.register(SENDER_ID);

                    msg = "Register Id: " + regId;

                    registrarNaNuvem();
                    //String feedback = HttpConnectionUtil.sendRegistrationIdToBackend(regId);
                    //Log.i(TAG, "Esse eh o feedback eh o registerId --> " + msg);

                    AndroidSystemUtil.storeRegistrationId(getActivity(), regId);
                } catch (IOException e) {
                    Log.i(TAG, e.getMessage());
                }

                return msg;
            }

            @Override
            public void onPostExecute(Object msg) {
                Log.e(TAG, "onPostExecute");
                //if (tv_titulo == null)
                //  Log.w(TAG, "tv_titulo eh igual a null");
                //tv_titulo.setText((String) msg);
                //Log.e(TAG, "Depois do onpostexcute");
            }

        }.execute(null, null, null);
    }

    public void registrarNaNuvem() {
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        String url = "http://guidogabriel.16mb.com/Controller/MonitorController.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i(TAG, "Resposta do registrarNaNuvem: " + s);
                        if (s.trim().equals("1")) {
                            Log.i(TAG, "Registration Id foi salvo com sucesso no servidor da nuvem");

                        } else {
                            Log.i(TAG, "FALHA AO SALVAR REGISTRATION ID NA NUVEM. Resultado foi difernete de 1");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i(TAG, "ERRO: Caiu no catch do registrarNaNuvem()");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("metodo", "salvar-registration-id");
                params.put("id-usuario", "2");
                params.put("registration-id", regId);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public void onClick(View v) {
        List<Monitor> lista = new ArrayList<Monitor>();

        EditText x = (EditText) getActivity().getCurrentFocus();
        x.clearFocus(); //Chamado antes de carregar a lista getAll(), porque eh preciso mudar o foco do edittext pra atualizar na listagem no adaprter
        lista.addAll(adapter.getAll());

        final JSONObject jsonMonitor = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            for (Monitor item : lista) {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("idtitulotesouro", item.getIdTituloRemoto().toString());
                jsonItem.put("idmonitor", item.getIdMonitorRemoto() == null ? "null" : item.getIdMonitorRemoto());
                jsonItem.put("taxacompra_menorque", item.getTaxaCompraMenorQue() == null ? null : item.getTaxaCompraMenorQue());
                jsonItem.put("taxacompra_maiorque", item.getTaxaCompraMaiorQue() == null ? null : item.getTaxaCompraMaiorQue());
                array.put(jsonItem);
            }
            jsonMonitor.put("monitor", array);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "Problema ao tentar criar o jsonMonitor");
        }
       //Log.i(TAG, jsonMonitor.toString());
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        String url = "http://guidogabriel.16mb.com/Controller/MonitorController.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i(TAG, "Resposta foi: " + s);
                        try {
                            String result = TituloTesouroHttp.jsonParaString(s, "resposta");
                            //Log.i(TAG,result+" foram salvos/atualizados");
                            Toast.makeText(getActivity(),result+" registros foram salvos/atualizados", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "Erro ao ler json do volley para salvar os monitores");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i(TAG, "Deu algum errinho boboi no volley do salvar-monitor: " + volleyError.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("metodo", "salvar-monitor");
                params.put("registration-id", regId);
                params.put("json-monitor", jsonMonitor.toString());
                Log.i(TAG,"json que esta sendo enviado: "+jsonMonitor.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };


        queue.add(request);
    }

}

