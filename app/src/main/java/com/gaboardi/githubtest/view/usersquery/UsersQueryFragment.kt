package com.gaboardi.githubtest.view.usersquery


import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.adapters.users.UsersAdapter
import com.gaboardi.githubtest.databinding.FragmentUsersQueryBinding
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.util.SpacingItemDecorator
import com.gaboardi.githubtest.util.dismissKeyboard
import com.gaboardi.githubtest.util.px
import com.gaboardi.githubtest.viewmodel.usersquery.UsersQueryViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersQueryFragment : Fragment() {
    private val usersViewModel: UsersQueryViewModel by viewModel()
    private lateinit var binding: FragmentUsersQueryBinding
    private lateinit var usersAdapter: UsersAdapter

    private val appExecutors: AppExecutors by inject()

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
            //Navigation
        }, onRetry = {
            usersViewModel.refresh()
        })
        binding.usersRecycler.adapter = usersAdapter
        binding.usersRecycler.addItemDecoration(SpacingItemDecorator(16.px, 16.px))
        binding.usersRecycler.setLayoutReference(R.layout.shimmer_user_item)
        binding.usersRecycler.stopShimmering()
        observe()
        react()
    }

    private fun observe() {
        usersViewModel.users.observe(this, Observer {
            usersAdapter.submitList(it)
            if(it.isNotEmpty()){
                binding.lottie.isGone = true
            }else{
                binding.lottie.isVisible = true
            }
        })
        usersViewModel.networkState.observe(this, Observer {
            usersAdapter.setNetworkState(it)
        })
        usersViewModel.refreshState.observe(this, Observer {

        })
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
    }

    private fun doSearch() {
        val query = binding.query.text.toString()
        dismissKeyboard(binding.query.windowToken)
        usersViewModel.setQuery(query)
    }
}
