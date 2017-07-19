package ru.mydispatcher.ui.selectgeoobject

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.mydispatcher.R
import ru.mydispatcher.data.model.GeoObject
import ru.mydispatcher.util.pagination.PagingRecyclerViewAdapter
import javax.inject.Inject


class GeoObjectsAdapter @Inject
constructor() : PagingRecyclerViewAdapter<GeoObject, GeoObjectsAdapter.ViewHolder>() {
    private var onObjectClickListener: (GeoObject) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_city, parent, false))


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(getItem(position))
    }

    fun onObjectClick(listener: (GeoObject) -> Unit ) {
        onObjectClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootLayout: View = itemView.findViewById(R.id.layoutRoot)
        val textViewName: TextView = itemView.findViewById(R.id.textViewCityName) as TextView
        val textViewRegion: TextView = itemView.findViewById(R.id.textViewCityRegion) as TextView

        fun bind(geoObject: GeoObject) {
            textViewName.text = geoObject.name
            textViewRegion.text = geoObject.parentName
            rootLayout.setOnClickListener { onObjectClickListener(geoObject) }
        }
    }
}