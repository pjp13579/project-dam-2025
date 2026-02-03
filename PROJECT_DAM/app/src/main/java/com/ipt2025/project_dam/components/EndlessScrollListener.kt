package com.ipt2025.project_dam.components

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * abstract listener to detect when the user scrolled to the bottom of a list
 * requires RecyclerView and LinearLayoutManager to work
 */
abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {

    // the total number of items in the dataset after the last load
    private var previousTotalItems = 0

    // true if we are still waiting for the previous set of data to load
    private var loading = true

    // tracks the current page of data to fetch
    private var currentPage = 0
    val THRESHOLD = 5 // load more when we are 5 items away from bottom

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        // get the LayoutManager to access item counts and scroll positions
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        // items currently drawn on the screen
        val visibleItemCount = layoutManager.childCount

        // total items currently in the adapter
        val totalItemCount = layoutManager.itemCount

        // index of the topmost visible item in the list
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()



        /**
         * if the total item count has increased, we assume the previous
         * 'onLoadMore' request has finished updating the adapter
         */
        if (loading && totalItemCount > previousTotalItems) {
            loading = false
            previousTotalItems = totalItemCount
        }

        // checks if we hit the threshold to load the next page
        /**
         * trigger a new load if
         * 1. we aren't already loading a page (loading == false)
         * 2. the remaining items below the current scroll position are less than or equal to THRESHOLD
         */
        if (!loading && (totalItemCount - visibleItemCount <= firstVisibleItemPosition + THRESHOLD)) {
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }

    // this needs to be implemented by the fragment to actually fetch data
    abstract fun onLoadMore(page: Int)
}