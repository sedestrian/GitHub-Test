package com.gaboardi.githubtest.view.usersquery


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.adapters.users.UsersAdapter
import com.gaboardi.githubtest.databinding.FragmentUsersQueryBinding
import com.gaboardi.githubtest.model.base.NetworkState
import com.gaboardi.githubtest.model.base.Status
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.util.SpacingItemDecorator
import com.gaboardi.githubtest.util.dismissKeyboard
import com.gaboardi.githubtest.util.px
import com.gaboardi.githubtest.viewmodel.usersquery.UsersQueryViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class UsersQueryFragment : Fragment() {
    private val usersViewModel: UsersQueryViewModel by viewModel()
    private lateinit var binding: FragmentUsersQueryBinding
    private lateinit var usersAdapter: UsersAdapter

    private val appExecutors: AppExecutors by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_query, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = usersViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersAdapter = UsersAdapter(appExecutors, onCLick = {
            findNavController().navigate(
                UsersQueryFragmentDirections.actionUsersQueryFragmentToUserRepositoriesFragment(
                    it.login
                )
            )
        }, onRetry = {
            usersViewModel.refresh()
        })
        binding.refresh.setProgressViewOffset(true, 0, 78.px)
        binding.usersRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.usersRecycler.adapter = usersAdapter
        binding.usersRecycler.addItemDecoration(SpacingItemDecorator(16.px, 16.px))
        observe()
        react()
    }

    private fun observe() {
        usersViewModel.users.observe(this, Observer {
            usersAdapter.submitList(it)
            val query = usersViewModel.currentQuery()
            if (it.isNotEmpty() || (query != null && query.isNotBlank())) {
                binding.lottie.isGone = true
                binding.refresh.isVisible = true
            } else if(query.isNullOrBlank()){
                binding.lottie.isVisible = true
                binding.refresh.isGone = true
            }
        })
        usersViewModel.networkState.observe(this, Observer {
            usersAdapter.setNetworkState(it)
            usersViewModel.handleNetworkState(it.status)
        })
        usersViewModel.refreshState.observe(this, Observer {
            binding.refresh.isRefreshing = it == NetworkState.LOADING
            usersAdapter.setNetworkState(it)
            usersViewModel.handleNetworkState(it.status)
        })
        usersViewModel.networkAvailable.observe(this, Observer {
            if(!it){
                showNoNetworkMessage()
            }
        })
    }

    private fun showNoNetworkMessage() {
        val snack = Snackbar.make(binding.root, getString(R.string.youre_offline), Snackbar.LENGTH_INDEFINITE)
        snack.setAction(R.string.retry) {
            usersViewModel.refresh()
            snack.dismiss()
        }
        snack.show()
    }

    private fun react() {
        binding.query.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_NEXT
            ) {
                doSearch()
                true
            } else {
                false
            }
        }
        binding.query.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch()
                true
            } else {
                false
            }
        }
        binding.refresh.setOnRefreshListener { usersViewModel.refresh() }
    }

    private fun doSearch() {
        val query = binding.query.text.toString()
        dismissKeyboard(binding.query.windowToken)
        usersViewModel.setQuery(query)
    }
}
