package ru.mydispatcher.ui.customer.orders

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import ru.mydispatcher.R
import ru.mydispatcher.ui.base.BaseActivity
import ru.mydispatcher.ui.customer.orders.orderslist.OrdersListFragment
import ru.mydispatcher.util.extensions.bindView
import javax.inject.Inject


class CustomerOrdersActivity :
        BaseActivity(),
        CustomerOrdersView {

    private val viewPager by bindView<ViewPager>(R.id.viewPager)
    private val tabLayout by bindView<TabLayout>(R.id.tabLayout)

    @Inject lateinit var presenter: CustomerOrdersPresenter

    private val adapter by lazy { PagerAdapter(supportFragmentManager) }

    override fun getLayoutResId() = R.layout.activity_customer_orders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.offscreenPageLimit = 2
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    enum class Fragments(val fragment: Fragment, val title: String) {
        ACTIVE(OrdersListFragment.createFragment(true), "активные"),
        INACTIVE(OrdersListFragment.createFragment(false), "неактивные")
    }


    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        private val PAGE_COUNT = 2

        override fun getItem(position: Int) = when (position) {
            0 -> Fragments.ACTIVE.fragment
            else -> Fragments.INACTIVE.fragment
        }

        override fun getCount(): Int = PAGE_COUNT

        override fun getPageTitle(position: Int) = when (position) {
            0 -> Fragments.ACTIVE.title
            else -> Fragments.INACTIVE.title
        }
    }


}