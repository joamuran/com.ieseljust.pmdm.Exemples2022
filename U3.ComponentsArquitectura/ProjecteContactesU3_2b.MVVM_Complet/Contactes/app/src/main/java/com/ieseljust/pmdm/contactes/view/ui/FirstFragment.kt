package com.ieseljust.pmdm.contactes.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ieseljust.pmdm.contactes.R
import com.ieseljust.pmdm.contactes.databinding.FragmentFirstBinding
import com.ieseljust.pmdm.contactes.view.dialogs.MyDialogFragment
import com.ieseljust.pmdm.contactes.viewmodel.AppContactesViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), MyDialogFragment.OkOrCancelDialogable {

    private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Adaptació a MVVM: Eliminem la línia:
    //private var contacteToRemove: Contacte?=null
    // ja que aquest contacte a eliminar el gestionarem des
    // del ViewModel a través de LiveData

    // Adaptació a MVVM: Definim una instància del ViewModel com a lateinit
    private lateinit var viewModel: AppContactesViewModel

    // Adaptació a MVVM: Eliminem els mètodes onCreate i
    // onSaveInstanceState, ja que ara no són necessaris

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Injectem la interfície a través del View Binding
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        // I la retornem
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // En primer lloc, anem a instanciar el ViewModel, mitjançant la classe
        // ViewModelProvider. Aci es on la vista "s'enganxa" al ViewModel.
        // Compte que com a owner hem d'afegir requireActivity() en lloc de
        // this, per a que l'àmbit del ViewModel siga ara el de l'activitat,
        // de manera que es puga compartir entre els fragments.
        viewModel = ViewModelProvider(requireActivity())[AppContactesViewModel::class.java]

        // Preparació del RecyclerView:

        // 1. Associem el LayoutManager
        binding.ContactesRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // 2. Creeem un observador i el subscrivim al LiveData adaptador
        //    definit al ViewModel. Així, quan es produisquen canvis al ViewModel,
        //    es reflexaran en l'adaptador del RecyclerView
        viewModel.adaptador.observe(viewLifecycleOwner) {
            binding.ContactesRecyclerView.adapter = it
        }

        // 3. Creem un altre observer, i el subscrivim als canvis que
        //    es produisquen a l'objecte contacteClicked del ViewModel.
        //    En aquest cas, per crear l'observer, es rep el contacte
        //    sobre el que hem fet click.

        viewModel.contacteClicked.observe(viewLifecycleOwner) { contacte ->
            contacte?.let {
                // Ressetegem el valor del contacteClicked
                viewModel.contacteClicked.value = null

                // I llancem la navegación cap al fragment d'edició
                //val bundle = bundleOf("contacte" to contacte)
                viewModel.setContacteActual(contacte)
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

                // Compartint dades amb el segon fragment a través del MVVM

            }
        }
        // 4. Creem un tercer observer i el subscrivim als canvis que es
        //    produisquen a l'objecte contacteLongClicked del ViewModel,
        //    a l'igual que en el cas anterior.
        viewModel.contacteLongClicked.observe(viewLifecycleOwner) { contacte ->
            contacte?.let {
                // Ressetegem el valor
                viewModel.contacteLongClicked.value = null

                // Llancem l'acció corresponent al click llarg; mostrar
                // el diàleg de confirmació d'eliminació de l'element.
                val myDialog = MyDialogFragment(
                    resources.getString(R.string.askRemoveTitle),
                    resources.getString(R.string.askRemove) + contacte.name + "?"
                )
                myDialog.show(parentFragmentManager, "MyDialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Els mètodes itemClicked i itemLongCLicked s'han eliminat d'aci,
    // ja que ara és el viewModel qui detecta els clicks a través del ViewHolder,
    // ides d'aci es gestionen amb els observers definits més amunt.

    // Com que la vista segueix implementant la interfície
    // MyDialogFragment.OkOrCancelDialogable, cal implementar
    // els mètodes onPositiveClick quan fem click
    // en acceptar al diàleg, i onCancelClick.

    override fun onPositiveClick() {
        // Si es prem a Acceptar en el diàleg eliminem l'item.

        // Obtenim el contacte a elimina a través del LiveData
        // contacteToRemove, i ressetegem immediatament el seu valor.
        val contacteToRemove=viewModel.contacteToRemove.value
        viewModel.contacteToRemove.value = null

        // Si existeix aquest contacte...
        contacteToRemove?.also {
            // En lloc d'eliminar el contacte directament, ho fem
            // a través del mètode removeContact del propi ViewModel,
            // que serà qui accedisca al model a través del
            // repositori per eliminar el contacte.

            viewModel.removeContact(it)
        }
    }

    override fun onCancelClick() {
        Toast.makeText(requireActivity().applicationContext,
            R.string.ActionCancelled, Toast.LENGTH_SHORT).show()
    }
}