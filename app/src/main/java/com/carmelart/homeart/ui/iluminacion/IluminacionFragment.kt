package com.carmelart.homeart.ui.iluminacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.StrictMode
import android.widget.Toast
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.databinding.FragmentIluminacionBinding
import com.carmelart.homeart.dbController
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class IluminacionFragment : Fragment() {

    private lateinit var iluminacionViewModel: IluminacionViewModel
    private lateinit var dataIlum: DataEntity
    private var _binding: FragmentIluminacionBinding? = null
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
        iluminacionViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(IluminacionViewModel::class.java)
        _binding = FragmentIluminacionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // TEXTOS
        val textView: TextView = binding.textSalon
        iluminacionViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // SWITCHES
        this.bindingManagement()
        // Actualizar data.
        this.dataIlum = dbController.DataDAO().getData()
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
            val activity: IluminacionFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: IluminacionFragment = this
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

    fun bindingManagement() {
        binding.switchIluminacionComedor.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionComedor.isChecked = isChecked
        }

        binding.switchIluminacionCogeneral.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionCogeneral.isChecked = isChecked
        }

        binding.switchIluminacionFregadero.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionFregadero.isChecked = isChecked
        }

        binding.switchIluminacionBageneral.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionBageneral.isChecked = isChecked
        }

        binding.switchIluminacionEspejo.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionEspejo.isChecked = isChecked
        }

        binding.switchIluminacionDogeneral.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionDogeneral.isChecked = isChecked
        }

        binding.switchIluminacionMesitaizq.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionMesizq.isChecked = isChecked
        }

        binding.switchIluminacionMesitadch.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionMesdch.isChecked = isChecked
        }

        binding.switchIluminacionOfgeneral.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionOfgeneral.isChecked = isChecked
        }

        binding.switchIluminacionGaming.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGaming.isChecked = isChecked
        }

        binding.switchIluminacionGageneral.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGageneral.isChecked = isChecked
        }

        binding.switchIluminacionHugeneral.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionHugeneral.isChecked = isChecked
        }

        binding.switchIluminacionSala.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionSala.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.led = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
        }

    }

    fun generateDataStringAndSend(data: DataEntity) {
        sendDataToServer(binding.textSet.text.toString() + " ; " + token + "\n")
    }

}