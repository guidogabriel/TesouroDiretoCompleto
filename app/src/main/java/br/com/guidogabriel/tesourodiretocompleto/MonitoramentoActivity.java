package br.com.guidogabriel.tesourodiretocompleto;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import br.com.guidogabriel.tesourodiretocompleto.fragments.MonitoramentoFragment;

/**
 * Created by guido_000 on 02/06/2015.
 */
public class MonitoramentoActivity extends AppCompatActivity {
    FragmentManager manager = getSupportFragmentManager();
    MonitoramentoFragment frag = (MonitoramentoFragment) manager.findFragmentByTag("monitoramentoFrag");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoramento);

        if (frag == null) {
            frag = new MonitoramentoFragment();
            //frag.setArguments(bundle);
            android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.rl_fragment_monitoramento, frag, "monitoramentoFrag");
            ft.commit();
        }

    }

    public void finishFragment(){
        if (frag != null){
            android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
            ft.remove(frag);
            ft.commit();

        }
    }
}