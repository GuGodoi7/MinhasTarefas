package com.example.minhastarefas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minhastarefas.databinding.ItemTarefaBinding
import com.example.minhastarefas.databinding.ItemTarefaCompletaBinding

class TarefasAdapter : ListAdapter<Tarefa, RecyclerView.ViewHolder>(DiffCallback()) {

    var onClick: (Tarefa) -> Unit = {}

    inner class TarefasViewHolder(val binding: ItemTarefaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tarefa: Tarefa) {
            binding.textViewTarefa.text = tarefa.descricao
            binding.root.setOnClickListener {
                onClick(tarefa)
                notifyDataSetChanged()
            }
        }
    }

    inner class TarefasCompletasViewHolder(val binding: ItemTarefaCompletaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tarefa: Tarefa) {
            binding.textViewTarefa.text = tarefa.descricao
            binding.root.setOnClickListener {
                onClick(tarefa)
                notifyDataSetChanged()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Tarefa>() {
        override fun areItemsTheSame(oldItem: Tarefa, newItem: Tarefa): Boolean {
            return oldItem.descricao == newItem.descricao
        }

        override fun areContentsTheSame(
            oldItem: Tarefa,
            newItem: Tarefa
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).completa) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTarefaBinding.inflate(layoutInflater, parent, false)
        val completaBinding = ItemTarefaCompletaBinding.inflate(layoutInflater, parent, false)
        return if (viewType == 0)
            TarefasViewHolder(binding)
        else TarefasCompletasViewHolder(completaBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TarefasCompletasViewHolder -> holder.bind(getItem(position))
            is TarefasViewHolder -> holder.bind(getItem(position))
        }
    }
}