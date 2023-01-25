package com.example.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstFragment = FirstFragment()
        val secondFragment = SecondFragment()
        var flag = false;

        supportFragmentManager.beginTransaction().apply {
            // Replace current fragment with firstFragment() , this case we are replacing nothing
            replace(R.id.flFragment, firstFragment)
            // Need to commit changes to apply
            commit()
        }
        btnFragment1.setOnClickListener {
            supportFragmentManager.saveFragmentInstanceState(secondFragment)
            supportFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                show(firstFragment) //LOL I DID IT LETS GOOOOOOO
                hide(secondFragment)
//                if(fragmentManager.findFragmentByTag("firstFragment") != null)
//                    show(firstFragment).commit()
//                else
//                    add(R.id.flFragment, firstFragment).commit()
//                if(fragmentManager.findFragmentByTag("secondFragment") != null)
//                    hide(secondFragment).commit()
                //replace(R.id.flFragment, firstFragment)
                //addToBackStack(null) // This makes back button go to previous fragment
                commit()
            }
        }
        btnFragment2.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                if(flag == false) {
                    add(R.id.flFragment, secondFragment)
                    flag = true
                }
                else
                    show(secondFragment)
                hide(firstFragment)
//                if(fragmentManager.findFragmentByTag("secondFragment") != null)
//                    show(secondFragment).commit()
//                else
//                    add(R.id.flFragment, secondFragment).commit()
//                if(fragmentManager.findFragmentByTag("firstFragment") != null)
//                    hide(firstFragment).commit()
                //replace(R.id.flFragment, secondFragment)
                //addToBackStack(null) // This makes back button go to previous fragment
                commit()
            }
        }

    }
}