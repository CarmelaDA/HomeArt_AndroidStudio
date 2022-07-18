package com.carmelart.homeart

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

import java.util.*

class TimePicker(val listener: (String) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener{


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hora = c.get(Calendar.HOUR_OF_DAY)
        val minuto = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hora, minuto, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(minute<10) listener("$hourOfDay:0$minute h")
        else listener("$hourOfDay:$minute h")
    }

}