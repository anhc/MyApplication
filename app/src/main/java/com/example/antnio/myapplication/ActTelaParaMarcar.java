package com.example.antnio.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.*;
import  android.view.*;
import android.content.*;

public class ActTelaParaMarcar extends AppCompatActivity implements View.OnClickListener {

    String classificacao;
    private Button btnOk, btnCancelar;
    private TextView teste;
    Intent it;
    //private RadioButton bom, ruim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tela_para_marcar);

        //bom = (RadioButton)findViewById(R.id.bom);
        //ruim = (RadioButton)findViewById(R.id.ruim);

        btnOk = (Button)findViewById(R.id.btnOk);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);

        btnOk.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        teste = (TextView) findViewById(R.id.teste);


        }

    public void onClick(View v){
        it = new Intent(this, MapsActivity.class);
        switch (v.getId()){
            case R.id.btnOk:
                if(it!=null) {
                    it.putExtra("Classificacao", classificacao);
                    it.putExtra("MARCAR", true);

                    if(classificacao.equals("Bom"))
                        setResult(0);
                    else
                        setResult(1);
                    finish();
                }
                break;
            case R.id.btnCancelar:
                if(it!=null) {
                    it.putExtra("MARCAR", false);
                    setResult(2);
                    finish();
                }
                break;
        }

    }

    public void tipoSelecionado(View view){
        Bundle bundle = getIntent().getExtras();        //trecho para testar passagem de parametros

        if(bundle != null) {                            //trecho para testar passagem de parametros
            String tipo = bundle.getString("TESTE");    //trecho para testar passagem de parametros
            teste.setText(tipo);                        //trecho para testar passagem de parametros
        }
        switch (view.getId()){
            case R.id.bom:
                classificacao = "Bom";
                break;
            case R.id.ruim:
                classificacao = "Ruim";
                break;
        }
    }

}
