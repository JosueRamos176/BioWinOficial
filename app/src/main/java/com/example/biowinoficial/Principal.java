package com.example.biowinoficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Principal extends AppCompatActivity {
    //DECLARACION DE LAS VARIABLES
    private TextView pdis;
    private TextView info;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //INSTANCIARLOS
        pdis = (TextView)findViewById(R.id.pdis);
        //VARIABLES DE FIREBASE
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //user = mAuth.getCurrentUser();
        //usersRef = db.getReference("Users").child(mAuth.getUid());
        //mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
        String id = mAuth.getCurrentUser().getUid();
    }

    //FUNCION PARA CERRAR LA SESION
    private void cerrarSesion() {
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //Funcion para ir a activity utiles
    public void utiles(View view){
        startActivity(new Intent(this, Utiles.class));
    }

    //Funcion para ir a activity centros
    public void centros(View view) {
        Uri gmmIntentUri1 = Uri.parse("geo:25.691785,-100.3191684");
        Intent mapIntent1 = new Intent(Intent.ACTION_VIEW, gmmIntentUri1);
        mapIntent1.setPackage("com.google.android.apps.maps");
        if (mapIntent1.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent1);
        }
    }

    //Funcion para ir a activity acerca de
    public void acercade(View view){
            startActivity(new Intent(this, AcercaDe.class));
    }

    //CREAR EL MENU FLOTANTE
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //OPCIONES DE MENU FLOTANTE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //ACTUALIZAR LA PANTALLA PRINCIPAL
        if(id==R.id.inicio){
            return true;
        }

        //MANDAR A GOOGLE MAPS
        if(id==R.id.centros176){
            Uri gmmIntentUri = Uri.parse("geo:25.691785,-100.3191684");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if(mapIntent.resolveActivity(getPackageManager())!= null){
                startActivity(mapIntent);
            }
        }

        //MANDAR A LA ACTIVITY UTILES
        if(id==R.id.utiles){
            startActivity(new Intent(this, Utiles.class));
        }

        //MANDAR A LA ACTIVITY ACERCA DE
        if(id==R.id.acerca){
            startActivity(new Intent(this, AcercaDe.class));
        }

        //CERRAR LA SESION
        if(id==R.id.logOut){
            cerrarSesion();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //ESTE METODO ES PARA EXTRAER INFORMACION DE LOS PUNTOS SEGUN EL USUARIO
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).child("puntos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String texto = dataSnapshot.getValue().toString();
                pdis.setText(texto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Principal.this, "Error al buscar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}