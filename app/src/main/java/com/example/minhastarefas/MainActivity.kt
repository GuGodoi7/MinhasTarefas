package com.example.minhastarefas

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
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
    val adapter = TarefasAdapter()
    val categoriasAdapter = CategoriasAdapter()

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
        recuperaDados()
        adapter.onClick = {
            val index = listaTarefas.indexOf(it)
            listaTarefas[index].completa = !listaTarefas[index].completa
            salvarDados(listaTarefas)
        }
        adapter.onLongClick = {
            val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.excluir_tarefa))
                .setMessage(getString(R.string.tem_certeza))
                .setPositiveButton(
                    getString(R.string.sim),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val index = listaTarefas.indexOf(it)
                            listaTarefas.removeAt(index)
                            salvarDados(listaTarefas)
                            adapter.notifyDataSetChanged()
                        }
                    })
                .setNegativeButton(
                    getString(R.string.nao),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            // Não faz nada
                        }
                    })

            dialog.show()
        }
        categoriasAdapter.onClick = { categoria ->
            adapter.submitList(listaTarefas.filter { tarefa ->
                tarefa.descricao.contains(categoria)
            })
        }
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

        categoriasAdapter.submitList(categorias)
        val categoriasJson = gson.toJson(categorias)
        sharedPrefs.edit().putString(Constants.CATEGORIAS, categoriasJson).apply()
    }

    private fun salvarDados(tarefas: List<Tarefa>) {
        val tarefasJson = gson.toJson(tarefas)
        sharedPrefs.edit().putString(Constants.TAREFAS, tarefasJson).apply()
        adapter.submitList(tarefas)
    }

    private fun recuperaDados() {
        val tarefasJson = sharedPrefs.getString(Constants.TAREFAS, "[]").orEmpty()
        val categoriasJson = sharedPrefs.getString(Constants.CATEGORIAS, "[]").orEmpty()
        listaTarefas.addAll(
            gson.fromJson<Array<Tarefa>>(
                tarefasJson,
                Array<Tarefa>::class.java
            )
        )

        categorias.addAll(
            gson.fromJson(
                categoriasJson,
                Array<String>::class.java
            )
        )

        adapter.submitList(listaTarefas)
        categoriasAdapter.submitList(categorias)
    }
}