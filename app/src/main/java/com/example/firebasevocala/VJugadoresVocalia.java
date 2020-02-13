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
import com.example.firebasevocala.model.JugadoresVocalia;
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

public class VJugadoresVocalia extends AppCompatActivity {

    private List<JugadoresVocalia> listJV = new ArrayList<JugadoresVocalia>();
    ArrayAdapter<JugadoresVocalia> arrayAdapterJV;

    private List<Equipo> listEquipo = new ArrayList<Equipo>();
    ArrayAdapter<Equipo> arrayAdapterEquipo;

    private List<Jugador> listJugador = new ArrayList<Jugador>();
    ArrayAdapter<Jugador> arrayAdapterJugador;

    EditText edtxIdJV, edtxIdJugador, edtxIdEquipo, edtxGoles, edtxTarjetaAmarilla, edtxTarjetaRoja, edtxIdVocalia;
    ListView listaJV;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    JugadoresVocalia jvSelected;
    Equipo equipoSelected;
    Jugador jugadorSelected;

    String idEquipo= "";
    String idJugador= "";
    Spinner spEquipos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores_vocalia);

        //edtxIdJV = findViewById(R.id.edtxIdJV);
        edtxIdJugador = findViewById(R.id.edtxIdJugador);
        edtxIdEquipo= findViewById(R.id.edtxIdEquipo);
        edtxGoles= findViewById(R.id.edtxGoles);
        edtxTarjetaAmarilla= findViewById(R.id.edtxTarjetaAmarilla);
        edtxTarjetaRoja= findViewById(R.id.edtxTarjetaRoja);
        edtxIdVocalia= findViewById(R.id.edtxIdVocalia);
        listaJV = findViewById(R.id.lv_JV);
        spEquipos = findViewById(R.id.spEquipos1);

        inicializarFirebase();
        listarDatos();
        loadEquipos();

        listaJV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public  void onItemClick (AdapterView<?> parent, View view, int position, long id){
                jugadorSelected = (Jugador) parent.getItemAtPosition(position);
                edtxIdJugador.setText(jugadorSelected.getNombre());
                edtxIdEquipo.setText(jugadorSelected.getIdEquipo());

            }
        });

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDatos() {

        databaseReference.child("Jugador").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Query q = databaseReference.orderByChild(idEquipo).equalTo("27433675-82c2-420c-900e-19b2368d35f7");

                listJugador.clear();
                for (DataSnapshot objSnaptShop : dataSnapshot.getChildren()){{

                    Jugador j = objSnaptShop.getValue(Jugador.class);
                    if(j.getIdEquipo().equals(idEquipo))
                    {
                        listJugador.add(j);
                        arrayAdapterJugador = new ArrayAdapter<Jugador>(VJugadoresVocalia.this, android.R.layout.simple_list_item_1, listJugador);
                        listaJV.setAdapter(arrayAdapterJugador);
                    }
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

                    ArrayAdapter<Equipo> arrayAdapter = new ArrayAdapter<>(VJugadoresVocalia.this, android.R.layout.simple_dropdown_item_1line, equipos);
                    spEquipos.setAdapter(arrayAdapter);
                    spEquipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> lista, View view, int position, long id) {
                            //idEquipo = parent.getItemAtPosition(position).toString();
                            idEquipo = equipos.get(position).getNombre();
                            listarDatos();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

                Toast.makeText(VJugadoresVocalia.this, idEquipo, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void limpiarCajas() {
        edtxIdJugador.setText("");
        edtxIdEquipo.setText("");
        edtxGoles.setText("");
        edtxTarjetaAmarilla.setText("");
        edtxTarjetaRoja.setText("");
        edtxIdVocalia.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String idJugador = edtxIdJugador.getText().toString();
        String idEquipo = edtxIdEquipo.getText().toString();
        String Goles = edtxGoles.getText().toString();
        String TA = edtxTarjetaAmarilla.getText().toString();
        String TR = edtxTarjetaRoja.getText().toString();
        String idVocalia = edtxIdVocalia.getText().toString();

        switch(item.getItemId()){
            case R.id.icon_add:{

                if(idJugador.equals("") || idEquipo.equals("") || Goles.equals("")
                        || TA.equals("")|| TR.equals("")|| idVocalia.equals("")){
                    validacion();
                    break;
                }
                else{
                    JugadoresVocalia jv = new JugadoresVocalia();
                    jv.setIdJV(UUID.randomUUID().toString());
                    jv.setIdJugador(idJugador);
                    jv.setIdEquipo(idEquipo);
                    jv.setGoles(Goles);
                    jv.setTamarilla(TA);
                    jv.setTroja(TR);
                    jv.setIdVocalia(idVocalia);
                    databaseReference.child("JugadoresVocalia").child(jv.getIdJV()).setValue(jv);

                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                    break;
                }
            }

            case R.id.icon_save:{
                JugadoresVocalia jv = new JugadoresVocalia();
                jv.setIdJV(jvSelected.getIdJV());
                jv.setIdJugador(edtxIdJugador.getText().toString().trim());
                jv.setIdEquipo(edtxIdEquipo.getText().toString().trim());
                jv.setGoles(edtxGoles.getText().toString().trim());
                jv.setTamarilla(edtxTarjetaAmarilla.getText().toString().trim());
                jv.setTroja(edtxTarjetaRoja.getText().toString().trim());
                jv.setIdVocalia(edtxIdVocalia.getText().toString().trim());
                databaseReference.child("JugadoresVocalia").child(jv.getIdJV()).setValue(jv);
                Toast.makeText(this, "Grabar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }

            case R.id.icon_delete:{
                JugadoresVocalia jv = new JugadoresVocalia();
                jv.setIdJV(jvSelected.getIdJV());
                databaseReference.child("JugadoresVocalia").child(jv.getIdJV()).removeValue();
                Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }

        return true;
    }

    private void validacion() {

        String idJugador = edtxIdJugador.getText().toString();
        String idEquipo = edtxIdEquipo.getText().toString();
        String Goles = edtxGoles.getText().toString();
        String TA = edtxTarjetaAmarilla.getText().toString();
        String TR = edtxTarjetaRoja.getText().toString();
        String idVocalia = edtxIdVocalia.getText().toString();

        if(idJugador.equals("")){
            edtxIdJugador.setError("Requerid");
        }
        else if(idEquipo.equals("")){
            edtxIdEquipo.setError("Requerid");
        }
        else if(Goles.equals("")){
            edtxGoles.setError("Requerid");
        }
        else if(TA.equals("")){
            edtxTarjetaAmarilla.setError("Requerid");
        }
        else if(TR.equals("")){
            edtxTarjetaRoja.setError("Requerid");
        }
        else if(idVocalia.equals("")){
            edtxIdVocalia.setError("Requerid");
        }
    }
}
