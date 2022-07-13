package com.carmelart.homeart.ui.salon

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.carmelart.homeart.databinding.FragmentSalonBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class SalonFragment : Fragment() {

    private lateinit var salonViewModel: SalonViewModel
    private lateinit var dataSalon: DataEntity
    private var _binding: FragmentSalonBinding? = null
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
        salonViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(SalonViewModel::class.java)

        _binding = FragmentSalonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTOS
        val textView: TextView = binding.textSalonSalon
        salonViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // SWITCHES
        this.bindingManagement()
        // ACTUALIZAR DATA
        this.dataSalon = dbController.DataDAO().getData()
        // TODAS LAS VARIABLES DE ILUMINACIÓN
        binding.switchTelevisionSalon.isChecked = this.dataSalon.sTelevision == 1
        binding.switchIluminacionSalaSalon.isChecked = this.dataSalon.luzSala == 1
        binding.switchIluminacionAmbienteSalon.isChecked = this.dataSalon.luzAmbiente == 1
        binding.switchIluminacionComedorSalon.isChecked = this.dataSalon.luzComedor == 1
        binding.switchCortinasSalon.isChecked = this.dataSalon.vSalon == 1

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
            val activity: SalonFragment = this
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: SalonFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: SalonFragment = this
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

        binding.switchTelevisionSalon.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleTelevisionSalon.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataSalon.sTelevision = ledValue
            dbController.DataDAO().updateData(this.dataSalon)
            this.generateDataStringAndSend(this.dataSalon)
        }

        binding.switchIluminacionSalaSalon.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionSalaSalon.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataSalon.luzSala = ledValue
            dbController.DataDAO().updateData(this.dataSalon)
            this.generateDataStringAndSend(this.dataSalon)
        }

        binding.switchIluminacionAmbienteSalon.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionAmbienteSalon.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataSalon.luzAmbiente = ledValue
            dbController.DataDAO().updateData(this.dataSalon)
            this.generateDataStringAndSend(this.dataSalon)
        }

        binding.switchIluminacionComedorSalon.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionComedorSalon.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataSalon.luzComedor = ledValue
            dbController.DataDAO().updateData(this.dataSalon)
            this.generateDataStringAndSend(this.dataSalon)
        }

        binding.switchCortinasSalon.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleCortinasSalon.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataSalon.vSalon = ledValue
            dbController.DataDAO().updateData(this.dataSalon)
            this.generateDataStringAndSend(this.dataSalon)
        }
    }

    private fun generateDataStringAndSend(data: DataEntity) {
        var vSalon: MutableList<String> = mutableListOf()

        // Todos los datos de Iluminación en el orden deseado
        vSalon.add(data.sTelevision.toString())
        vSalon.add(data.luzSala.toString())
        vSalon.add(data.luzAmbiente.toString())
        vSalon.add(data.luzComedor.toString())
        vSalon.add(data.vSalon.toString())

        sendDataToServer("n;$vSalon;$token\n") // n[..., ..., ...] + token
    }
}