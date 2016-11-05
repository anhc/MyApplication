package com.example.antnio.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;

public class ActTelaParaMarcar extends AppCompatActivity implements View.OnClickListener {

    String classificacao = "vazio";
    private Button btnOk, btnCancelar;
    //RadioGroup grupo1;
    //RadioButton bom;
    private TextView teste;
    Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tela_para_marcar);

        btnOk = (Button)findViewById(R.id.btnOk);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);

        btnOk.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        teste = (TextView) findViewById(R.id.teste);

        //grupo1 = (RadioGroup) findViewById(R.id.grupo1);

        //grupo1.setEnabled(false);

        /*for(int i = 0; i < grupo1.getChildCount(); i++) {
            grupo1.getChildAt(i).setEnabled(false);
        }*/

        //bom = (RadioButton) findViewById(R.id.bom);

        //bom.setEnabled(false);

        }

    public void onClick(View v){
        it = new Intent(this, MapsActivity.class);
        switch (v.getId()){
            case R.id.btnOk:
                if(it!=null && !classificacao.equals("vazio")) {
                    it.putExtra("Classificacao", classificacao);

                    if(classificacao.equals("Bom")){
                        setResult(1);
                        MapsActivity.tipo = "Tee";
                    }
                    else{
                        setResult(2);
                        MapsActivity.tipo = "TESTANDO NNN STATIC";
                    }
                    finish();
                }
                break;
            case R.id.btnCancelar:
                if(it!=null) {
                    setResult(3);
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
