package br.com.guidogabriel.tesourodiretocompleto;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import br.com.guidogabriel.tesourodiretocompleto.fragments.TituloTesouroFragment;


public class MainActivity extends ActionBarActivity {
    private static String LOG = "Tesouro Direto"; //Vou usar para o logcat quando necessario
    private Toolbar mToolbar;
    private Drawer.Result navigationDrawerLeft;
    FragmentManager manager = getSupportFragmentManager();
    TituloTesouroFragment frag = (TituloTesouroFragment) manager.findFragmentByTag("mainFrag");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Titulos Publicos");
        setSupportActionBar(mToolbar);

        if (frag == null) {

            frag = new TituloTesouroFragment();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag"); //Substituimos o espaco no xml alocado para o fragment pelo fragmente CarFragment que instanciamos
            ft.commit();
        }


        navigationDrawerLeft = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSelectedItem(0)
                        //.withAccountHeader(headerNavigationLeft) CABEcALHO?????????????
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() { //Implementando os eventos de click simples
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

                    }
                })
                .build();
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Principal"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Graficos"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Relatorios"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Acompanhar taxas"));
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
       //  String[] nome = new String[]{"Tesouro IPCA+ com Juros Semestrais 2015 (NTNB)", "Tesouro IPCA+ 2015 (NTNB Princ)", "Tesouro IPCA+ com Juros Semestrais 2017 (NTNB)", "Tesouro IPCA+ 2019 (NTNB Princ)", "Tesouro IPCA+ com Juros Semestrais 2020 (NTNB)", "Tesouro IPCA+ 2024 (NTNB Princ)", "Tesouro IPCA+ com Juros Semestrais 2024 (NTNB)", "Tesouro IPCA+ com Juros Semestrais 2035 (NTNB)", "Tesouro IPCA+ 2035 (NTNB Princ)", "Tesouro IPCA+ com Juros Semestrais 2045 (NTNB)"};
       //  String[] taxaCompra = new String[]{"Taxa compra: -", "Taxa compra: -", "Taxa compra: -", "Taxa compra: 6,67%", "Taxa compra: 6,50%", "Taxa compra: 6,28%", "Taxa compra: -", "Taxa compra: 6,16%", "Taxa compra: 6,10%", "Taxa compra: -"};
       // String[] precoCompra = new String[]{"Preço compra: -", "Preço compra: -", "Preço compra: -", "Preço compra: R$ 2.024,83", "Preço compra: R$ 2.601,66", "Preço compra: R$ 1.493,15", "Preço compra: -", "Preço compra: R$ 2.654,76", "Preço compra: R$ 804,44", "Preço compra: -"};
       // List<TituloTesouro> listAux = new ArrayList<>();



         //       for (int i = 0; i < qtdTitulos; i++) {
         //   new TituloTesouro(nome[i % nome.length], precoCompra[i % precoCompra.length], taxaCompra[i % taxaCompra.length]).save();
            //listAux.add(tituloTesouro);
       // }
        List<TituloTesouro> listaTitulos = TituloTesouro.listAll(TituloTesouro.class);
        return listaTitulos;
        //return (listAux);
    }
}
