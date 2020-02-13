package com.example.firebasevocala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.firebasevocala.model.ValoresVocalia;
import com.example.firebasevocala.model.Vocalia;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VvaloresVocalia extends AppCompatActivity {


    private List<Jugador> listJugadores = new ArrayList<Jugador>();
    private List<Equipo> listEquipo = new ArrayList<Equipo>();
    ArrayAdapter<Jugador> arrayAdapterJugador;
    ArrayAdapter<Equipo> arrayAdapterEquipo;
    ListView listaJugador;
    Jugador jugadorSelected;


    String idEquipo = "";
    String nombresEquipos="";
    private List<ValoresVocalia> listValoresVocalia = new ArrayList<ValoresVocalia>();
    private List<Vocalia> listVocalia = new ArrayList<Vocalia>();
    ArrayAdapter<ValoresVocalia> arrayAdapterValoresVocalia;
    ArrayAdapter<Vocalia> arrayAdapterVocalia;

    EditText edtxValorArbitraje, edtxValorAporte, edtxValorSeguro, edtxValorTA, edtxValorTR,edtxOtrosValores;
    ListView listaValoresVocalia;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValoresVocalia valoresVocalíaSelected;


    Spinner spEquipos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vvalores_vocalia);

        edtxValorArbitraje = findViewById(R.id.edtxValorArbitraje);
        edtxValorAporte = findViewById(R.id.edtxValorAporte);
        edtxValorSeguro = findViewById(R.id.edtxValorSeguro);
        edtxValorTA = findViewById(R.id.edtxValorTA);
        edtxValorTR = findViewById(R.id.edtxValorTR);
        edtxOtrosValores = findViewById(R.id.edtxOtrosValores);
        listaValoresVocalia = findViewById(R.id.lvValoresVocalia);
        spEquipos = findViewById(R.id.spEquipos);

        inicializarFirebase();
        listarDatos();
        loadVocalia();

        listaValoresVocalia.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public  void onItemClick (AdapterView<?> parent, View view, int position, long id){
                valoresVocalíaSelected = (ValoresVocalia) parent.getItemAtPosition(position);

            }
        });
        
        
    }

    private void listarDatos() {
        databaseReference.child("ValoresVocalía").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listValoresVocalia.clear();
                for (DataSnapshot objSnaptShop : dataSnapshot.getChildren()){{
                    ValoresVocalia j = objSnaptShop.getValue(ValoresVocalia.class);
                    listValoresVocalia.add(j);

                    arrayAdapterValoresVocalia = new ArrayAdapter<ValoresVocalia>(VvaloresVocalia.this, android.R.layout.simple_list_item_1, listValoresVocalia);
                    listaValoresVocalia.setAdapter(arrayAdapterValoresVocalia);
                }}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadVocalia() {
        final List<Vocalia> equipos = new ArrayList<>();
        databaseReference.child("Vocalia").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        Vocalia e = ds.getValue(Vocalia.class);
                        equipos.add(e);
                        String eid = ds.child("nombresEquipos").getValue().toString();
                        nombresEquipos= eid;
                        //String nombre = ds.child("nombre").getValue().toString();
                        //equipos.add(new Equipo(eid,nombre));
                    }

                    ArrayAdapter<Vocalia> arrayAdapter = new ArrayAdapter<>(VvaloresVocalia.this, android.R.layout.simple_dropdown_item_1line, equipos);
                    spEquipos.setAdapter(arrayAdapter);
                    spEquipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //idEquipo = parent.getItemAtPosition(position).toString();
                            idEquipo = equipos.get(position).getIdVocalia();
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


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }


    private void limpiarCajas() {
        edtxValorArbitraje.setText("");
        edtxValorAporte.setText("");
        edtxValorSeguro.setText("");
        edtxValorTA.setText("");
        edtxValorTR.setText("");
        edtxOtrosValores.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String vArbtitraje = edtxValorArbitraje.getText().toString();
        String vAporte = edtxValorAporte.getText().toString();
        String vseguro = edtxValorSeguro.getText().toString();
        String vTA = edtxValorTA.getText().toString();
        String vTR = edtxValorTR.getText().toString();
        String vOtrosValores = edtxOtrosValores.getText().toString();

        switch(item.getItemId()){
            case R.id.icon_add:{

                if(vArbtitraje.equals("") || vAporte.equals("") || vseguro.equals("")
                        || vTA.equals("")|| vTR.equals("")|| vOtrosValores.equals("")){
                    validacion();
                    break;
                }
                else{
                    ValoresVocalia v = new ValoresVocalia();
                    v.setIdValores(UUID.randomUUID().toString());
                    v.setIdVocalia(idEquipo);
                    v.setIdEquipo(nombresEquipos);
                    v.setValorArbitraje(vArbtitraje);
                    v.setValorAporte(vAporte);
                    v.setValorSeguro(vseguro);
                    v.setValorTA(vTA);
                    v.setValorTR(vTR);
                    v.setOtrosValores(vOtrosValores);
                    databaseReference.child("ValoresVocalía").child(v.getIdValores()).setValue(v);

                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                    break;
                }
            }

            case R.id.icon_save:{
                ValoresVocalia v = new ValoresVocalia();
                v.setIdValores(valoresVocalíaSelected.getIdValores());
                v.setIdVocalia("");
                v.setIdEquipo(idEquipo);
                v.setValorArbitraje(edtxValorArbitraje.getText().toString().trim());
                v.setValorAporte(edtxValorAporte.getText().toString().trim());
                v.setValorSeguro(edtxValorSeguro.getText().toString().trim());
                v.setValorTA(edtxValorTA.getText().toString().trim());
                v.setValorTR(edtxValorTR.getText().toString().trim());
                v.setOtrosValores(edtxOtrosValores.getText().toString().trim());
                databaseReference.child("ValoresVocalía").child(v.getIdValores()).setValue(v);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }

            case R.id.icon_delete:{
                ValoresVocalia v = new ValoresVocalia();
                v.setIdValores(valoresVocalíaSelected.getIdVocalia());
                databaseReference.child("ValoresVocalía").child(v.getIdVocalia()).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }

        return true;
    }

    private void validacion() {

        String vArbtitraje = edtxValorArbitraje.getText().toString();
        String vAporte = edtxValorAporte.getText().toString();
        String vseguro = edtxValorSeguro.getText().toString();
        String vTA = edtxValorTA.getText().toString();
        String vTR = edtxValorTR.getText().toString();
        String vOtrosValores = edtxOtrosValores.getText().toString();

        if(vArbtitraje.equals("")){
            edtxValorArbitraje.setError("Requerid");
        }
        else if(vAporte.equals("")){
            edtxValorAporte.setError("Requerid");
        }
        else if(vseguro.equals("")){
            edtxValorSeguro.setError("Requerid");
        }
        else if(vTA.equals("")){
            edtxValorTA.setError("Requerid");
        }
        else if(vTR.equals("")){
            edtxValorTR.setError("Requerid");
        }
        else if(vOtrosValores.equals("")){
            edtxOtrosValores.setError("Requerid");
        }
    }



    public void Suma(View view) {
        double arbitraje, aporte, seguro, ta, tr ,otros, total;
        String stringTotal="";

        String vArbtitraje = edtxValorArbitraje.getText().toString();
        String vAporte = edtxValorAporte.getText().toString();
        String vseguro = edtxValorSeguro.getText().toString();
        String vTA = edtxValorTA.getText().toString();
        String vTR = edtxValorTR.getText().toString();
        String vOtrosValores = edtxOtrosValores.getText().toString();

        arbitraje = Double.parseDouble(vArbtitraje);
        aporte = Double.parseDouble(vAporte);
        seguro = Double.parseDouble(vseguro);
        ta = Double.parseDouble(vTA);
        tr = Double.parseDouble(vTR);
        otros = Double.parseDouble(vOtrosValores);

        total = arbitraje+aporte+seguro+ta+tr+otros;
        stringTotal = Double.toString(total);

        Toast.makeText(VvaloresVocalia.this, stringTotal, Toast.LENGTH_LONG).show();

    }
}
