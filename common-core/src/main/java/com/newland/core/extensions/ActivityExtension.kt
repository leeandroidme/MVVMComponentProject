package com.newland.core.extensions

import android.app.Activity
import android.content.Intent
import android.widget.Toast

fun <T> Activity.startActivity(clazz: Class<T>){
    val intent= Intent(this,clazz)
    startActivity(intent)
}
fun Activity.showToast(text:String){
    Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
}