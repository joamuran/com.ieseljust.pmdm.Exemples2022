package com.ieseljust.pmdm.incidenciesv2.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ieseljust.pmdm.incidenciesv2.R
import com.ieseljust.pmdm.incidenciesv2.databinding.FragmentMainBinding
import com.ieseljust.pmdm.incidenciesv2.view.dialogs.MyDialogFragment
import com.ieseljust.pmdm.incidenciesv2.viewmodel.IncidenciesViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class MainFragment : Fragment(), MyDialogFragment.OkOrCancelDialogable {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // Declaració del ViewModel
    private lateinit var viewModel: IncidenciesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instanciem el ViewModel mitjançant el ViewModelProvider.
        viewModel = ViewModelProvider(requireActivity())[IncidenciesViewModel::class.java]

        // Preparació del RecyclerView. Associació del LayoutManager
        binding.IncidenciesRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Preparem els observers en una funció a banda
        prepareObsevers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Mètode per preparar tots els observers de la vista
    fun prepareObsevers(){

        // Creeem un observador a l'adaptador per automatitzar
        // l'actualització de l'adaptador del recyclerView.
        viewModel.adaptador.observe(viewLifecycleOwner) {
            binding.IncidenciesRecyclerView.adapter = it
        }

        // Observador de la llista de contactes
        // Necessari per actualitzar l'adaptador
        viewModel.incidenciaList.observe(viewLifecycleOwner) { contactes ->
            contactes.let {
                if (viewModel.deletedPos.value == null) {
                    viewModel.adaptador.value?.notifyDataSetChanged()
                } else {
                    viewModel.adaptador.value?.notifyItemRemoved(viewModel.deletedPos.value!!)
                    viewModel.deletedPos.value = null
                }
            }
        }

        // Observador del contacte clicat
        viewModel.incidenciaClicked.observe(viewLifecycleOwner) { incidencia ->
            incidencia?.let {
                // Ressetegem el valor del incidenciaClicked
                viewModel.incidenciaClicked.value = null
                // I llancem la navegació cap al fragment d'edició
                findNavController().navigate(R.id.action_MainFragment_to_EditFragment)
            }
        }

        // Observador del contacte clicat per eliminar (clic llarg)
        viewModel.incidenciaLongClicked.observe(viewLifecycleOwner) { incidencia ->
            incidencia?.let {
                // Ressetegem el valor
                viewModel.incidenciaLongClicked.value = null

                // Llancem l'acció corresponent al click llarg; mostrar
                // el diàleg de confirmació d'eliminació de l'element.
                val myDialog = MyDialogFragment(
                    resources.getString(R.string.askRemoveTitle),
                    resources.getString(R.string.askRemove) +"'"+ incidencia.assumpte + "' ?"
                )
                myDialog.show(parentFragmentManager, "MyDialog")
            }
        }



    } // End Prepare Observers


    // Implementació de la interfície per al diàleg

    override fun onPositiveClick() {
        // Si es prem a Acceptar en el diàleg eliminem l'item.

        // Obtenim el contacte a elimina a través del LiveData
        // contacteToRemove, i ressetegem immediatament el seu valor.
        val incidenciaToRemove = viewModel.incidenciaToRemove.value
        viewModel.incidenciaToRemove.value = null

        incidenciaToRemove?.also {
            // Invoquem al mètode del VoewModel per eliminar una incidència.
            viewModel.removeIncidencia(it)
        }
    }

    override fun onCancelClick() {
        Toast.makeText(
            requireActivity().applicationContext,
            R.string.ActionCancelled, Toast.LENGTH_SHORT
        ).show()
    }
}