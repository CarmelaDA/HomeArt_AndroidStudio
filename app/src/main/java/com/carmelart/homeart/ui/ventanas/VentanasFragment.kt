package com.carmelart.homeart.ui.ventanas

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.carmelart.homeart.databinding.FragmentVentanasBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class VentanasFragment : Fragment() {

    private lateinit var ventanasViewModel: VentanasViewModel
    private lateinit var dataVent: DataEntity
    private var _binding: FragmentVentanasBinding? = null
    private val binding get() = _binding!!
    private val timeout = 1000
    private val token = "fe5g8e2a5f4e85d2e85a7c5"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
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
        this.bindingManagement()
        // ACTUALIZAR DATA
        this.dataVent = dbController.DataDAO().getData()
        // TODAS LA VARIABLES DE SEGURIDAD
        binding.switchPuertaParcela.isChecked = this.dataVent.pParcela == 1
        binding.switchPuertaGaraje.isChecked = this.dataVent.pGaraje == 1
        binding.switchVentanaSalon.isChecked = this.dataVent.vSalon == 1
        binding.switchVentanaDormitorio.isChecked = this.dataVent.vDormitorio == 1
        binding.switchVentanaOficina.isChecked = this.dataVent.vOficina == 1

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            val activity: VentanasFragment = this
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: VentanasFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: VentanasFragment = this
            Toast.makeText(
                getActivity(), "Servidor caído",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun bindingManagement() {

        binding.switchPuertaParcela.setOnCheckedChangeListener { _, isChecked ->
            binding.togglePuertaParcela.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataVent.pParcela = ventValue
            dbController.DataDAO().updateData(this.dataVent)
            this.generateDataStringAndSend('p', this.dataVent)
        }

        binding.switchPuertaGaraje.setOnCheckedChangeListener { _, isChecked ->
            binding.togglePuertaGaraje.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataVent.pGaraje = ventValue
            dbController.DataDAO().updateData(this.dataVent)
            this.generateDataStringAndSend('g', this.dataVent)
        }

        binding.switchVentanaSalon.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentanaSalon.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataVent.vSalon = ventValue
            dbController.DataDAO().updateData(this.dataVent)
            this.generateDataStringAndSend('l', this.dataVent)
        }

        binding.switchVentanaDormitorio.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentanaDormitorio.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataVent.vDormitorio = ventValue
            dbController.DataDAO().updateData(this.dataVent)
            this.generateDataStringAndSend('d', this.dataVent)
        }

        binding.switchVentanaOficina.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentanaOficina.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataVent.vOficina = ventValue
            dbController.DataDAO().updateData(this.dataVent)
            this.generateDataStringAndSend('o', this.dataVent)
        }
    }


    fun generateDataStringAndSend(zona: Char, data: DataEntity) {
        var vVent: MutableList<String> = mutableListOf()
        // Todos los datos de Iluminación en el orden deseado

        vVent.add(data.pParcela.toString())
        vVent.add(data.pGaraje.toString())
        vVent.add(data.vSalon.toString())
        vVent.add(data.vDormitorio.toString())
        vVent.add(data.vOficina.toString())

        if(zona=='p') sendDataToServer("p;$vVent[0];$token\n")
        if(zona=='g') sendDataToServer("g;$vVent[1];$token\n")
        if(zona=='l') sendDataToServer("l;$vVent[2];$token\n")
        if(zona=='d') sendDataToServer("d;$vVent[3];$token\n")
        if(zona=='o') sendDataToServer("o;$vVent[4];$token\n")

        //sendDataToServer("p;$vVent;$token\n") // v[..., ..., ...] + token
    }
}