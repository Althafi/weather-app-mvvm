package com.example.weatherapp.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.CityListAdapter
import com.example.weatherapp.databinding.ActivityCityListBinding
import com.example.weatherapp.model.CityListResponseApi
import com.example.weatherapp.viewmodel.CityListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityListActivity : AppCompatActivity() {

    private lateinit var biding: ActivityCityListBinding
    private val viewModel: CityListViewModel by viewModels()
    private val cityListAdapter by lazy { CityListAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityCityListBinding.inflate(layoutInflater)
        setContentView(biding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        biding.apply {
            etSearchCity.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                }

                override fun afterTextChanged(s: Editable?) {
                    pbLoadCity.visibility = View.VISIBLE
                    viewModel.getCityList(s.toString(), 10).enqueue(object : Callback<CityListResponseApi> {
                        override fun onResponse(
                            call: Call<CityListResponseApi>,
                            response: Response<CityListResponseApi>,
                        ) {
                            if (response.isSuccessful) {
                                val data = response.body()
                                data?.let {
                                    pbLoadCity.visibility = View.GONE
                                    cityListAdapter.differ.submitList(it)
                                    rvCityList.apply {
                                        layoutManager = LinearLayoutManager(
                                            this@CityListActivity,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        adapter = cityListAdapter
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<CityListResponseApi>, t: Throwable) {
                            Toast.makeText(this@CityListActivity, t.message, Toast.LENGTH_SHORT).show()
                        }

                    })
                }

            })
        }
    }
}