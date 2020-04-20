package com.example.myokhttp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.myokhttp.databinding.ActivityMenuBinding
import com.example.myokhttp.okhttp.OkhttpActivity
import com.example.myokhttp.retrofit.RetrofitActivity

class MenuActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btn1 -> {
                startActivity(Intent(this@MenuActivity, OkhttpActivity::class.java))
            }
            binding.btn2 -> {
                startActivity(Intent(this@MenuActivity, RetrofitActivity::class.java))
            }
        }
    }
}
