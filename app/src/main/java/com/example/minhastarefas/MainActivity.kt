package com.example.minhastarefas

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minhastarefas.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPrefs: SharedPreferences
    private val listaTarefas = arrayListOf<Tarefa>()
    private val categorias = arrayListOf<String>()
    private val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id)

        navHostFragment?.findNavController()?.let {
            navController = it
        }

        setupActionBarWithNavController(navController)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.navigateUp()) {
                    navController.navigateUp()
                } else {
                    this@MainActivity.finish()
                }
            }
        })
        recuperaDados("TAREFA")
//        abrirTelaListaTarefas()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun criarTarefa(descricao: String) {
        listaTarefas.add(
            Tarefa(
                descricao = descricao,
                completa = false
            )
        )

        salvarDados(listaTarefas)
        println(listaTarefas)
        categorizaTarefas(descricao)
        navController.navigateUp()
    }

    private fun categorizaTarefas(descricao: String) {
        val listaPalavras = descricao.trim().split("\\s+".toRegex())
        for (i in listaPalavras) {
            if (i.contains("#")) {
                if (!categorias.contains(i)) {
                    categorias.add(i)
                }
            }
        }
        println(categorias)
    }

    private fun salvarDados(tarefas: List<Tarefa>) {
        val tarefasJson = gson.toJson(tarefas)
        sharedPrefs.edit().putString("TAREFA", tarefasJson).apply()
    }

    private fun recuperaDados(key: String) {
        val tarefasJson = sharedPrefs.getString(key, "").orEmpty()
        listaTarefas.addAll(gson.fromJson<Array<Tarefa>>(
            tarefasJson,
            Array<Tarefa>::class.java
        ))
    }

//    override fun onBackPressed() {
//        navController.navigateUp()
//    }

//    private fun abrirTelaListaTarefas() {
//        val listaTarefasFragment = ListaTarefasFragment.newInstance({
//            abrirTelaCriarTarefas()
//        }, "")
//
//        supportFragmentManager.beginTransaction().replace(
//            binding.frameLayout.id,
//            listaTarefasFragment
//        ).commit()
//    }
//
//    private fun abrirTelaCriarTarefas() {
//        val listaTarefas = arrayListOf<Tarefa>()
//        val criaTarefasFragment = CriaTarefasFragment.newInstance({
//            val tarefa = Tarefa(
//                descricao = it,
//                completa = false
//            )
//            listaTarefas.add(tarefa)
//        }, "")
//
//        supportFragmentManager.beginTransaction().replace(
//            binding.frameLayout.id,
//            criaTarefasFragment
//        ).commit()
//    }
}