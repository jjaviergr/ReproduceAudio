package com.example.pc.reproduce;

//Antes de usar esta aplicación es necesario descargarse un fichero de música MP3 con el navegador.


import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends ListActivity {
    private List<String> listaNombresArchivos;
    private List<String> listaRutasArchivos;
    private ArrayAdapter<String> adaptador;
    private String directorioRaiz;
    private TextView carpetaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carpetaActual = (TextView) findViewById(R.id.rutaActual);

        //directorioRaiz = Environment.getExternalStorageDirectory().getPath();
       directorioRaiz=  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

        verArchivosDirectorio(directorioRaiz);
        Toast.makeText(this, directorioRaiz, Toast.LENGTH_LONG).show();
    }

    private void verArchivosDirectorio(String rutaDirectorio) {
        carpetaActual.setText("Estas en: " + rutaDirectorio);
        listaNombresArchivos = new ArrayList<String>();
        listaRutasArchivos = new ArrayList<String>();
        File directorioActual = new File(rutaDirectorio);
        try
        {


            File[] listaArchivos = directorioActual.listFiles();

            int x = 0;

            // Si no es nuestro directorio raiz creamos un elemento que nos
            // permita volver al directorio padre del directorio actual
            if (!rutaDirectorio.equals(directorioRaiz))
            {
                listaNombresArchivos.add("../");
                listaRutasArchivos.add(directorioActual.getParent());
                x = 1;
            }

            // Almacenamos las rutas de todos los archivos y carpetas del directorio
            for (File archivo : listaArchivos) {
                listaRutasArchivos.add(archivo.getPath());
            }

            // Ordenamos la lista de archivos para que se muestren en orden alfabetico
            Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);


            // Recorredos la lista de archivos ordenada para crear la lista de los nombres
            // de los archivos que mostraremos en el listView
            for (int i = x; i < listaRutasArchivos.size(); i++)
            {
                File archivo =new File(listaRutasArchivos.get(i));
                if (archivo.isFile())
                {
                    listaNombresArchivos.add(archivo.getName());
                }
                else
                {
                    listaNombresArchivos.add("/" + archivo.getName());
                }
            }

            // Si no hay ningun archivo en el directorio lo indicamos
            if (listaArchivos.length < 1)
            {
                listaNombresArchivos.add("No hay ningun archivo");
                listaRutasArchivos.add(rutaDirectorio);
            }


            // Creamos el adaptador y le asignamos la lista de los nombres de los
            // archivos y el layout para los elementos de la lista
            adaptador = new ArrayAdapter<String>(this,R.layout.text_view_lista_archivos, listaNombresArchivos);
            setListAdapter(adaptador);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, e.toString(),  Toast.LENGTH_LONG).show();
        }
    }


    protected void onListItemClick(ListView l, View v, int position, long id) {

        // Obtenemos la ruta del archivo en el que hemos hecho click en el
        // listView
        File archivo = null;
        try {
            archivo = new File(listaRutasArchivos.get(position));


            // Si es un archivo se muestra un Toast con su nombre y si es un directorio
            // se cargan los archivos que contiene en el listView
            if (archivo.isFile())
            {
                Toast.makeText(this,
                        "Has seleccionado el archivo: " + archivo.getAbsolutePath(),
                        Toast.LENGTH_LONG).show();


                Intent intent = new Intent(Intent.ACTION_VIEW);
                ///Toast.makeText(this, archivo.getAbsolutePath(),  Toast.LENGTH_LONG).show();
                intent.setData(Uri.parse("file:/" + archivo.getAbsolutePath()));
                intent.setDataAndType(Uri.parse("file:/" + archivo.getAbsolutePath()), "audio/mp3");
                startActivity(intent);


            } else {
                // Si no es un directorio mostramos todos los archivos que contiene
                verArchivosDirectorio(listaRutasArchivos.get(position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
