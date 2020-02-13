package com.example.firebasevocala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class MenuCircular extends AppCompatActivity {

    private Button mButtonSignOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_circular);

        mAuth = FirebaseAuth.getInstance();
        mButtonSignOut = (Button) findViewById(R.id.btnSignout);
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MenuCircular.this, LoginActivity.class));
                finish();
            }
        });


            CircleMenu circleMenu = findViewById(R.id.circlemenu);
            final String [] menus = {"Equipo", "Jugadores", "Vocalía", "Jugadores Vocalía", "Valores Vocalía", "Salir"};

            circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.ic_add, R.drawable.ic_salir)
                    .addSubMenu(Color.parseColor("#0099FF"), R.drawable.ic_equipo)
                    .addSubMenu(Color.parseColor("#0099FF"), R.drawable.ic_jugador)
                    .addSubMenu(Color.parseColor("#0099FF"), R.drawable.ic_vocalia)
                    .addSubMenu(Color.parseColor("#0099FF"), R.drawable.ic_jugadores_vocalia)
                    .addSubMenu(Color.parseColor("#0099FF"), R.drawable.ic_valores_vocalia)
                    .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                        @Override
                        public void onMenuSelected(int i) {
                            Toast.makeText(MenuCircular.this, "Click " + menus[i], Toast.LENGTH_LONG).show();
                        }

                    }).setOnMenuSelectedListener(new OnMenuSelectedListener() {
                @Override
                public void onMenuSelected(int index) {
                    switch(index){
                        case 0: startActivity(new Intent(MenuCircular.this,MainActivity.class));break;
                        case 1: startActivity(new Intent(MenuCircular.this,VJugadores.class));break;
                        case 2: startActivity(new Intent(MenuCircular.this,VVocalia.class));break;
                        case 3: startActivity(new Intent(MenuCircular.this,VJugadoresVocalia.class));break;
                        case 4: startActivity(new Intent(MenuCircular.this,VvaloresVocalia.class));break;
                    }
                }

            });






        }
}
