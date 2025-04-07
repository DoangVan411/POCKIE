package com.example.pockie.presentation.ui.authentication.addcontacts

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.R
import com.example.pockie.databinding.FragmentContactsBinding
import com.example.pockie.domain.model.Contact
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDialog()

        init()

        binding.next.setOnClickListener {
            findNavController().navigate(
                ContactsFragmentDirections.actionContactsFragmentToSetUpFragment(),
                navOptions = NavOptions.Builder().setPopUpTo(R.id.contactsFragment, true).build()
            )
        }

    }

    private fun showDialog() {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.fragment_dialog_add_contacts, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.show()
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics
            ).toInt()
        )

        dialogView.findViewById<Button>(R.id.next).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun init() {
        val sampleList = listOf(
            Contact("A", "0"),
            Contact("B", "0"),
            Contact("C", "0")
        )
        val contactsAdapter = ContactsAdapter()
        binding.contactRecyclerview.adapter = contactsAdapter
        binding.contactRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        contactsAdapter.submitList(sampleList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}