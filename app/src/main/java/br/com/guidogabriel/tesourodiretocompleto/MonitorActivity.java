package br.com.guidogabriel.tesourodiretocompleto;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import br.com.guidogabriel.tesourodiretocompleto.fragments.MonitorFragment;


/**
 * Created by guido_000 on 17/06/2015.
 */
public class MonitorActivity extends AppCompatActivity {
    FragmentManager manager = getSupportFragmentManager();
    MonitorFragment frag = (MonitorFragment) manager.findFragmentByTag("monitorFrag");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);


        if (frag == null) {
            frag = new MonitorFragment();
            //frag.setArguments(bundle);
            android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.rl_fragment_monitor, frag, "monitoramentoFrag");
            ft.commit();
        }

    }
}
