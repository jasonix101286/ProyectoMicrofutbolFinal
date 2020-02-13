package com.example.firebasevocala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firebasevocala.model.Equipo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<Equipo> listEquipo = new ArrayList<Equipo>();
    ArrayAdapter<Equipo> arrayAdapterEquipo;

    EditText ENombre, EFecha;
    ListView listaEquipos;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Equipo equipoSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ENombre = findViewById(R.id.etNombre);
        EFecha = findViewById(R.id.etFecha);
        listaEquipos = findViewById(R.id.lv_Equipos);

        inicializarFirebase();
        listarDatos();

        listaEquipos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public  void onItemClick (AdapterView<?> parent, View view, int position, long id){
                equipoSelected = (Equipo) parent.getItemAtPosition(position);
                ENombre.setText(equipoSelected.getNombre());
                EFecha.setText(equipoSelected.getFechaFundacion());
            }
        });

    }

    private void listarDatos() {
        databaseReference.child("Equipo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listEquipo.clear();
                for (DataSnapshot objSnaptShop : dataSnapshot.getChildren()){{
                    Equipo e = objSnaptShop.getValue(Equipo.class);
                    listEquipo.add(e);

                    arrayAdapterEquipo = new ArrayAdapter<Equipo>(MainActivity.this, android.R.layout.simple_list_item_1, listEquipo);
                    listaEquipos.setAdapter(arrayAdapterEquipo);
                }}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void inicializarFirebase() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String nombreEquipo = ENombre.getText().toString();
        String fechaFundacion = EFecha.getText().toString();

        switch(item.getItemId()){
            case R.id.icon_add:{

                if(nombreEquipo.equals("") || fechaFundacion.equals("")){
                    validacion();
                    break;
                }
                else{
                    Equipo e = new Equipo();
                    e.setEid(UUID.randomUUID().toString());
                    e.setNombre(nombreEquipo);
                    e.setFechaFundacion(fechaFundacion);
                    databaseReference.child("Equipo").child(e.getEid()).setValue(e);

                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                    break;
                }
            }

            case R.id.icon_save:{
                Equipo e = new Equipo();
                e.setEid(equipoSelected.getEid());
                e.setNombre(ENombre.getText().toString().trim());
                e.setFechaFundacion(EFecha.getText().toString().trim());
                databaseReference.child("Equipo").child(e.getEid()).setValue(e);
                Toast.makeText(this, "Grabar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }

            case R.id.icon_delete:{
                Equipo e = new Equipo();
                e.setEid(equipoSelected.getEid());
                databaseReference.child("Equipo").child(e.getEid()).removeValue();
                Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }

        return true;
    }

    private void limpiarCajas() {
        ENombre.setText("");
        EFecha.setText("");

    }

    private void validacion() {
        String nombreEquipo = ENombre.getText().toString();
        String fechaFundacion = EFecha.getText().toString();

        if(nombreEquipo.equals("")){
            ENombre.setError("Requerid");
        }
        else if(fechaFundacion.equals("")){
            EFecha.setError("Requerid");
        }

    }

}

