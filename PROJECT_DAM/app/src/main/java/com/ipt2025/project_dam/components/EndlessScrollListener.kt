package com.ipt2025.project_dam.components

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// abstract listener to detect when the user scrolled to the bottom of a list
abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {
    private var previousTotalItems = 0
    private var loading = true
    private var currentPage = 0
    val THRESHOLD = 5 // load more when we are 5 items away from bottom

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        // if count increased, it means the loading finished
        if (loading && totalItemCount > previousTotalItems) {
            loading = false
            previousTotalItems = totalItemCount
        }

        // checks if we hit the threshold to load the next page
        if (!loading && (totalItemCount - visibleItemCount <= firstVisibleItemPosition + THRESHOLD)) {
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }

    // this needs to be implemented by the fragment to actually fetch data
    abstract fun onLoadMore(page: Int)
}