package com.example.minhastarefas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minhastarefas.databinding.FragmentListaTarefasBinding

class ListaTarefasFragment : Fragment() {

    private lateinit var binding: FragmentListaTarefasBinding
    private lateinit var adapter: TarefasAdapter
    private lateinit var categoriasAdapter: CategoriasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaTarefasBinding.inflate(inflater)
        adapter = (activity as MainActivity).adapter
        categoriasAdapter = (activity as MainActivity).categoriasAdapter

        binding.botaoCriaTarefa.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentListaTarefas_to_fragmentCriarTarefas)
        }

        configurarRecyclewView()
        return binding.root
    }

    private fun configurarRecyclewView() {
        binding.recyclerViewTarefas.adapter = adapter
        binding.recyclerViewTarefas.layoutManager = LinearLayoutManager(context)

        binding.recyclewViewCategorias.adapter = categoriasAdapter
        binding.recyclewViewCategorias.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

//    companion object {
//        private var criarNovaTarefa: () -> Unit = {}
//
//        @JvmStatic
//        fun newInstance(criaTarefa: () -> Unit = {}, param2: String): ListaTarefasFragment {
//            criarNovaTarefa = criaTarefa
//            return ListaTarefasFragment().apply {
//                arguments = Bundle().apply {
//
//                }
//            }
//        }
//    }
}