package com.example.aula9app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    TextView txtCarros;
    private DatabaseHelper helper;
    private EditText modelo, valor, ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Cadastrar novo carro");

        txtCarros = (TextView) findViewById(R.id.txtCarros);
        txtCarros.setTypeface(null, Typeface.BOLD);

        modelo = findViewById(R.id.txtModelo);
        valor = findViewById(R.id.txtValor);
        ano = findViewById(R.id.txtAno);

        helper = new DatabaseHelper(this);
    }

    public void salvarCarro(View view) {

        if (modelo.getText().equals("") || valor.getText().equals("") || ano.getText().equals("")) {
            Toast.makeText(this, "Os campos estão em branco! Preencha eles para prosseguir", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("modelo", modelo.getText().toString());
        values.put("ano", Integer.parseInt(ano.getText().toString()));
        values.put("valor", Double.parseDouble(valor.getText().toString()));

        long resultado = db.insert("carro", null, values);
        if(resultado != -1) {
            Toast.makeText(AddActivity.this, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
            limpar();
        }
        else {
            Toast.makeText(AddActivity.this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
        }
    }

    public void limpar()
    {
        modelo.setText("");
        ano.setText("");
        valor.setText("");
    }

    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}