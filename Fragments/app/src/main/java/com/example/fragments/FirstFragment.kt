package com.example.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.View
import androidx.lifecycle.Lifecycle
import kotlinx.android.synthetic.main.fragment_first.*


class FirstFragment : Fragment(R.layout.fragment_first)
{
    var textFile = "123"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numberPicker.minValue = 15
        numberPicker.maxValue = 30

        var enter = "Test"

        textButton.setOnClickListener {
            textFile =  editText.getText().toString()
            textEditText.text = textFile
        }


        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            textNumberPicker.text = newVal.toString()
        }

    }
}

