package com.ieseljust.pmdm.apac2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ieseljust.pmdm.apac2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MyDialogFragment.OkOrCancelDialogable {

    private lateinit var binding: ActivityMainBinding

    private var incidenciaToRemove: Incidencia?=null


    // Callbacks per gestionar l'spinner
    private fun itemClicked(incidencia: Incidencia, v: View) {
        // Creem un nou Intent per a obrir l'activitat d'edició
        // proporcionant-li el contacte sobre el qual s'ha fet clic

        val intent = Intent(this, EditaIncidencia::class.java).apply{
            // I li proporcionem el contacte, ja que aquest és serializable
            putExtra("com.ieseljust.pmdm.apac2.Incidencia", incidencia)
        }
        startActivity(intent)
    }

    private fun itemLongClicked(incidencia: Incidencia, v: View):Boolean {
        incidenciaToRemove=incidencia
        val myDialog = MyDialogFragment(getString(R.string.askRemoveTitle), getString(R.string.askRemove)+" "+incidencia.assumpte+"?")
        myDialog.show(supportFragmentManager, "removeDialog")
        return true
    }

    override fun onStart() {
        super.onStart()


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // "Unflem" els contactes amb el contingut del JSON

        // Associem el LayoutManager
        binding.IncidenciesRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.IncidenciesRecyclerView.setHasFixedSize(true)

        binding.IncidenciesRecyclerView.adapter = AdaptadorIncidencies(
            { incidencia: Incidencia, v: View -> itemClicked(incidencia, v) },
            { incidencia: Incidencia, v: View -> itemLongClicked(incidencia, v) }
        )

        // Inicialitzem l'objecte Incidencies
        Incidencies.afigDadesInicials(applicationContext)

    }

    override fun onResume() {
        super.onResume()
        // Quan la vista torna a primer pla, redibuixem el RecyclerView
        // Per si es va haver-hi modificacions en el contingut.
        binding.IncidenciesRecyclerView.adapter?.notifyDataSetChanged()

    }

    override fun onPositiveClick() {
        // Si es prem a Acceptar en el diàleg
        // eliminem l'item.
        incidenciaToRemove?.also {
            val index=Incidencies.remove(it) // Recordem que aquest mètode ens tornava l'índex!

            // I actualitzem l'adaptador del RecyclerView
            // En lloc de DataSetChanged,
            // utilitzem el mètode notifyItemRemoved
            // indicant la posició eliminada. D'aquesta manera, s'aplica una
            // animació en l'esborrat.
            binding.IncidenciesRecyclerView.adapter?.notifyItemRemoved(index)
        }

    }

    override fun onCancelClick() {
        // Toast.makeText(applicationContext, R.string.ActionCancelled, Toast.LENGTH_SHORT).show()
    }


    /* Gestió del menú (només en aquest apantalla) */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_incidencies, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Determinem l'acció a realitzar segons l'id de l'item
        return when (item.itemId) {
            R.id.NewIssue -> { // Si es tria la primera opció
                val intent = Intent(this, EditaIncidencia::class.java)
                // Açò no caldria aci
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                // No afegim cap informació addicional a l'Intent

                startActivity(intent)
                true
            }
            // En cas que no coincidisca amb ningun id, invoquem al
            // mètode onOptionsItemSelected d'else
            else -> super.onOptionsItemSelected(item)
        }
    }


}