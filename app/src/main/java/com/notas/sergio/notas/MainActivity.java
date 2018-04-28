package com.notas.sergio.notas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    private ListView elegirNotas;
    private String [] archivos;
    private String [] archivosNota;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        elegirNotas = (ListView) findViewById(R.id.elegirNotas);
        elegirNotas.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                i.putExtra("fichero", elegirNotas.getItemAtPosition(position).toString());
                startActivity(i);
            }
        });
        elegirNotas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                dialogoBorrar(position).show();
                return true;
            }
        });

    }
    @Override
    protected  void onResume()
    {
        super.onResume();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        archivos = fileList();
        archivosNota = new String[archivos.length + 1];
        archivosNota[0] = "new";
        int j = 1;
        for(int i = 0; i < archivos.length; i++)
        {
            archivosNota[j] = archivos[i];
            archivosNota[j] = archivosNota[j].substring(0,archivosNota[j].length() - 4);
            j++;
        }
        int iAR = 1;
        int jAR = archivosNota.length - 1;
        while(iAR < jAR)
        {
            String aux = archivosNota[iAR];
            archivosNota[iAR] = archivosNota[jAR];
            archivosNota[jAR] = aux;
            iAR++;
            jAR--;
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, archivosNota);
        i = new Intent(this, NotasActivity.class);
        elegirNotas.setAdapter(adapter);

    }

    public AlertDialog dialogoBorrar(final int pos)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete")
                .setMessage("Delete this" + archivosNota[pos] + "?")

                .setPositiveButton("Delete",
                       new DialogInterface.OnClickListener()
                       {
                           @Override
                           public void onClick(DialogInterface dialog, int d)
                           {
                               String ficheroBorrar = archivosNota[pos] + ".txt";
                               File dir = getFilesDir();
                               File file = new File(dir, ficheroBorrar);
                               boolean borrado = file.delete();
                               if(borrado)
                               {
                                   onResume();
                                   Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
                               }
                           }
                       })
        .setNegativeButton("Cancel",
            new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int d)
                {
                    Toast.makeText(getApplicationContext(),"Not eliminado", Toast.LENGTH_SHORT).show();
                }
            });
        return builder.create();
    }
}
