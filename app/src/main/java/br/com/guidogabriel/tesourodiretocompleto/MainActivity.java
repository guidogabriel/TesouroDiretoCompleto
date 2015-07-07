package br.com.guidogabriel.tesourodiretocompleto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONException;

import java.util.List;

import br.com.guidogabriel.tesourodiretocompleto.fragments.CotacaoAtualVolleyFragment;


public class MainActivity extends ActionBarActivity implements Response.ErrorListener, Response.Listener<String> {
    //private static String LOG = "Tesouro Direto"; //Vou usar para o logcat quando necessario
    private Toolbar mToolbar;
    private Drawer.Result navigationDrawerLeft;
    FragmentManager manager = getSupportFragmentManager();
    //TituloTesouroFragment frag = (TituloTesouroFragment) manager.findFragmentByTag("mainFrag");
    //CotacaoAtualFragment frag = (CotacaoAtualFragment) manager.findFragmentByTag("mainFrag");
    CotacaoAtualVolleyFragment frag = (CotacaoAtualVolleyFragment) manager.findFragmentByTag("mainFrag");
    TextView tv_status_mercado;
    TextView tv_ultima_atualizacao;
    String statusMercado;
    String ultimaAtualizacao;



    private TextView tvRegistrationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.w("Tesouro Direto","Log de erro",new RuntimeException("Teste de erro"));
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Cotação Atual");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorText));
        setSupportActionBar(mToolbar);

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        String URL = "http://guidogabriel.16mb.com/StatusMercado.php";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL,
                this,
                this
        );
        queue.add(request);


        if (frag == null) {
            Log.i("Tesouro Direto", "Dentrosssss");
            //frag = new TituloTesouroFragment();
            frag = new CotacaoAtualVolleyFragment();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag"); //Substituimos o espaco no xml alocado para o fragment pelo fragmente CarFragment que instanciamos
            ft.commit();
        }


        navigationDrawerLeft = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0) //importante quando estamos trabalhando com GCM e temos que abrir no item correto do drawer
                        //.withAccountHeader(headerNavigationLeft) CABEcALHO?????????????
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() { //Implementando os eventos de click simples
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

                        switch (i) {
                            case 1: {
                                Intent intent = new Intent(MainActivity.this, GraficoActivity.class);
                                startActivity(intent);
                            }
                            break;
                            case 3: {
                                Intent intent = new Intent(MainActivity.this, MonitoramentoActivity.class);
                                startActivity(intent);
                            }
                            break;
                            case 4: {
                                Intent intent = new Intent(MainActivity.this, MonitorActivity.class);
                                startActivity(intent);
                            }
                            break;
                        }


                    }
                })
                .build();
//
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Cotacao Atal").withIcon(getResources().getDrawable(R.drawable.database)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Meus Graficos").withIcon(getResources().getDrawable(R.drawable.chartline)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Meus Relatorios").withIcon(getResources().getDrawable(R.drawable.chartline)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Acompanhar taxas").withIcon(getResources().getDrawable(R.drawable.clock)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Monitor"));


    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e("Tesouro Direto", "Erro ao tentar buscar o status do mercado ou ao usar o gcm");
    }

    @Override
    public void onResponse(String s) {

        try {
            statusMercado = TituloTesouroHttp.jsonParaString(s, "statusmercado");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Tesouro Direto", "Erro na leitura do statusmercado: " + e.getMessage());
        }
        try {
            statusMercado = TituloTesouroHttp.jsonParaString(s, "statusmercado");
            ultimaAtualizacao = "Últ. atualiz.: " + TituloTesouroHttp.jsonParaString(s, "ultimaatualizacao").substring(0, 19);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        tv_status_mercado = (TextView) this.findViewById(R.id.tv_status_mercado);
        tv_ultima_atualizacao = (TextView) this.findViewById(R.id.tv_ultima_atualizacao);

        switch (statusMercado) {
            case "01": {
                tv_status_mercado.setText("Mercado Aberto para Compra");
                tv_ultima_atualizacao.setText(ultimaAtualizacao);
                break;
            }
            case "02": {
                tv_status_mercado.setText("Mercado Aberto para Compra e Venda");
                tv_ultima_atualizacao.setText(ultimaAtualizacao);
                break;
            }
            case "03": {
                tv_status_mercado.setText("Mercado Fechado");
                break;
            }
            case "04": {
                tv_status_mercado.setText("Mercado Suspenso");
                break;
            }
            case "05": {
                tv_status_mercado.setText("Não foi possível obter Status do mercado");
                Toast.makeText(this, "Erro ao obter status do mercado. Equipe de suporte já está corrigindo o problema", Toast.LENGTH_LONG).show();
                break;
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if (navigationDrawerLeft.isDrawerOpen()) {
            navigationDrawerLeft.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public List<TituloTesouro> gerarTitulosTesouro(int qtdTitulos) {
        String[] nome = new String[]{"Tesouro IPCA+ com Juros Semestrais 2015 (NTNB)", "Tesouro IPCA+ 2015 (NTNB Princ)", "Tesouro IPCA+ com Juros Semestrais 2017 (NTNB)", "Tesouro IPCA+ 2019 (NTNB Princ)", "Tesouro IPCA+ com Juros Semestrais 2020 (NTNB)", "Tesouro IPCA+ 2024 (NTNB Princ)", "Tesouro IPCA+ com Juros Semestrais 2024 (NTNB)", "Tesouro IPCA+ com Juros Semestrais 2035 (NTNB)", "Tesouro IPCA+ 2035 (NTNB Princ)", "Tesouro IPCA+ com Juros Semestrais 2045 (NTNB)"};
        String[] taxaCompra = new String[]{"Taxa compra: -", "Taxa compra: -", "Taxa compra: -", "Taxa compra: 6,67%", "Taxa compra: 6,50%", "Taxa compra: 6,28%", "Taxa compra: -", "Taxa compra: 6,16%", "Taxa compra: 6,10%", "Taxa compra: -"};
        String[] precoCompra = new String[]{"Pre�o compra: -", "Pre�o compra: -", "Pre�o compra: -", "Pre�o compra: R$ 2.024,83", "Pre�o compra: R$ 2.601,66", "Pre�o compra: R$ 1.493,15", "Pre�o compra: -", "Pre�o compra: R$ 2.654,76", "Pre�o compra: R$ 804,44", "Pre�o compra: -"};
        //List<TituloTesouro> listAux = new ArrayList<>();


        for (int i = 0; i < qtdTitulos; i++) {
            //TituloTesouro tituloTesouro =
            //new TituloTesouro(nome[i % nome.length], precoCompra[i % precoCompra.length], taxaCompra[i % taxaCompra.length]).save();
            //listAux.add(tituloTesouro);
        }
        List<TituloTesouro> listaTitulos = TituloTesouro.listAll(TituloTesouro.class);
        return listaTitulos;
        //return (listAux);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //    checkPlayServices();
    }


}
