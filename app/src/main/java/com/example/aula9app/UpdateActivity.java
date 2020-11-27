package com.example.aula9app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    TextView txtCarros;
    String idDados = "";
    private DatabaseHelper helper;
    private EditText modelo, valor, ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setTitle("Alterar/excluir o carro selecionado");

        txtCarros = (TextView) findViewById(R.id.txtCarros);
        txtCarros.setTypeface(null, Typeface.BOLD);

        Bundle extras = getIntent().getExtras();
        idDados = extras.getString("idDados");

        modelo = findViewById(R.id.txtModelo);
        valor = findViewById(R.id.txtValor);
        ano = findViewById(R.id.txtAno);

        helper = new DatabaseHelper(this);
        preencheCampos();
    }

    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    private void preencheCampos() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM carro WHERE id = " + idDados, null);
        cursor.moveToFirst();
        modelo.setText(cursor.getString(1));
        ano.setText(String.valueOf(cursor.getInt(2)));
        valor.setText(String.valueOf(cursor.getDouble(3)));
        cursor.close();
    }

    public void atualizarCarro(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("modelo", modelo.getText().toString());
        values.put("ano", Integer.parseInt(ano.getText().toString()));
        values.put("valor", Double.parseDouble(valor.getText().toString()));

        String where[] = new String[]{idDados};
        long resultado = db.update("carro", values, "id = ?", where);
        if (resultado != -1) {
            Toast.makeText(this, "Registro atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Erro ao atualizar!", Toast.LENGTH_SHORT).show();
        }
    }

    public void excluirCarro(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where[] = new String[]{idDados};
        long resultado = db.delete("carro", "id = ?", where);
        if (resultado != -1) {
            Toast.makeText(this, "Registro excluído com sucesso!", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Erro ao excluir!", Toast.LENGTH_SHORT).show();
        }
    }
}