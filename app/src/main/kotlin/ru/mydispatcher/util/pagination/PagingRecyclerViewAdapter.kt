package ru.mydispatcher.util.pagination

import android.support.v7.widget.RecyclerView

import java.util.ArrayList

abstract class PagingRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var items: MutableList<T> = mutableListOf()
    var isAllItemsLoaded: Boolean = false

    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Возвращает количество настоящих элементов, которые необходимо пагинировать
     * @return
     */
    val realItemCount: Int
        get() = items.size

    fun addNewItems(items: List<T>) {
        if (items.size == 0) {
            isAllItemsLoaded = true
            return
        }
        this.items.addAll(items)
        notifyItemInserted(itemCount - items.size)
    }

    fun clear() {
        isAllItemsLoaded = false
        this.items.clear()
        notifyDataSetChanged()
    }

    protected open fun getItem(position: Int): T {
        return items[position]
    }
}

