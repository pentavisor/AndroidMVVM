package com.example.apptest2019.fragments.fragment_main_menu_page

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apptest2019.R
import com.example.apptest2019.databinding.RecyclerviewHeaderMainPageBinding
import com.example.apptest2019.databinding.RecyclerviewItemMainPageBinding
import com.example.apptest2019.repository.database.database_models.UserDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException
import java.lang.Exception

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class RecyclerMainPageUsersAdapter(
    val cardClickListener: BasicAdapterCardClickListener,
    val headerCardClickListener: BasicAdapterHeaderCardClickListener
) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffUtilUserDataModelCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.IO)

    //start фугкция для обновления содержимого листа
    fun addHeaderAndSetList(list: List<UserDataModel>?) {
        adapterScope.launch {
                val items = when (list) {
                    null -> listOf(DataItem.Header)
                    else -> listOf(DataItem.Header) + list.map { DataItem.UserDataModelItem(it) }
                }
                withContext(Dispatchers.Main) {
                    submitList(items)
                }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.UserDataModelItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val userItem = getItem(position) as DataItem.UserDataModelItem
                holder.bind(userItem.userDataModel, cardClickListener)
            }
            is HeaderViewHolder -> {
                val headerItem = getItem(position) as DataItem.Header
                holder.bind(headerCardClickListener)
            }
        }
    }

    class ItemViewHolder private constructor(val binding: RecyclerviewItemMainPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserDataModel, cardClickListener: BasicAdapterCardClickListener) {
            //if (binding is RecyclerviewHeaderMainPageBinding) {
            binding.dataModel = item
            binding.itemClickListener = cardClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerviewItemMainPageBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }


    class HeaderViewHolder private constructor(val binding: RecyclerviewHeaderMainPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cardClickListener: BasicAdapterHeaderCardClickListener) {
            // if (binding is RecyclerviewHeaderMainPageBinding) {
            // binding.dataModel = item
            binding.itemClickListener = cardClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    RecyclerviewHeaderMainPageBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }
}

// использование класса DiffUtil для того чтобы при обновлении списка отойти от this.notifyDataSetChanged()
// это позволяет сильно экономить ресурсы на отрисовке RecyclerView при динамическом добавлении элементов
// из за этго был заменен родитель класса с RecyclerView.Adapter<RecyclerMainPageUsersAdapter.ViewHolder>()
// на  ListAdapter<UserDataModel, RecyclerMainPageUsersAdapter.ViewHolder>(DiffUtilUserDataModelCallback())
// и  удален override fun getItemCount(): Int { return items.size }
class DiffUtilUserDataModelCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.equals(newItem)
    }
}

sealed class DataItem {
    abstract val id: Long

    data class UserDataModelItem(val userDataModel: UserDataModel) : DataItem() {
        override val id = userDataModel.id
    }

    object Header : DataItem() {
        override val id: Long = Long.MIN_VALUE
    }
}

// clickListener для элементов списка
interface BasicAdapterCardClickListener {
    fun cardClicked(f: UserDataModel)
}

// clickListener для дополнительных элементов(принимает вид элемента и определяет тип вызова у callback)
interface BasicAdapterHeaderCardClickListener {
    fun cardHeaderClicked(typeOfButton: Int)
}

