package com.example.firebasevocala;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebasevocala.model.Equipo;
import com.example.firebasevocala.model.Vocalia;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class VVocalia extends AppCompatActivity {
    String idEquipo1 = "";
    String idEquipo2 = "";
    Button GPS, btnFoto;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;
    private ImageView mImageView;
    private ProgressDialog mProgressDialog;

    TextView coordenadasGPS;
    String stringGPS="";


    private List<Vocalia> listVocalia = new ArrayList<Vocalia>();
    private List<Equipo> listEquipo = new ArrayList<Equipo>();
    ArrayAdapter<Vocalia> arrayAdapterVocalia;
    ArrayAdapter<Equipo> arrayAdapterEquipo;

    EditText nombreVocal, nombreArbitro;
    ListView listaVocalia;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Vocalia vocaliaSelected;

    Spinner spCampeonato, spHorarrio, spEquipo1,spEquipo2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocalia);

        mStorage = FirebaseStorage.getInstance().getReference();

        btnFoto = (Button) findViewById(R.id.btnImagen);
        mImageView = findViewById(R.id.imvImagen);
        mProgressDialog = new ProgressDialog(this);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);

            }
        });


        coordenadasGPS = (TextView) findViewById(R.id.mensaje_id);
        GPS = (Button) findViewById(R.id.btnGPS);


        GPS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                LocationManager locationManager = (LocationManager) VVocalia.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        coordenadasGPS.setText(""+location.getLatitude()+" "+ location.getLongitude());
                        stringGPS = coordenadasGPS.toString();
                        //Toast.makeText(VVocalia.this, "GPS", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                };
                int permissionCheck = ContextCompat.checkSelfPermission
                        (VVocalia.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, locationListener);
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission
                (VVocalia.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale
                    (VVocalia.this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);


            }
        }



        spCampeonato = (Spinner) findViewById(R.id.sp_campeonato);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(this, R.array.Campeonato, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCampeonato.setAdapter(spinner_adapter);
        //String Campeonato = spCampeonato.getSelectedItem().toString();


        spHorarrio = (Spinner) findViewById(R.id.sp_horario);
        ArrayAdapter spinner_adapter_horario = ArrayAdapter.createFromResource(this, R.array.Horario, android.R.layout.simple_spinner_item);
        spinner_adapter_horario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHorarrio.setAdapter(spinner_adapter_horario);
        //String Horario = spHorarrio.getSelectedItem().toString();

        nombreVocal = findViewById(R.id.edtxNombreVocal);
        nombreArbitro = findViewById(R.id.edtxNombreArbitro);
        listaVocalia = findViewById(R.id.lv_Vocalia);

        spEquipo1 = findViewById(R.id.spEquipos1);
        spEquipo2 = findViewById(R.id.spEquipos2);

        inicializarFirebase();
        listarDatos();
        loadEquipos1();
        loadEquipos2();

        listaVocalia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vocaliaSelected = (Vocalia) parent.getItemAtPosition(position);
                nombreVocal.setText(vocaliaSelected.getNombreVocal());
                nombreArbitro.setText(vocaliaSelected.getNombreArbitro());
            }
        });
    }

    private void listarDatos() {
        databaseReference.child("Vocalia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listVocalia.clear();
                for (DataSnapshot objSnaptShop : dataSnapshot.getChildren()){{
                    Vocalia v = objSnaptShop.getValue(Vocalia.class);
                    listVocalia.add(v);

                    arrayAdapterVocalia = new ArrayAdapter<Vocalia>(VVocalia.this, android.R.layout.simple_list_item_1, listVocalia);
                    listaVocalia.setAdapter(arrayAdapterVocalia);
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void loadEquipos1(){
        final List<Equipo> equipos = new ArrayList<>();
        databaseReference.child("Equipo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        Equipo e = ds.getValue(Equipo.class);
                        equipos.add(e);
                        String eid = ds.child("eid").getValue().toString();
                        idEquipo1 = eid;
                        //String nombre = ds.child("nombre").getValue().toString();
                        //equipos.add(new Equipo(eid,nombre));
                    }

                    ArrayAdapter<Equipo> arrayAdapter = new ArrayAdapter<>(VVocalia.this, android.R.layout.simple_dropdown_item_1line, equipos);
                    spEquipo1.setAdapter(arrayAdapter);
                    spEquipo1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //idEquipo = parent.getItemAtPosition(position).toString();
                            idEquipo1 = equipos.get(position).getNombre();
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

    public void loadEquipos2(){
        final List<Equipo> equipos = new ArrayList<>();
        databaseReference.child("Equipo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        Equipo e = ds.getValue(Equipo.class);
                        equipos.add(e);
                        String eid = ds.child("eid").getValue().toString();
                        idEquipo2 = eid;
                        //String nombre = ds.child("nombre").getValue().toString();
                        //equipos.add(new Equipo(eid,nombre));
                    }

                    ArrayAdapter<Equipo> arrayAdapter = new ArrayAdapter<>(VVocalia.this, android.R.layout.simple_dropdown_item_1line, equipos);
                    spEquipo2.setAdapter(arrayAdapter);
                    spEquipo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //idEquipo = parent.getItemAtPosition(position).toString();
                            idEquipo2 = equipos.get(position).getNombre();
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String Campeonato = spCampeonato.getSelectedItem().toString();
        String Horario = spHorarrio.getSelectedItem().toString();
        String nVocal = nombreVocal.getText().toString();
        String nArbitro= nombreArbitro.getText().toString();
        String coor = coordenadasGPS.getText().toString();

        switch(item.getItemId()){
            case R.id.icon_add:{

                if(nombreVocal.equals("") || nombreArbitro.equals("")){
                    validacion();
                    break;
                }
                else{
                    Vocalia v = new Vocalia();
                    v.setIdVocalia(UUID.randomUUID().toString());
                    v.setCampeonato(Campeonato);
                    v.setNombreVocal(nVocal);
                    v.setHorario(Horario);
                    v.setNombreArbitro(nArbitro);
                    v.setUbicación(coor);
                    v.setNombresEquipos(idEquipo1 + " vs " + idEquipo2);
                    databaseReference.child("Vocalia").child(v.getIdVocalia()).setValue(v);

                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                    break;
                }
            }

            case R.id.icon_save:{
                Vocalia v = new Vocalia();
                v.setIdVocalia(vocaliaSelected.getIdVocalia());
                v.setCampeonato(spCampeonato.toString().trim());
                v.setNombreVocal(nombreVocal.getText().toString().trim());
                v.setHorario(spHorarrio.toString().trim());
                v.setNombreArbitro(nombreArbitro.getText().toString().trim());
                v.setUbicación(coordenadasGPS.getText().toString().trim());
                databaseReference.child("Vocalia").child(v.getIdVocalia()).setValue(v);
                Toast.makeText(this, "Grabar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }

            case R.id.icon_delete:{
                Vocalia v = new Vocalia();
                v.setIdVocalia(vocaliaSelected.getIdVocalia());
                databaseReference.child("Vocalia").child(v.getIdVocalia()).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }

        return true;
    }

    private void limpiarCajas() {
        nombreArbitro.setText("");
        nombreVocal.setText("");

    }

    private void validacion() {
        String nombreEquipo = nombreArbitro.getText().toString();
        String fechaFundacion = nombreVocal.getText().toString();

        if(nombreEquipo.equals("")){
            nombreArbitro.setError("Requerid");
        }
        else if(fechaFundacion.equals("")){
            nombreVocal.setError("Requerid");
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            mProgressDialog.setTitle("Subiendo...");
            mProgressDialog.setMessage("Subiendo foto a FireBase");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filepath = mStorage.child("fotos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Uri desgargarFoto = taskSnapshot.getDownloadUrl();

                    Glide.with(VVocalia.this).load(desgargarFoto).fitCenter().centerCrop().into(mImageView);

                    Toast.makeText(VVocalia.this, "Imágen guardada exitosamente", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
