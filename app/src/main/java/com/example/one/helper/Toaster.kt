package com.example.one.helper

import android.content.Context
import android.widget.Toast

object Toaster {
    fun showShortToaster(context: Context,string:String){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show()
    }

    fun showLongToaster(context: Context,string: String){
        Toast.makeText(context,string,Toast.LENGTH_LONG).show()
    }
}