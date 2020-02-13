package com.example.firebasevocala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuOpciones extends AppCompatActivity {

    Button btnEquipo, btnJugadores, btnCerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_opciones);

        btnEquipo = findViewById(R.id.btnEquipo);
        btnJugadores = findViewById(R.id.btnJugadores);
    }

    public void vistaEquipo(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void vistaJugadores(View view) {
        Intent intent = new Intent(this, VJugadores.class);
        startActivity(intent);
    }

    public void vistaJugadoresVocalia(View view) {
        Intent intent = new Intent(this, VJugadoresVocalia.class);
        startActivity(intent);
    }

    public void vistaVocalia(View view) {
        Intent intent = new Intent(this, VVocalia.class);
        startActivity(intent);
    }

    public void vistaValoresVocalia(View view) {
        Intent intent = new Intent(this, VvaloresVocalia.class);
        startActivity(intent);
    }



}
