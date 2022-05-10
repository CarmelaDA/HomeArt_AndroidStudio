package com.carmelart.homeart.ui.tiempo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v4.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import com.carmelart.homeart.R
import com.carmelart.homeart.databinding.FragmentTiempoBinding

class TiempoFragment : Fragment() {

    private lateinit var tiempoViewModel: TiempoViewModel
    private var _binding: FragmentTiempoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tiempoViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(TiempoViewModel::class.java)

        _binding = FragmentTiempoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTiempoInterior
        tiempoViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // SWITCHES

        binding.switchVentiladorInt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentiladorInt.isChecked = isChecked
        }

        binding.switchCalefaccionInt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleCalefactorInt.isChecked = isChecked
        }

        binding.switchVentiladorExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentiladorExt.isChecked = isChecked
        }

        binding.switchCalefaccionExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleCalefactorExt.isChecked = isChecked
        }

        binding.switchRiegoHuerto.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleRiegoHuerto.isChecked = isChecked
        }


        // MODO MANUAL/AUTOMÁTICO

        binding.switchModo.setOnCheckedChangeListener { _, isChecked ->

            // Bloqueo de Switches
            binding.switchVentiladorInt.isEnabled = !isChecked
            binding.switchVentiladorExt.isEnabled = !isChecked
            binding.switchCalefaccionInt.isEnabled = !isChecked
            binding.switchCalefaccionExt.isEnabled = !isChecked
            binding.switchRiegoHuerto.isEnabled = !isChecked
        }



        // FIN onCreateView
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}