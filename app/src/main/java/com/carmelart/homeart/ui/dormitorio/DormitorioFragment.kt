package com.carmelart.homeart.ui.dormitorio

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

import com.carmelart.homeart.databinding.FragmentDormitorioBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.databinding.FragmentCocinaBinding
import com.carmelart.homeart.dbController
import com.carmelart.homeart.ui.cocina.CocinaFragment
import com.carmelart.homeart.ui.cocina.CocinaViewModel

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class DormitorioFragment : Fragment() {

    private lateinit var dormitorioViewModel: DormitorioViewModel
    private lateinit var dataDormitorio: DataEntity
    private var _binding: FragmentDormitorioBinding? = null
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
        dormitorioViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(DormitorioViewModel::class.java)

        _binding = FragmentDormitorioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTOS
        val textView: TextView = binding.textDormitorioDormitorio
        dormitorioViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // SWITCHES
        this.bindingManagement()
        // ACTUALIZAR DATA
        this.dataDormitorio = dbController.DataDAO().getData()
        // TODAS LAS VARIABLES DE DORMITORIO
        binding.switchIluminacionGeneralDorm.isChecked = this.dataDormitorio.luzDormitorio == 1
        binding.switchIluminacionMesitaizqDorm.isChecked = this.dataDormitorio.luzMesitaIzq == 1
        binding.switchIluminacionMesitadchDorm.isChecked = this.dataDormitorio.luzMesitaDch == 1
        binding.switchCortinasDorm.isChecked = this.dataDormitorio.vDormitorio == 1
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
            val activity: DormitorioFragment = this
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: DormitorioFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: DormitorioFragment = this
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

        binding.switchIluminacionGeneralDorm.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGeneralDorm.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataDormitorio.luzDormitorio = ledValue
            dbController.DataDAO().updateData(this.dataDormitorio)
            this.generateDataStringAndSend(this.dataDormitorio)
        }

        binding.switchIluminacionMesitaizqDorm.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionMesizqDorm.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataDormitorio.luzMesitaIzq = ledValue
            dbController.DataDAO().updateData(this.dataDormitorio)
            this.generateDataStringAndSend(this.dataDormitorio)
        }

        binding.switchIluminacionMesitadchDorm.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionMesdchDorm.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataDormitorio.luzMesitaDch = ledValue
            dbController.DataDAO().updateData(this.dataDormitorio)
            this.generateDataStringAndSend(this.dataDormitorio)
        }

        binding.switchCortinasDorm.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleCortinasDorm.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataDormitorio.vDormitorio = ledValue
            dbController.DataDAO().updateData(this.dataDormitorio)
            this.generateDataStringAndSend(this.dataDormitorio)
        }
    }

    private fun generateDataStringAndSend(data: DataEntity) {
        var vDormitorio: MutableList<String> = mutableListOf()

        // Todos los datos de Dormitorio en el orden deseado
        vDormitorio.add(data.luzDormitorio.toString())
        vDormitorio.add(data.luzMesitaIzq.toString())
        vDormitorio.add(data.luzMesitaDch.toString())
        vDormitorio.add(data.vDormitorio.toString())

        sendDataToServer("r;$vDormitorio;$token\n") // r[..., ..., ...] + token
    }
}