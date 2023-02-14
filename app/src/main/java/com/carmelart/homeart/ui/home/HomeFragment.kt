package com.carmelart.homeart.ui.home

import androidx.lifecycle.Observer
import android.os.StrictMode
//import android.arch.lifecycle
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.carmelart.homeart.R
import com.carmelart.homeart.database.DataDb

import com.carmelart.homeart.databinding.FragmentHomeBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dataLectura: DataEntity
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val timeout = 1000
    private val token = "fe5g8e2a5f4e85d2e85a7c5"

    lateinit var dbController: DataDb

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTOS
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // PEDIR LECTURA
        this.bindingManagement()

        // ACTUALIZAR DATA
        dbController = Room.databaseBuilder(
            requireContext(),
            DataDb::class.java,
            "HomeArt_DB",
        ).allowMainThreadQueries().build()

        this.dataLectura = dbController.DataDAO().getData()

        return root
    }

    private fun sendDataToServer(message: String) {
        try {
            var address = "172.20.10.12"    // Actualizar de vez en cuando
            var port = 80

            // Se necesita de un HOST y un PORT, se conecta el SERVERPOCKET al puerto 7777
            val socket = Socket()
            socket.soTimeout = timeout
            socket.connect(InetSocketAddress(address, port), timeout)
            println("CONECTADO")

            // Obtiene el flujo de salida del SOCKET
            val outputStream = socket.getOutputStream()

            // Crea un flujo de salida para sacar los datos
            val dataOutputStream = DataOutputStream(outputStream)
            println("Enviando cadena de datos por el ServerSocket")

            // Escribe el MENSAJE que se quiere enviar
            dataOutputStream.writeUTF(message)
            dataOutputStream.flush() // Envía el mensaje
            dataOutputStream.close() // Cierra el final del flujo de salida cuando se termina

            println("Cerrando socket")
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                getActivity(), "Servidor caído",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindingManagement() {

        object : CountDownTimer(86400000, 60000) { // Durante 1 día, cada minuto

            override fun onTick(millisUntilFinished: Long) {

                dataLectura.tLectura = 1
                dbController.DataDAO().updateData(dataLectura)
                generateDataStringAndSend(dataLectura)

                /*Toast.makeText(
                    getActivity(),"Pidiendo datos",
                    Toast.LENGTH_LONG
                ).show()*/
            }
            override fun onFinish() {
            }
        }.start()
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

    private fun generateDataStringAndSend(data: DataEntity) {
        var vHome: MutableList<String> = mutableListOf()

        // Todos los datos de Tiempo en el orden deseado

        vHome.add(data.tLectura.toString())

        sendDataToServer("H;$vHome;$token\n") // H[..., ..., ...] + token
    }
}