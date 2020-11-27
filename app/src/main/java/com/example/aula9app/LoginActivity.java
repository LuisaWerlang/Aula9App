package com.example.aula9app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText login, senha;
    EditText user, senha1, senha2;
    String sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTela1();
    }

    public void setTela1() {
        setContentView(R.layout.activity_login);
        setTitle("Login");
        login = findViewById(R.id.txtUserLogin);
        senha = findViewById(R.id.txtSenhaLogin);
    }

    public void setTela2() {
        setContentView(R.layout.activity_cadastro);
        setTitle("Cadastro");
        user = findViewById(R.id.txtUserCadastro);
        senha1 = findViewById(R.id.txtSenha1Cadastro);
        senha2 = findViewById(R.id.txtSenha2Cadastro);
    }

    private void setTela3() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("sessao", sessao);
        startActivity(intent);
    }

    public void loginClick(View view) {
        String user = login.getText().toString();
        String pass = senha.getText().toString();

        if (user.equals("") || pass.equals("")) {
            messageBox("Os campos estão em branco! Preencha eles para prosseguir");
            return;
        }
        SharedPreferences settings = getSharedPreferences(" UserInfo", MODE_PRIVATE);
        String userSettings = settings.getString("user", "");
        String passSettings = settings.getString("pass", "");
        int session = settings.getInt("session", 0);

        if (user.equals(userSettings) || pass.equals(passSettings)) {
            session++;
            //sessao.setText("Sessão #" + session);
            sessao = "Sessão #" + session;
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("session", session);
            editor.commit();
            editor.apply();
            setTela3();
        }
        else {
            messageBox("Usuário e senha incorretos");
            setTela2();
        }
    }

    public void gravarClick(View view) {
        String user1 = user.getText().toString();
        String pass1 = senha1.getText().toString();
        String pass2 = senha2.getText().toString();

        if (user1.equals("") || pass1.equals("") || pass2.equals("")) {
            messageBox("Os campos estão em branco! Preencha eles para prosseguir");
            return;
        }

        if (!pass1.equals(pass2)) {
            messageBox("Senhas diferentes, tente novamente!");
            return;
        }

        SharedPreferences settings = getSharedPreferences(" UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("user", user1);
        editor.putString("pass", pass1);
        editor.commit();
        editor.apply();
        messageBox("Cadastro realizado com sucesso!");
        setTela1();
    }

    public void voltarClick(View view) {
        setTela1();
    }

    private void messageBox(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}