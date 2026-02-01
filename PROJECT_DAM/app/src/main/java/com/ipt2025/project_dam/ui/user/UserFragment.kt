package com.ipt2025.project_dam.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.components.EndlessScrollListener
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.UsersAPIService
import com.ipt2025.project_dam.databinding.FragmentUserListBinding
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class UserFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserRecyclerViewAdapter
    private var currentPage = 1
    private val PAGE_LIMIT = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check permission and navigate back if unauthorized
        if (!RetrofitProvider.canViewUsers()) {
            Toast.makeText(context, "Access denied - Admin only", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        setupRecyclerView()
        setupClickListeners()
        setupUIBasedOnPermissions()
    }

    override fun onResume() {
        super.onResume()
        // repopulate recycler view list when user returns back to the user list fragment
        refreshUserList()
    }

    private fun refreshUserList() {
        // Clear existing data and reset to page 1
        adapter.clearUsers()
        currentPage = 1
        fetchUsers(currentPage, PAGE_LIMIT)
    }

    private fun setupUIBasedOnPermissions() {
        // Hide the add user button if user doesn't have permission
        if (!RetrofitProvider.canCreateUser()) {
            binding.btnAddUser.visibility = View.GONE
        } else {
            binding.btnAddUser.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        // Initialize adapter with click handler
        adapter = UserRecyclerViewAdapter(mutableListOf()) { user ->
            val bundle = Bundle().apply {
                putString("_id", user.id)
            }
            findNavController().navigate(R.id.action_userFragment_to_userDetailsFragment, bundle)
        }

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.addOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore(page: Int) {
                currentPage++
                fetchUsers(currentPage, PAGE_LIMIT)
            }
        })
        binding.list.adapter = adapter
    }

    private fun fetchUsers(page: Int, limit: Int) {
        val apiService = RetrofitProvider.create(UsersAPIService::class.java)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = apiService.getUsers(page = page, limit = limit)
                adapter.addUsers(response.users)
            } catch (e: CancellationException) { } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    context,
                    "Error fetching users: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnAddUser.setOnClickListener {
            // Validate permission before navigating
            if (RetrofitProvider.canCreateUser()) {
                findNavController().navigate(R.id.action_userFragment_to_addEditUserFragment)
            } else {
                Toast.makeText(
                    context,
                    "You don't have permission to create users",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}