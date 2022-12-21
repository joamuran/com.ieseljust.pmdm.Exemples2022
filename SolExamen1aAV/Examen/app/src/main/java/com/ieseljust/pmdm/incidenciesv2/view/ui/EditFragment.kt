package com.ieseljust.pmdm.incidenciesv2.view.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ieseljust.pmdm.incidenciesv2.R
import com.ieseljust.pmdm.incidenciesv2.databinding.FragmentEditIncidenciaBinding
import com.ieseljust.pmdm.incidenciesv2.model.db.Incidencia
import com.ieseljust.pmdm.incidenciesv2.viewmodel.IncidenciesViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditFragment : Fragment() {

    private var _binding: FragmentEditIncidenciaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Referència al ViewModel
    private lateinit var viewModel: IncidenciesViewModel

    // Variables temporals
    var tmpImageUri=""  // Guardarà la URI de la imatge actual

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditIncidenciaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  Instanciem el ViewModel, utilitzant la classe ViewModelProvider
        viewModel = ViewModelProvider(requireActivity())[IncidenciesViewModel::class.java]

        // Imatge per defecte
        binding.imageView.setImageResource(R.drawable.incidencia)

        // Per defecte deixem com a no editable el RatingBar
        binding.ratingBar.setIsIndicator(true)

        binding.switchResolt.setOnCheckedChangeListener { _, isChecked ->
            binding.ratingBar.setIsIndicator(!isChecked)

            if (isChecked) binding.ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.teal_200)))
            else binding.ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), androidx.constraintlayout.widget.R.color.material_grey_300)))
        }


        // Gestió d'esdeveniments
        binding.buttonSave.setOnClickListener {
            // Creem un objecte de tipus Incidència, i invoquem el mètode
            // de guardar del ViewModel

            val incidencia= Incidencia(
                assumpte=binding.textTitleIncidencia.text.toString(),
                descripcio=binding.textDescripcio.text.toString(),
                ubicacio=binding.textCarrer.text.toString(),
                servei=binding.SpinnerServei.selectedItem.toString(),
                img=tmpImageUri,
                resolt=binding.switchResolt.isChecked,
                valoracio=binding.ratingBar.rating // NOU

            )

            viewModel.storeOrUpdateIncidencia(incidencia)

        }

        // Preparem els observers en una funció a banda
        prepareObsevers()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // Mètode per preparar tots els observers de la vista
    private fun prepareObsevers() {

        viewModel.incidenciaEnEdicio.observe(viewLifecycleOwner) {

            Log.d("[ Debug ]", viewModel.incidenciaEnEdicio.value.toString())
            // Quan es fa click sobre una targeta per editar-la, s'actualitza el
            // LiveData incidenciaEnEdicio, de manera que aquest fragment és notificat
            // d'aquesta actualització, i per tant, actualitza la interfície.

            it?.let {
                // actualitzem la imatge temporal
                tmpImageUri=it.img?:""

                val imgid=when(it.img) {
                    "" -> resources.getIdentifier(
                        "incidencia",
                        "drawable",
                        context?.packageName
                    )
                    else -> context?.resources?.getIdentifier(
                        it.img,
                        "drawable",
                        context?.packageName
                    )
                }

                // Actualitzem les vistes de la interfície
                binding.imageView.setImageResource(imgid!!)
                binding.textTitleIncidencia.setText(it.assumpte)
                binding.textTitleIncidencia.doOnTextChanged{ text, _, _, _ ->
                    viewModel.incidenciaEnEdicio.value?.assumpte=text.toString()
                }


                binding.textDescripcio.setText(it.descripcio)
                binding.textDescripcio.doOnTextChanged{ text, _, _, _ ->
                    viewModel.incidenciaEnEdicio.value?.descripcio=text.toString()
                }

                binding.textCarrer.setText(it.ubicacio)
                binding.textCarrer.doOnTextChanged{ text, _, _, _ ->
                    viewModel.incidenciaEnEdicio.value?.ubicacio=text.toString()
                }

                // Actualització de l'spinner
                // Recorrem els diferents valors d'aquest, i comparem
                // amb el valor guardat. Si coindiceix, deixem seleccionat el valor.

                val itemselected:String?
                for (i in 0..binding.SpinnerServei.adapter.count){
                    Log.d("Debug", binding.SpinnerServei.adapter.getItem(i).toString())
                    if (binding.SpinnerServei.adapter.getItem(i).equals(it.servei)) {
                        binding.SpinnerServei.setSelection(i)
                        itemselected=it.servei
                        break
                    }
                }

                // Per detectar els canvis a l'spinner cal un objecte que implemente
                // la interfície AdapterView.OnItemSelectedListener

                binding.SpinnerServei.onItemSelectedListener= object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        Log.d("DebugME", selectedItem)
                        viewModel.incidenciaEnEdicio.value?.servei=selectedItem
                    } // to close the onItemSelected
                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

                // Establim el valor del switch
                if (it.resolt?:false) binding.switchResolt.isChecked=true

                // I quan aquest canvie, modifiquem la incidència en edició
                binding.switchResolt.setOnCheckedChangeListener { _, isChecked ->
                    Log.d("*****", "Actualitzant incidencia en edicio")
                    viewModel.incidenciaEnEdicio.value?.resolt=isChecked

                    // Extres per al RatingBar
                    if (isChecked) {                           // Si la indicència està resolta
                        binding.ratingBar.setIsIndicator(false)         // Habilitem que es puga valorar

                        // Actualitzem el valor del ViewModel
                        viewModel.incidenciaEnEdicio.value?.valoracio=binding.ratingBar.rating

                        // Canviem el color al blau normal
                        binding.ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.teal_200)))
                    } else {
                        binding.ratingBar.setIsIndicator(true)
                        // Canviem el color a gris
                        binding.ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), androidx.constraintlayout.widget.R.color.material_grey_300)))
                    }
                }

                // ratingBar
                if (it.resolt ?: false) {                           // Si la indicència està resolta
                    binding.ratingBar.setIsIndicator(false)         // Habilitem que es puga valorar
                    binding.ratingBar.rating=it.valoracio?:0.0F    // Valor per defecte
                } else binding.ratingBar.setIsIndicator(true)      // En cas contrari inhabilitem que es puga escriure

                // Actualització del value quan es modifique el ragingBar
                binding.ratingBar.setOnRatingBarChangeListener { _, v, b ->
                    viewModel.incidenciaEnEdicio.value?.valoracio = v
                }


            }
        }

        // Observadors per saber quan s'ha guardat o modificat una incidència

        viewModel.incidenciaSaved.observe(viewLifecycleOwner) { saved ->
            saved?.let {
                // Netegem el valor per a no entrar en un bucle infinit
                viewModel.incidenciaSaved.value=null
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.incidenciaSaved),
                    Snackbar.LENGTH_LONG
                ).setAction(resources.getString(android.R.string.ok)) {
                    findNavController().navigateUp()
                }.show()
            }
        }

        viewModel.incidenciaUpdated.observe(viewLifecycleOwner) { updated ->
            updated?.let {
                // Netegem el valor per a no entrar en un bucle infinit
                viewModel.incidenciaUpdated.value=null
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.incidenciaModified),
                    Snackbar.LENGTH_LONG
                ).setAction(resources.getString(android.R.string.ok)) {
                    findNavController().navigateUp()
                }.show()
            }
        }
    }

}