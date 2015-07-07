package br.com.guidogabriel.tesourodiretocompleto;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import br.com.guidogabriel.tesourodiretocompleto.fragments.HistoricoTituloFragment;


public class HistoricoTituloActivity extends ActionBarActivity {
    FragmentManager manager = getSupportFragmentManager();
    HistoricoTituloFragment frag = (HistoricoTituloFragment) manager.findFragmentByTag("historicoTituloFrag");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_titulo);

        //Long idTitulo = getIntent().getExtras().getLong("id");
        //Bundle bundle = new Bundle();
        //bundle.putString("idTitulo", idTitulo+"");//you can use putInt or putBoolean etc
        //Log.i("Tesouro Direto","Dentro do HisoticoTituloActivity o valor de idTiulo e: "+idTitulo);

        if(frag == null){
            frag = new HistoricoTituloFragment();
            //frag.setArguments(bundle);
            android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.rl_fragment_historico, frag, "historicoTituloFrag" );
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historico_titulo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
