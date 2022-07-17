package com.carmelart.homeart

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.carmelart.homeart.ui.configuracion.ConfiguracionFragment
import java.util.*

class TimePicker(val listener: (hora: Int, minuto: Int) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hora = c.get(Calendar.HOUR_OF_DAY)
        val minuto = c.get(Calendar.MINUTE)
        return TimePickerDialog(getActivity(), this, hora, minuto, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener(hourOfDay, minute)
    }
}