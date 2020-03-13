package com.example.biowinoficial;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Utiles extends AppCompatActivity {
    //agregar las variables que se usaran en la actividad
    private int libreta = 10, pluma = 5, lapices = 3, hojas = 15;
    private EditText edittext1, edittext2, edittext3, edittext4;
    private Button bt1, bt2, bt3, bt4;
    private Button btnEnviar;
    private ProgressDialog progressDialog;
    private TextView ptsdisp;
    //VARIABLES DE FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utiles);

        //encontrar los botones, edittext mediante el ID de cada uno
        edittext1 = (EditText) findViewById(R.id.editText3);
        edittext2 = (EditText) findViewById(R.id.editText4);
        edittext3 = (EditText) findViewById(R.id.editText5);
        edittext4 = (EditText) findViewById(R.id.editText6);
        ptsdisp = (TextView) findViewById(R.id.pdis176);
        bt1 = (Button) findViewById(R.id.button6);
        bt2 = (Button) findViewById(R.id.button9);
        bt3 = (Button) findViewById(R.id.button7);
        bt4 = (Button) findViewById(R.id.button8);

        //DIALOGO
        progressDialog = new ProgressDialog(this);

        //BOTON ENVIAR
        btnEnviar = (Button) findViewById(R.id.btnEnviar);

        //INSTANCIAR LOS METODOS DE FIREBASE
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Codigo para guardar los valores, el cual llama a otra funcion
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
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
                ptsdisp.setText(texto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Utiles.this, "Error al buscar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //AGREGAR LA CANTIDAD DE UTILES SEGUN EL USUARIO CONECTADO
    private void guardar() {
        //CAMBIAR EL VALOR POR UN STRING
        String et1 = edittext1.getText().toString();
        String et2 = edittext2.getText().toString();
        String et3 = edittext3.getText().toString();
        String et4 = edittext4.getText().toString();
        String pts = ptsdisp.getText().toString();

        //DIALOGO QUE SE ESTA REALIZANDO LA ACCION
        progressDialog.setMessage("Enviando cantidades de artículos...");
        progressDialog.show();

        //CREAR EL NUEVO HASH MAP A ENTRAR EN LA BASE DE DATOS
        Map<String, Object> mapita = new HashMap<>();
        mapita.put("libreta", et1);
        mapita.put("pluma", et2);
        mapita.put("lapices", et3);
        mapita.put("hojas", et4);
        mapita.put("puntos", pts);

        //PEDIR EL UID DEL USUARIO CONECTADO
        String id = mAuth.getCurrentUser().getUid();

        //ENVIAR MEDIANTE LA BASE DE DATOS Y EL EVENTO
        mDatabase.child("Users").child(id).updateChildren(mapita).isSuccessful();
        Toast.makeText(Utiles.this, "Datos enviados y guardados, ", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        /*
        mDatabase.child("Users").child(id).setValue(mapita).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                } else {
                    Toast.makeText(Utiles.this, "Error al enviar los datos", Toast.LENGTH_SHORT).show();
                }

            }
        });
        */
    }

    //Cerrar sesion del usuario
    private void cerrarSesion() {
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //Crear el menu flotante
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*Crear las opciones del menu flotante de manera que el usuario pueda elegir una opcion */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Opcion de inicio
        if(id==R.id.inicio){
            startActivity(new Intent(this, Principal.class));
        }

        //Opcion que te envia a Google Maps mediante las coordenadas
        if(id==R.id.centros176){
            Uri gmmIntentUri = Uri.parse("geo:25.691785,-100.3191684");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if(mapIntent.resolveActivity(getPackageManager())!= null){
                startActivity(mapIntent);
            }
        }

        //Actualiza toda la actividad
        if(id==R.id.utiles){
            return true;
        }

        //Opcion que envia a la actividad de AcercaDe
        if(id==R.id.acerca){
            startActivity(new Intent(this, AcercaDe.class));
        }

        //Cerrar la sesion del usuario logueado ahora mismo
        if(id==R.id.logOut){
            cerrarSesion();
        }

        //Regresar si no se uso ninguna opcion
        return super.onOptionsItemSelected(item);
    }

    //Opcion que SUMA un al editText y RESTA a los puntos disponibles
    public void suma1(View View){
        String et1 = edittext1.getText().toString();
        String pts = ptsdisp.getText().toString();
        int num1 = Integer.parseInt(et1);
        int ptsd = Integer.parseInt(pts);
        if (libreta > ptsd){
            Toast.makeText(getApplicationContext(), "No tienes suficientes puntos", Toast.LENGTH_SHORT).show();
        } else {
            int s = ptsd - libreta;
            int m = num1 + 1;
            String resultado = String.valueOf(s);
            String cont = String.valueOf(m);
            ptsdisp.setText(resultado);
            edittext1.setText(cont);
            bt1.setEnabled(true);
        }
    }

    //Opcion que SUMA un al editText y RESTA a los puntos disponibles
    public void suma2(View View){
        String et2 = edittext2.getText().toString();
        String pts = ptsdisp.getText().toString();
        int num2 = Integer.parseInt(et2);
        int ptsd = Integer.parseInt(pts);
        if (pluma > ptsd){
            Toast.makeText(getApplicationContext(), "No tienes suficientes puntos", Toast.LENGTH_SHORT).show();
        } else {
            int s1 = ptsd - pluma;
            int m1 = num2 + 1;
            String resultado1 = String.valueOf(s1);
            String cont1 = String.valueOf(m1);
            ptsdisp.setText(resultado1);
            edittext2.setText(cont1);
            bt2.setEnabled(true);
        }
    }

    //Opcion que SUMA un al editText y RESTA a los puntos disponibles
    public void suma3(View View){
        String et3 = edittext3.getText().toString();
        String pts = ptsdisp.getText().toString();
        int num3 = Integer.parseInt(et3);
        int ptsd = Integer.parseInt(pts);
        if (lapices > ptsd){
            Toast.makeText(getApplicationContext(), "No tienes suficientes puntos", Toast.LENGTH_SHORT).show();
        } else {
            int s2 = ptsd - lapices;
            int m2 = num3 + 1;
            String resultado2 = String.valueOf(s2);
            String cont2 = String.valueOf(m2);
            ptsdisp.setText(resultado2);
            edittext3.setText(cont2);
            bt3.setEnabled(true);
        }
    }

    //Opcion que SUMA un al editText y RESTA a los puntos disponibles
    public void suma4(View View){
        String et4 = edittext4.getText().toString();
        String pts = ptsdisp.getText().toString();
        int num4 = Integer.parseInt(et4);
        int ptsd = Integer.parseInt(pts);
        if (hojas > ptsd){
            Toast.makeText(getApplicationContext(), "No tienes suficientes puntos", Toast.LENGTH_SHORT).show();
        } else {
            int s3 = ptsd - hojas;
            int m3 = num4 + 1;
            String resultado3 = String.valueOf(s3);
            String cont3 = String.valueOf(m3);
            ptsdisp.setText(resultado3);
            edittext4.setText(cont3);
            bt4.setEnabled(true);
        }
    }

    //Opcion que RESTA un al editText y SUMA a los puntos disponibles
    public void resta1(View View){
        String et1 = edittext1.getText().toString();
        String pts = ptsdisp.getText().toString();
        int num5 = Integer.parseInt(et1);
        int ptsd = Integer.parseInt(pts);
        if (num5 == 0 || num5 == -1){
            bt1.setEnabled(false);
        } else {
            int s4 = ptsd + libreta;
            int m4 = num5 - 1;
            String resultado4 = String.valueOf(s4);
            String cont4 = String.valueOf(m4);
            ptsdisp.setText(resultado4);
            edittext1.setText(cont4);
        }
    }

    //Opcion que RESTA un al editText y SUMA a los puntos disponibles
    public void resta2(View View){
        String et2 = edittext2.getText().toString();
        String pts = ptsdisp.getText().toString();
        int num6 = Integer.parseInt(et2);
        int ptsd = Integer.parseInt(pts);
        if (num6 == 0 || num6 == -1){
            bt2.setEnabled(false);
        } else {
            int s5 = ptsd + pluma;
            int m5 = num6 - 1;
            String resultado5 = String.valueOf(s5);
            String cont5 = String.valueOf(m5);
            ptsdisp.setText(resultado5);
            edittext2.setText(cont5);
        }

    }

    //Opcion que RESTA un al editText y SUMA a los puntos disponibles
    public void resta3(View View){
        String et3 = edittext3.getText().toString();
        String pts = ptsdisp.getText().toString();
        int num7 = Integer.parseInt(et3);
        int ptsd = Integer.parseInt(pts);
        if (num7 == 0 || num7 == -1){
            bt3.setEnabled(false);
        } else {
            int s6 = ptsd + lapices;
            int m6 = num7 - 1;
            String resultado6 = String.valueOf(s6);
            String cont6 = String.valueOf(m6);
            ptsdisp.setText(resultado6);
            edittext3.setText(cont6);
        }
    }

    //Opcion que RESTA un al editText y SUMA a los puntos disponibles
    public void resta4(View View){
        String et4 = edittext4.getText().toString();
        String pts = ptsdisp.getText().toString();
        int num8 = Integer.parseInt(et4);
        int ptsd = Integer.parseInt(pts);
        if (num8 == 0 || num8 == -1){
            bt4.setEnabled(false);
        } else {
            int s7 = ptsd + hojas;
            int m7 = num8 - 1;
            String resultado7 = String.valueOf(s7);
            String cont7 = String.valueOf(m7);
            ptsdisp.setText(resultado7);
            edittext4.setText(cont7);
        }
    }
}