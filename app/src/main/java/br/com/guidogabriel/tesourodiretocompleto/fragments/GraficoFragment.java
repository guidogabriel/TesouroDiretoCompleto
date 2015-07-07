package br.com.guidogabriel.tesourodiretocompleto.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.guidogabriel.tesourodiretocompleto.R;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouro;
import br.com.guidogabriel.tesourodiretocompleto.TituloTesouroHttp;
import br.com.guidogabriel.tesourodiretocompleto.VolleySingleton;

public class GraficoFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {
    private LineChart chart;
    private List<TituloTesouro> mlist;
    Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("Tesouro Direto", "Entrei no: onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("Tesouro Direto", "Entrei no: onCreateView");
        View layout = inflater.inflate(R.layout.fragment_grafico, null);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i("Tesouro Direto", "Entrei no: onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        chart = (LineChart) getActivity().findViewById(R.id.chart);

        //chart.setBackgroundColor(Color.CYAN);
        chart.setDescription("Esta eh a descricao of my fucking chart");
        chart.setNoDataTextDescription("Nenhum dado para o momento");
        chart.setDescriptionColor(Color.BLACK);
        chart.setDescriptionPosition(1000f, 1000f);
        chart.setDescriptionTypeface(Typeface.SANS_SERIF);
        chart.setDescriptionTextSize(15f);

        //Setando value highligthing
        chart.setHighlightEnabled(true);
        //Habilitando touch gesture
        chart.setTouchEnabled(true);
        //hablitando scaling (eixo x e y) e dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        //habilitar pinch zoom para evitar scaling x e y separadamente
        chart.setPinchZoom(true);

        chart.setBackgroundColor(Color.LTGRAY);

        chart.setLogEnabled(true);
/*        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaxValue(2f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("Tesouro Direto", "Entrei no: onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        int idTitulo = 10;
        int inicio = 0;
        int tamanho = 19;

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        String url = "http://guidogabriel.16mb.com/HitoricoTituloRefatoradoJson.php";
        url += "?idtitulo=" + idTitulo + "&inicio=" + inicio + "&tamanho=" + tamanho;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url, //LivroHttp.LIVROS_URL_JSON,  // url da requisocao
                this,
                this
        );
        queue.add(request);
    }

    public void preencherDados() {
        Log.i("Tesouro Direto", "Entrei no: preencherDados");
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        int cont = 0;
        calendar = Calendar.getInstance();
        int dia;
        Date dataTitulo;
        for (TituloTesouro item : mlist) {
            //DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dataTitulo = df.parse(item.getDataUltimaAtualizacao());
                GregorianCalendar objCalendario = new GregorianCalendar();
                objCalendario.setTime(dataTitulo);
                dia = objCalendario.get(Calendar.DAY_OF_MONTH);
                //Log.i("Tesouro Direto", "Vamos ver o formato da date " + df.parse(item.getDataUltimaAtualizacao()));
                Log.i("Tesouro Direto", "Vamos ver somente o dia " + dia);
                xVals.add("" + dia);
            } catch (ParseException e) {
                Log.i("Tesouro Direto", "Erro no format.parse()");
            }

            Entry c1e1 = new Entry(Float.parseFloat(item.getPrecoVenda()), cont);
            Entry c1e2 = new Entry(Float.parseFloat(item.getPrecoCompra()), cont);
            valsComp1.add(c1e1);
            valsComp2.add(c1e2);
            cont++;
        }

        for (String item : xVals) {
            Log.i("Tesouro Direto", "Valor de xVals: " + item.toString());
        }
        for (Entry item : valsComp1) {
            Log.i("Tesouro Direto", "valsComp1: " + item.toString());
        }

/*
        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        valsComp1.add(c1e1);
        Entry c1e2 = new Entry(50.000f, 1); // 1 == quarter 2 ...
        valsComp1.add(c1e2);

        Entry c2e1 = new Entry(120.000f, 0); // 0 == quarter 1
        valsComp2.add(c2e1);
        Entry c2e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        valsComp2.add(c2e2);
*/
        LineDataSet setComp1 = new LineDataSet(valsComp1, "Taxa Compra");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet setComp2 = new LineDataSet(valsComp2, "Preco Compra");
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);
/*
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.Q");
        xVals.add("2.Q");
        xVals.add("3.Q");
        xVals.add("4.Q");
*/
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        //Pegando o objeto de legenda
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis x1 = chart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);
        //x1.setLabelsToSkip();
        //x1.resetLabelsToSkip();
        x1.setPosition(XAxis.XAxisPosition.TOP_INSIDE);

        YAxis y1 = chart.getAxisLeft();
        y1.setTextColor(Color.RED);
        y1.setAxisMaxValue(150f);
        y1.setDrawGridLines(false);
        //y1.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);


        YAxis y12 = chart.getAxisRight();
        y12.setEnabled(true);

        chart.invalidate(); // refresh


    }

    @Override
    public void onResponse(String s) {
        Log.i("Tesouro Direto", "Entrei no: onResponse");

        JSONObject json = null;
        try {
            json = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Tesouro Direto", "Mensagem de erro 1 do GraficoFragment -> " + e.getMessage());
        }
        try {
            mlist = TituloTesouroHttp.lerJsonTitulos(json);
            preencherDados();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Tesouro Direto", "Mensagem de erro 2 do GraficoFragment -> " + e.getMessage());
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.i("Tesouro Direto", "Entrei no: onErrorResponse");
        Log.i("Tesouro Direto", "Caiu no onErrorResponse do GraficoFragment");
    }
}