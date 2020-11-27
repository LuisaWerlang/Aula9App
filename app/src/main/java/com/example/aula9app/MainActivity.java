package com.example.aula9app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView txtCarros;
    private DatabaseHelper helper;
    private EditText txtAno, txtModelo;
    private ListView lista;
    List<Map<String, Object>> carros;
    String[] de = {"id", "modelo", "ano", "valor"};
    int[] para = {R.id.tvId, R.id.tvModelo, R.id.tvAno, R.id.tvValor};
    TextView sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Busca de carros cadastrados");

        txtCarros = (TextView) findViewById(R.id.txtCarros);
        txtCarros.setTypeface(null, Typeface.BOLD);

        sessao = findViewById(R.id.tvSessao);

        Bundle extras = getIntent().getExtras();
        sessao.setText(extras.getString("sessao"));

        txtAno = findViewById(R.id.txtAno);
        txtModelo = findViewById(R.id.txtModelo);
        lista = findViewById(R.id.txtDados);
        lista.setClickable(true);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String idDados = String.valueOf(carros.get(i).get("id"));
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("idDados", idDados);
                startActivity(intent);
            }
        });
        helper = new DatabaseHelper(this);
    }

    public void buscarAno(View view) {
        String buscaAno = txtAno.getText().toString();
        String query = "";

        if(buscaAno.isEmpty()) {
            query = "SELECT * FROM carro";
        }
        else {
            query = "SELECT * FROM carro WHERE ano = " + buscaAno;
        }
//        carros = listarCarros(query);
//        SimpleAdapter adapter = new SimpleAdapter(this, carros, R.layout.listagem, de, para);
//        lista.setAdapter(adapter);

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        loadList(c);
    }

    private List<Map<String, Object>> listarCarros(String query) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        carros = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < cursor.getCount(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            String id = cursor.getString(0);
            String modelo = cursor.getString(1);
            int ano = cursor.getInt(2);
            double valor = cursor.getDouble(3);
            item.put("id", id);
            item.put("modelo", "Modelo: " + modelo);
            item.put("ano", "Ano: " + ano);
            item.put("valor", String.format("R$ %.2f", valor));
            carros.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return carros;
    }

    public void adicionarCarro(View view) {
        startActivity(new Intent(this,AddActivity.class));
    }

    public void buscarModelo(View view) {
        String buscaModelo = txtModelo.getText().toString();
        SQLiteDatabase db = helper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("carro");
        String [] projection = new String[] {"*"};
        String selection;
        String[] selArgs;

        if(buscaModelo.isEmpty()) {
            selection = null;
            selArgs = null;
        }
        else {
            selection = "modelo=?";
            selArgs = new String[]{buscaModelo};
        }
        Cursor c = builder.query(db, projection, selection, selArgs,null,null,"ano DESC");
        loadList(c);
    }

    private void loadList(Cursor c) {
        carros = new ArrayList<Map<String, Object>>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Map<String, Object> mapa = new HashMap<String, Object>();
            String id = c.getString(0);
            String modelo = c.getString(1);
            String ano = c.getString(2);
            String valor = c.getString(3);
            mapa.put("id", id);
            mapa.put("modelo", modelo);
            mapa.put("ano", ano);
            mapa.put("valor", valor);
            carros.add(mapa);
            c.moveToNext();
        }
        c.close();
        SimpleAdapter adapter = new SimpleAdapter(this, carros, R.layout.listagem, de, para);
        lista.setAdapter(adapter);
    }

    public void fazerLogout(View view) {
        SharedPreferences settings = getSharedPreferences(" UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("user", "");
        editor.putString("pass", "");
        editor.putInt("session", 0);
        editor.commit();
        editor.apply();
        Toast.makeText(this, "Você fez logout!", Toast.LENGTH_LONG).show();

        startActivity(new Intent(this,LoginActivity.class));
    }
}