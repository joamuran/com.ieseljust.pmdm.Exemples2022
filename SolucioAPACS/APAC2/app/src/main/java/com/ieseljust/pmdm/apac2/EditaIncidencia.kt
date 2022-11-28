package com.ieseljust.pmdm.apac2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.ieseljust.pmdm.apac2.databinding.ActivityEditaIncidenciaBinding

class EditaIncidencia : AppCompatActivity() {

    private lateinit var binding: ActivityEditaIncidenciaBinding

    private var idIncidenciaActual=-1

    // Imatge actual per defecte
    private var imatgeActual:Int=R.drawable.incidencia

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Unflem la classe de vinculació
        binding=ActivityEditaIncidenciaBinding.inflate(layoutInflater)

        // Establim el disseny de l'activitat
        setContentView(binding.root)

        // OBtenim la incidència de l'Intent
        val incidencia: Incidencia? = getIntent().getExtras()?.
        getSerializable("com.ieseljust.pmdm.apac2.Incidencia") as Incidencia?

        // Si la Intent porta una incidència, establim
        // els valors dels camps
        incidencia?.also {

            // Ens guardem l'identificador de la incidència
            idIncidenciaActual=it.id // it és la pròpia incidència

            // I l'identificador de la imatge
            imatgeActual=it.img

            //

            // Actualitzem les vistes de la interfície
            binding.imageView.setImageResource(it.img)
            binding.textTitleIncidencia.setText(it.assumpte)
            binding.textDescripcio.setText(it.descripcio)
            binding.textCarrer.setText(it.ubicacio)

            // Actualització de l'spinner
            // Recorrem els diferents valors d'aquest, i comparem
            // amb el valor guardat. Si coindiceix, deixem seleccionat el valor.

            for (i in 0..binding.SpinnerServei.adapter.count)
                if (binding.SpinnerServei.adapter.getItem(i).equals(it.servei)) {
                    binding.SpinnerServei.setSelection(i)
                    break
                }

            // Establim el valor del switch
            if (it.resolt) binding.switchResolt.isChecked=true

        }

        // Capturem el click sobre el botó de guardar:
        binding.buttonSave.setOnClickListener {
            guardaIncidencia()
        }
    }

    fun guardaIncidencia() {

        val assumpte = binding.textTitleIncidencia.text.toString()
        val descripcio = binding.textDescripcio.text.toString()
        val zona = binding.textCarrer.text.toString()
        val servei = binding.SpinnerServei.selectedItem.toString()
        val resolt = binding.switchResolt.isChecked



        if (idIncidenciaActual != -1) { // Si tenia ja un id i cal actualitzar...
            Incidencies.update(
                Incidencia(
                    idIncidenciaActual,
                    assumpte,
                    descripcio,
                    zona,
                    servei,
                    imatgeActual,
                    resolt
                )
            )
            Log.d("Debug", "Modifique ("+idIncidenciaActual+"):"+assumpte+descripcio);
        } else {
            // En cas contrari, caldrà afegir la nova incidència
            // Ens guardem l'ID que retorna com a id d'incidència actual (per no crear duplicats)
            idIncidenciaActual=Incidencies.add(assumpte, descripcio, zona, servei, imatgeActual, resolt)

            Log.d("Debug", "Cree nou ("+idIncidenciaActual+"):"+assumpte+descripcio);

        }

        Log.d("Debug", Incidencies.incidencies.toString())
        Snackbar.make(binding.root,
            getText(R.string.incidenciaSaved),
            Snackbar.LENGTH_LONG).setAction(getText(R.string.Close), {finish()}).show()


    }
}