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
import androidx.fragment.app.activityViewModels
import androidx.room.Room

import com.carmelart.homeart.MainActivityViewModel
import com.carmelart.homeart.database.ProductsDatabase
import com.carmelart.homeart.databinding.FragmentIluminacionBinding
//import com.carmelart.homeart.formatStringsForTextView

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException


//import com.carmelart.homeart.dbController

//import com.carmelart.homeart.database.ProductsEntity

class IluminacionFragment : Fragment() {

    private lateinit var iluminacionViewModel: IluminacionViewModel
    private var _binding: FragmentIluminacionBinding? = null

    private val model: MainActivityViewModel by activityViewModels()



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var nLED: Int = 0

    //val ilum = dbController.productsDAO().getOneProduct("baseId")

    private val timeout = 1000
    private val token = "fe5g8e2a5f4e85d2e85a7c5"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
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

        // Base de datos

        val db = Room.databaseBuilder(
            requireContext(),
            ProductsDatabase::class.java,
            "HomeArt_DB",
        ).allowMainThreadQueries().build()

        db.DataDAO().getData("myDB")



        // TEXTOS

        val textView: TextView = binding.textSalon
        iluminacionViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        // SWITCHES

        binding.switchIluminacionSala.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionSala.isChecked = isChecked



            @Override

            if(nLED==0) nLED=1
            else nLED=0



            //if(ilum.LEDazul==0)ilum.LEDazul=1
            //else ilum.LEDazul=0

            //dbController.productsDAO().updateProduct(product = ilum)


            formatStringsForTextView()

            sendDataToServer(binding.textSet.text.toString() + " ; " + token + "\n")
            //sendDataToServer(mainActivityViewModel.mensaje + " ; " + token + "\n")
        }

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

        // Base de datos
        /*this.dbController = Room
            .databaseBuilder(this.requireContext(), ProductsDatabase::class.java, "HomeArtDatabase")
            .allowMainThreadQueries()
            .build()*/

        return root
    }


    private fun formatStringsForTextView()
    {
        val stringLED: String

        if (nLED == 0) stringLED = "0"
        else stringLED = "1"

        //if(ilum.LEDazul==0) stringLED = "0"
        //else stringLED = "1"

        binding.textSet.text = "SET_LED:$stringLED \n"
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
        }

        catch (e: SocketException)
        {
            e.printStackTrace()
            val activity: IluminacionFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        }

        catch (e: Exception)
        {
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

    // Base de datos
    /*val Fragment.dbController: ProductsDatabase
        get()=(requireActivity() as IluminacionFragment).dbController*/
}