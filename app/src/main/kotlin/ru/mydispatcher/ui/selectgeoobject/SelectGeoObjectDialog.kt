package ru.mydispatcher.ui.selectgeoobject

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_select_geo_object.*
import ru.mydispatcher.R
import ru.mydispatcher.data.model.GeoObject
import ru.mydispatcher.ui.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject


class SelectGeoObjectDialog : DialogFragment(), SelectGeoObjectView {

    @Inject lateinit var presenter: SelectGeoObjectPresenter
    @Inject lateinit var adapter: GeoObjectsAdapter
    lateinit var type: String
    lateinit var cityFlowable: Flowable<String>
    lateinit var cityDisposable: Disposable
    private var onObjectClickListener: (GeoObject) -> Unit = {}

    companion object {
        const val EXTRA_OBJECT_TYPE = "ru.mydispatcher.extraObjectType"
        const val CITIES = "cities"
        const val REGIONS = "regions"
        const val ALL = ""
        fun createDialog(objectTypes: String): SelectGeoObjectDialog {
            val bundle = Bundle()
            bundle.putString(EXTRA_OBJECT_TYPE, objectTypes)
            val fragment = SelectGeoObjectDialog()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments.getString(EXTRA_OBJECT_TYPE)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_select_geo_object, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).activityComponent?.inject(this)
        presenter.bind(this)
        recyclerViewGeoObjects.layoutManager = LinearLayoutManager(activity)
        recyclerViewGeoObjects.addItemDecoration(DividerItemDecoration(activity, VERTICAL))
        recyclerViewGeoObjects.adapter = adapter
        adapter.onObjectClick {
            onObjectClickListener(it)
            dismiss()
        }
        cityFlowable = editTextCityQuery.textChanges()
                .skip(1)
                .filter { it.length > 2 || it.isEmpty() }
                .map { it.toString() }
                .toFlowable(BackpressureStrategy.LATEST)
        presenter.getObjects(recyclerViewGeoObjects, type, "")
        cityDisposable = cityFlowable.subscribe(
                {
                    adapter.clear()
                    presenter.getObjects(recyclerViewGeoObjects, type, it)
                },
                {
                    Timber.e(it)
                })
    }

    override fun onDestroy() {
        presenter.destroy()
        if (!cityDisposable.isDisposed) {
            cityDisposable.dispose()
        }
        super.onDestroy()
    }

    fun onObjectClick(listener: (GeoObject) -> Unit ) {
        onObjectClickListener = listener
    }

    override fun showLoading() {
        recyclerViewGeoObjects.visibility = GONE
        layoutNothingFound.visibility = GONE
        layoutError.visibility = GONE
        progressBar.visibility = VISIBLE
    }

    override fun addItems(geoObjects: List<GeoObject>) {
        recyclerViewGeoObjects.visibility = VISIBLE
        layoutNothingFound.visibility = GONE
        layoutError.visibility = GONE
        progressBar.visibility = GONE
        adapter.addNewItems(geoObjects)
    }

    override fun showError(message: String) {
        recyclerViewGeoObjects.visibility = GONE
        layoutNothingFound.visibility = GONE
        layoutError.visibility = VISIBLE
        progressBar.visibility = GONE
        textViewError.text = message
    }

    override fun showError(message: Int) {
        recyclerViewGeoObjects.visibility = GONE
        layoutNothingFound.visibility = GONE
        layoutError.visibility = VISIBLE
        progressBar.visibility = GONE
        textViewError.text = getString(message)
    }

    override fun showEmpty() {
        recyclerViewGeoObjects.visibility = GONE
        layoutNothingFound.visibility = VISIBLE
        layoutError.visibility = GONE
        progressBar.visibility = GONE
    }
}