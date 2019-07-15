package com.gaboardi.githubtest.view.userrepos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.adapters.repos.ReposAdapter
import com.gaboardi.githubtest.databinding.FragmentUserRepositoriesBinding
import com.gaboardi.githubtest.model.base.NetworkState
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.util.SpacingItemDecorator
import com.gaboardi.githubtest.util.px
import com.gaboardi.githubtest.viewmodel.userrepos.UserReposViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserRepositoriesFragment : Fragment() {
    private val reposViewModel: UserReposViewModel by viewModel()
    private lateinit var binding: FragmentUserRepositoriesBinding
    private lateinit var usersAdapter: ReposAdapter

    private val appExecutors: AppExecutors by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        arguments?.let {
            val user = UserRepositoriesFragmentArgs.fromBundle(it).user
            reposViewModel.setUser(user)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_repositories, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = reposViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersAdapter = ReposAdapter(appExecutors, onCLick = {
            //Navigation
        }, onRetry = {
            reposViewModel.refresh()
        })
        binding.repoRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.repoRecycler.adapter = usersAdapter
        binding.repoRecycler.addItemDecoration(SpacingItemDecorator(horizontal = 0, vertical = 16.px))
        binding.repoRecycler.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        observe()
        react()
    }

    private fun observe() {
        reposViewModel.repos.observe(this, Observer {
            usersAdapter.submitList(it)
            if(it.isNotEmpty()){
                binding.lottie.isGone = true
                binding.refresh.isVisible = true
            }else{
                binding.lottie.isVisible = true
                binding.refresh.isGone = true
            }
        })
        reposViewModel.networkState.observe(this, Observer {
            usersAdapter.setNetworkState(it)
        })
        reposViewModel.refreshState.observe(this, Observer {
            binding.refresh.isRefreshing = it == NetworkState.LOADING
        })
    }

    private fun react() {
        binding.refresh.setOnRefreshListener { reposViewModel.refresh() }
    }
}
