package com.gaboardi.githubtest.view.stargazers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.adapters.stargazers.StargazersAdapter
import com.gaboardi.githubtest.databinding.FragmentStargazersBinding
import com.gaboardi.githubtest.model.base.NetworkState
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.util.SpacingItemDecorator
import com.gaboardi.githubtest.util.px
import com.gaboardi.githubtest.viewmodel.stargazers.StargazersViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StargazersFragment : Fragment() {
    private val stargazersViewModel: StargazersViewModel by viewModel()
    private lateinit var binding: FragmentStargazersBinding
    private lateinit var usersAdapter: StargazersAdapter

    private val appExecutors: AppExecutors by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        arguments?.let {
            val fullName = StargazersFragmentArgs.fromBundle(it).repoFullName
            stargazersViewModel.setRepoFullName(fullName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stargazers, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = stargazersViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersAdapter = StargazersAdapter(appExecutors) {
            stargazersViewModel.refresh()
        }
        binding.stargazersRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.stargazersRecycler.adapter = usersAdapter
        binding.stargazersRecycler.addItemDecoration(SpacingItemDecorator(16.px, 16.px))
        observe()
        react()
    }

    private fun observe() {
        stargazersViewModel.stargazers.observe(this, Observer {
            usersAdapter.submitList(it)
            if (it.isNotEmpty()) {
                binding.lottie.isGone = true
                binding.refresh.isVisible = true
            } else {
                binding.lottie.isVisible = true
                binding.refresh.isGone = true
            }
        })
        stargazersViewModel.networkState.observe(this, Observer {
            usersAdapter.setNetworkState(it)
        })
        stargazersViewModel.refreshState.observe(this, Observer {
            binding.refresh.isRefreshing = it == NetworkState.LOADING
        })
    }

    private fun react() {
        binding.refresh.setOnRefreshListener { stargazersViewModel.refresh() }
        binding.back.setOnClickListener { findNavController().navigateUp() }
    }
}
