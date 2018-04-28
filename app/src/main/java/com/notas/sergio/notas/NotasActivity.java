package com.notas.sergio.notas;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NotasActivity extends AppCompatActivity
{
    private EditText nombreNota;
    private EditText nota;
    private static int numNuevos = 0;
    private static int numNotasVacias = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        AdView mAdView2 = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);

        nombreNota = (EditText) findViewById(R.id.nombreNota);
        nota = (EditText) findViewById(R.id.nota);
        String [] archivos = fileList();
        Bundle bundle = getIntent().getExtras();
        String datoNombre = bundle.getString("fichero");
        nombreNota.setText(datoNombre);
        datoNombre = bundle.getString("fichero") + ".txt";

        if(existe(archivos,datoNombre))
        {
            try
            {
                InputStreamReader archivo = new InputStreamReader(openFileInput(datoNombre));
                BufferedReader br = new BufferedReader(archivo);

                String linea = br.readLine();
                String todo = "";
                while(linea != null)
                {
                    todo += linea + "\n";
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
                nota.setText(todo);
            }
            catch (IOException e)
            {

            }
        }
    }
    private boolean existe(String [] archivos, String archBuscar)
    {
        for(int i = 0; i < archivos.length; i++)
        {
            if(archBuscar.equals(archivos[i]))
            {
                return true;
            }
        }
        return false;
    }
    public void guardar(View view)
    {
        String nombre = nombreNota.getText().toString() + ".txt";
        if(nombre.equals("new.txt"))
        {
            nombre = nombreNota.getText().toString() + numNuevos + ".txt";
            numNuevos++;
        }
        if(nombre.equals(".txt"))
        {
            nombre = "note" + numNotasVacias + ".txt";
            numNotasVacias++;
        }
        nombre = nombre.replace('/','-');
        try
        {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(nombre,Activity.MODE_PRIVATE));
            archivo.write(nota.getText().toString());
            archivo.flush();
            archivo.close();
            Toast.makeText(this,"saved",Toast.LENGTH_SHORT).show();
            finish();
        }
        catch(IOException e)
        {

        }
    }
}
