package com.carmelart.homeart.ui.ventanas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.carmelart.homeart.R
import com.carmelart.homeart.databinding.FragmentVentanasBinding

class VentanasFragment : Fragment() {

    private lateinit var ventanasViewModel: VentanasViewModel
    private var _binding: FragmentVentanasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ventanasViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(VentanasViewModel::class.java)

        _binding = FragmentVentanasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textParcela
        ventanasViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // SWITCHES

        binding.switchPuertaParcela.setOnCheckedChangeListener { _, isChecked ->
            binding.togglePuertaParcela.isChecked = isChecked
        }

        binding.switchPuertaGaraje.setOnCheckedChangeListener { _, isChecked ->
            binding.togglePuertaGaraje.isChecked = isChecked
        }

        binding.switchVentanaSalon.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentanaSalon.isChecked = isChecked
        }

        binding.switchVentanaDormitorio.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentanaDormitorio.isChecked = isChecked
        }

        binding.switchVentanaOficina.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentanaOficina.isChecked = isChecked
        }

        // FIN onCreateView
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}