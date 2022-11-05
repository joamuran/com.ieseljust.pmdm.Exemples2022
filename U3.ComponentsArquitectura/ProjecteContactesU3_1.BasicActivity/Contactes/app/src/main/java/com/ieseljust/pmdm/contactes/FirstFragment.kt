package com.ieseljust.pmdm.contactes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ieseljust.pmdm.contactes.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), MyDialogFragment.OkOrCancelDialogable {

    private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Element a eliminat
    private var contacteToRemove:Contacte?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Fent proves", "onCreate")

        contacteToRemove=savedInstanceState?.getSerializable("com.ieseljust.pmdm.contactes.contacteToRemove") as Contacte?
        Log.d("Fent proves", "Recupere: "+contacteToRemove?.name)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("Fent proves", "Guarde: "+contacteToRemove?.name)
        outState.putSerializable("com.ieseljust.pmdm.contactes.contacteToRemove", contacteToRemove)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        Log.d("Fent proves", "onCreateView")

        // "Unflem" els contactes amb el contingut del JSON,
        // Fem ús de requiereActivity en lloc de this, per accedir a l'activitat.
        Contactes.inflate(requireActivity(), R.raw.contacts)

        // Associem el LayoutManager. En lloc de this, fem ús de nou
        // de requireActivity
        binding.ContactesRecyclerView.layoutManager= LinearLayoutManager(requireActivity())
        binding.ContactesRecyclerView.setHasFixedSize(true)

        // Assignem l'adaptador al RecyclerView, proporcionant-li
        // les dues funcions lambda que invocaràn els callbacks per
        // gestionar els clicks.

        binding.ContactesRecyclerView.adapter = AdaptadorContactes(
            { contacte: Contacte, v: View -> itemClicked(contacte, v) },
            { contacte: Contacte, v: View -> itemLongClicked(contacte, v) }
        )



      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun itemClicked(contacte: Contacte, v: View) {
        val bundle = bundleOf("contacte" to contacte)
        v.findNavController()
            .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
    }


    private fun itemLongClicked(contacte: Contacte, v: View):Boolean {
        contacteToRemove=contacte
        val myDialog = MyDialogFragment(getString(R.string.askRemoveTitle), getString(R.string.askRemove))
        //myDialog.show(requireActivity().supportFragmentManager, "removeDialog")
        myDialog.show(childFragmentManager, "removeDialog")
        return true
    }



    override fun onPositiveClick() {
        // Si es prem a Acceptar en el diàleg
        // eliminem l'item.
        contacteToRemove?.also {
            val index=Contactes.remove(it) // Recordem que aquest mètode ens tornava l'índex!

            // I actualitzem l'adaptador del RecyclerView
            // En lloc de DataSetChanged,
            // utilitzem el mètode notifyItemRemoved
            // indicant la posició eliminada. D'aquesta manera, s'aplica una
            // animació en l'esborrat.
            binding.ContactesRecyclerView.adapter?.notifyItemRemoved(index)
        }

    }

    override fun onCancelClick() {
        Toast.makeText(requireActivity().applicationContext, R.string.ActionCancelled, Toast.LENGTH_SHORT).show()
    }
}