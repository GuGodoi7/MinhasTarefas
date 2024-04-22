package com.example.minhastarefas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.minhastarefas.databinding.FragmentListaTarefasBinding

class ListaTarefasFragment : Fragment() {

    private lateinit var binding: FragmentListaTarefasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaTarefasBinding.inflate(inflater)

        binding.botaoCriaTarefa.setOnClickListener {
            criarNovaTarefa.invoke()
        }

        return binding.root
    }

    companion object {
        private var criarNovaTarefa: () -> Unit = {}

        @JvmStatic
        fun newInstance(criaTarefa: () -> Unit = {}, param2: String): ListaTarefasFragment {
            criarNovaTarefa = criaTarefa
            return ListaTarefasFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }
}