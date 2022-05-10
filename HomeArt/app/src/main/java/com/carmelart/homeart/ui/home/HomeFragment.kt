package com.carmelart.homeart.ui.home


import android.arch.lifecycle.Observer
//import android.arch.lifecycle
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.carmelart.homeart.R
import com.carmelart.homeart.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttExterior.setOnClickListener {
            findNavController().navigate(R.id.nav_exterior)
        }

        binding.buttCocina.setOnClickListener {
            findNavController().navigate(R.id.nav_cocina)
        }

        binding.buttSalon.setOnClickListener {
            findNavController().navigate(R.id.nav_salon)
        }

        binding.buttBano.setOnClickListener {
            findNavController().navigate(R.id.nav_bano)
        }

        binding.buttDormitorio.setOnClickListener {
            findNavController().navigate(R.id.nav_dormitorio)
        }

        binding.buttOficina.setOnClickListener {
            findNavController().navigate(R.id.nav_oficina)
        }

        binding.buttGaraje.setOnClickListener {
            findNavController().navigate(R.id.nav_garaje)
        }

        binding.buttHuerto.setOnClickListener {
            findNavController().navigate(R.id.nav_huerto)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}