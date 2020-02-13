package com.example.firebasevocala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.firebasevocala.model.Equipo;
import com.example.firebasevocala.model.Jugador;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VJugadores extends AppCompatActivity {

    String idEquipo = "";
    private List<Jugador> listJugadores = new ArrayList<Jugador>();
    private List<Equipo> listEquipo = new ArrayList<Equipo>();
    ArrayAdapter<Jugador> arrayAdapterJugador;
    ArrayAdapter<Equipo> arrayAdapterEquipo;

    EditText edtxNombre, edtxNumero, edtxCedula;
    ListView listaJugador;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Jugador jugadorSelected;


    Spinner spEquipos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores);

        edtxNombre = findViewById(R.id.edtxNombre);
        edtxNumero = findViewById(R.id.edtxNumero);
        edtxCedula = findViewById(R.id.edtxtCedula);
        listaJugador = findViewById(R.id.lv_Jugadores);
        spEquipos = findViewById(R.id.spinnerEquipos);


        inicializarFirebase();
        listarDatos();
        loadEquipos();


        listaJugador.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public  void onItemClick (AdapterView<?> parent, View view, int position, long id){
                jugadorSelected = (Jugador) parent.getItemAtPosition(position);
                edtxNombre.setText(jugadorSelected.getNombre());
                edtxNumero.setText(jugadorSelected.getNumero());
                edtxCedula.setText(jugadorSelected.getCedula());
            }
        });


    }

    private void listarDatos() {
        databaseReference.child("Jugador").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listJugadores.clear();
                for (DataSnapshot objSnaptShop : dataSnapshot.getChildren()){{
                    Jugador j = objSnaptShop.getValue(Jugador.class);
                    listJugadores.add(j);

                    arrayAdapterJugador = new ArrayAdapter<Jugador>(VJugadores.this, android.R.layout.simple_list_item_1, listJugadores);
                    listaJugador.setAdapter(arrayAdapterJugador);
                }}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadEquipos(){
        final List<Equipo> equipos = new ArrayList<>();
        databaseReference.child("Equipo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        Equipo e = ds.getValue(Equipo.class);
                        equipos.add(e);
                        String eid = ds.child("eid").getValue().toString();
                        idEquipo = eid;
                        //String nombre = ds.child("nombre").getValue().toString();
                        //equipos.add(new Equipo(eid,nombre));
                    }

                    ArrayAdapter<Equipo> arrayAdapter = new ArrayAdapter<>(VJugadores.this, android.R.layout.simple_dropdown_item_1line, equipos);
                    spEquipos.setAdapter(arrayAdapter);
                    spEquipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //idEquipo = parent.getItemAtPosition(position).toString();
                            idEquipo = equipos.get(position).getNombre();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
                //listarDatos();
                //Toast.makeText(VJugadores.this, idEquipo, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void limpiarCajas() {
        edtxNombre.setText("");
        edtxNumero.setText("");
        edtxCedula.setText("");
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

        String nombreJugador = edtxNombre.getText().toString();
        String numeroJugador = edtxNumero.getText().toString();
        String cedulaJugador = edtxCedula.getText().toString();

        switch(item.getItemId()){
            case R.id.icon_add:{

                if(nombreJugador.equals("") || numeroJugador.equals("") || cedulaJugador.equals("")){
                    validacion();
                    break;
                }
                else{
                    Jugador j = new Jugador();
                    j.setJid(UUID.randomUUID().toString());
                    j.setNombre(nombreJugador);
                    j.setNumero(numeroJugador);
                    j.setCedula(cedulaJugador);
                    j.setIdEquipo(idEquipo);
                    databaseReference.child("Jugador").child(j.getJid()).setValue(j);

                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                    break;
                }
            }

            case R.id.icon_save:{
                Jugador j = new Jugador();
                j.setJid(jugadorSelected.getJid());
                j.setNombre(edtxNombre.getText().toString().trim());
                j.setNumero(edtxNumero.getText().toString().trim());
                j.setCedula(edtxCedula.getText().toString().trim());
                databaseReference.child("Jugador").child(j.getJid()).setValue(j);
                Toast.makeText(this, "Grabar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }

            case R.id.icon_delete:{
                Jugador j = new Jugador();
                j.setJid(jugadorSelected.getJid());
                databaseReference.child("Jugador").child(j.getJid()).removeValue();
                Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }

        return true;
    }

    private void validacion() {

        String nombreJugador = edtxNombre.getText().toString();
        String numeroJugador = edtxNumero.getText().toString();
        String cedulaJugador = edtxCedula.getText().toString();

        if(nombreJugador.equals("")){
            edtxNombre.setError("Requerid");
        }
        else if(numeroJugador.equals("")){
            edtxNumero.setError("Requerid");
        }
        else if(cedulaJugador.equals("")){
            edtxCedula.setError("Requerid");
        }
    }

}
