package com.example.myapplication.Activity.admin;

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.Activity.DangNhap
import com.example.myapplication.Activity.GioHang
import com.example.myapplication.R
import com.example.myapplication.Service.APIServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

public class ThemSanPham : AppCompatActivity() {
    private val REQUEST_CODE_IMAGEVIEW = 1000
    private var urlImg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        val toolbarquanlytaikhoan = findViewById<Toolbar>(R.id.toobarAddSanPham)
        setSupportActionBar(toolbarquanlytaikhoan)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbarquanlytaikhoan?.setNavigationIcon(R.drawable.back)
        toolbarquanlytaikhoan?.setNavigationOnClickListener(View.OnClickListener { finish() })

        themsanpham()
    }

    private fun themsanpham() {
        val HinhAnhSP = findViewById<TextView>(R.id.HinhAnhSP)

        HinhAnhSP.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_IMAGEVIEW)
        })

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            val TenSP = findViewById<EditText>(R.id.TenSP)
            val GiaSP = findViewById<EditText>(R.id.GiaSP)
            val NgayGiamGia = findViewById<EditText>(R.id.NgayGiamGia)
            val GiamGia = findViewById<EditText>(R.id.GiamGia)
            val MoTaSP = findViewById<EditText>(R.id.MoTaSP)
            val IDSP = findViewById<EditText>(R.id.IDSP)

            val dataService = APIServices.getService()
            val calback = dataService.insertSanPham(
                TenSP.text.toString(),
                GiaSP.text.toString(),
                NgayGiamGia.text.toString(),
                GiamGia.text.toString(),
                urlImg,
                MoTaSP.text.toString(),
                IDSP.text.toString()
            )
            calback.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    Toast.makeText(this@ThemSanPham,"Thêm sản phẩm thành công",Toast.LENGTH_LONG).show()
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    Toast.makeText(this@ThemSanPham,"Thêm sản phẩm Thất bại",Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_IMAGEVIEW && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val img = findViewById<ImageView>(R.id.imgInsert)
                img.setImageBitmap(bitmap)
                urlImg = toArrayString(bitmap)
                Log.d("SangTB", "onActivityResult: $urlImg")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun toArrayString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val array = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(array, Base64.DEFAULT)
    }
}
