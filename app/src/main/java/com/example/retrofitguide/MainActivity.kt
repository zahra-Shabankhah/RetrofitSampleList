package com.example.retrofitguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitguide.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter : TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progressbar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTodos()
            }catch (e : IOException){
                Log.d("MainActivity", "IOException")
                binding.progressbar.isVisible = false
                return@launchWhenCreated
            }catch (e : HttpException){
                Log.d("MainActivity", "HttpException")
                binding.progressbar.isVisible = false
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null){
                todoAdapter.todos = response.body()!!
            }else{
                Log.d("MainActivity", "Response is not successful")
            }
            binding.progressbar.isVisible = false

        }
    }
   private fun setUpRecyclerView() = binding.recyclerView.apply {
       todoAdapter = TodoAdapter()
       adapter = todoAdapter
       layoutManager = LinearLayoutManager(this@MainActivity)
   }
}