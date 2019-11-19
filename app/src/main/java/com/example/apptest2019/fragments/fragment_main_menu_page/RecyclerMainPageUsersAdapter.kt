package com.example.apptest2019.fragments.fragment_main_menu_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.apptest2019.R
import com.example.apptest2019.databinding.RecyclerviewHeaderMainPageBinding
import com.example.apptest2019.databinding.RecyclerviewItemMainPageBinding
import com.example.apptest2019.repository.database.database_models.UserDataModel
import java.lang.Exception

/*
* 0.5) вставить, продублировать  (items as? MutableList)?.add(0, Any())
* 1) getItemViewType тип для определения нового поля в адаптере
* 2) onCreateViewHolder вставить inflater для дальнейшего биндинга
* 3) ViewHolder вставить туда конфигурирование
* */

class RecyclerMainPageUsersAdapter(
    val cardClickListener:BasicAdapterCardClickListener,
    val headerCardClickListener:BasicAdapterHeaderCardClickListener
) :
    RecyclerView.Adapter<RecyclerMainPageUsersAdapter.ViewHolder>() {
    var items = listOf<Any>()
        set(value) {
            field = value
            // проверка должна быть сделана на каждый элемент header
            if((items as? MutableList)?.get(0) !is EmptyHeaderClass)
            (items as? MutableList)?.add(0, EmptyHeaderClass())
            this.notifyDataSetChanged()
        }//set y листа переопределён для того чтобы обновлть отображение адаптера при обновлении списка


    override fun getItemCount(): Int {
        return items.size
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                1
            }//нулевой элемент - HeaderViewHolder(кнопка) ViewType 1
            else -> {
                0
            }//остальные элементы списка ViewType 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var listItemBinding: ViewDataBinding? = null
        try {
            when (viewType) {
                0 -> {
                    listItemBinding = DataBindingUtil.inflate<RecyclerviewItemMainPageBinding>(
                        inflater,
                        R.layout.recyclerview_item_main_page,
                        parent,
                        false
                    )
                }// binding простого элемента
                1 -> {
                    listItemBinding = DataBindingUtil.inflate<RecyclerviewHeaderMainPageBinding>(
                        inflater,
                        R.layout.recyclerview_header_main_page,
                        parent,
                        false
                    )
                }//binding элемента Header
            }
        } catch (E: Exception) {
        }
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(val binding: ViewDataBinding?) :
        RecyclerView.ViewHolder((binding as ViewDataBinding).root) {
        fun bind(item: Any) {
            try {
                //колбеки для определения параметров класса записи в ркциклкре
                if (binding is RecyclerviewHeaderMainPageBinding) {
                    // если биндинг от item это один из Headers
                    binding.itemClickListener = headerCardClickListener
                }
                if (binding is RecyclerviewItemMainPageBinding) {
                    // если биндинг от item это один из обычных Items
                    binding.dataModel = (item as? UserDataModel)
                    binding.itemClickListener = cardClickListener
                }
            } catch (e: TypeCastException) {

            }
            //binding.dataModel = item //чтобы не забыть что делает этот кусок :)
            binding?.executePendingBindings()
        }
    }
    //класс создан для того чтобы можно было отличить элементы Headers в списке объектов Any
    inner class EmptyHeaderClass : Any()
}

// clickListener для элементов списка
interface BasicAdapterCardClickListener {
    fun cardClicked(f: UserDataModel)
}

// clickListener для дополнительных элементов(принимает вид элемента и определяет тип вызова у callback)
interface BasicAdapterHeaderCardClickListener {
    fun cardHeaderClicked(typeOfButton: Int)
}
