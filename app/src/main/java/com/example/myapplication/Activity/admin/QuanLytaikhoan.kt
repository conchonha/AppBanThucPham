package com.example.myapplication.Activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.R
import com.example.myapplication.Service.DataService
import com.example.myapplication.Service.APIServices
import com.example.myapplication.Model.Taikhoan
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.Activity.DangKy
import com.example.myapplication.Adapter.Adapter_Quanlytaikhoan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class QuanLytaikhoan : AppCompatActivity() {
    private var toolbarquanlytaikhoan: Toolbar? = null
    private var recyclerviewquanlytaikhoan: RecyclerView? = null
    private val REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quan_lytaikhoan)
        toolbarquanlytaikhoan = findViewById(R.id.toolbarquanlytaikhoan)
        setSupportActionBar(toolbarquanlytaikhoan)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbarquanlytaikhoan?.setNavigationIcon(R.drawable.back)
        toolbarquanlytaikhoan?.setNavigationOnClickListener(View.OnClickListener { finish() })
        getdatataikhoan()
    }

    private fun getdatataikhoan() {
        val dataService = APIServices.getService()
        val callback = dataService.getdataalltaikhoan()
        callback.enqueue(object : Callback<List<Taikhoan?>?> {
            override fun onResponse(
                call: Call<List<Taikhoan?>?>,
                response: Response<List<Taikhoan?>?>
            ) {
                Log.d("AAA", "getdata all taikhoan: $response")
                if (response.isSuccessful) {
                    val arrayList = response.body() as? ArrayList<Taikhoan> ?: arrayListOf()
                    setrecyclerview(arrayList)
                }
            }

            override fun onFailure(call: Call<List<Taikhoan?>?>, t: Throwable) {}
        })

    }

    private fun setrecyclerview(arrayList: ArrayList<Taikhoan>) {
        recyclerviewquanlytaikhoan = findViewById(R.id.recyclerviewquanlytaikhoan)
        recyclerviewquanlytaikhoan?.setHasFixedSize(true)
        recyclerviewquanlytaikhoan?.setLayoutManager(GridLayoutManager(applicationContext, 1))
        val adpter =
            Adapter_Quanlytaikhoan(applicationContext, R.layout.layout_quanlytaikhoan, arrayList)
        recyclerviewquanlytaikhoan?.setAdapter(adpter)
        adpter.notifyDataSetChanged()

        adpter.callback = {taikhoan ->
            startActivityForResult(
                Intent(this, DangKy::class.java).putExtra("admin",taikhoan),REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            finish()
            startActivity(intent)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}