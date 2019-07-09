package com.gaboardi.githubtest.view.usersquery


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.adapters.users.UsersAdapter
import com.gaboardi.githubtest.util.SpacingItemDecorator
import com.gaboardi.githubtest.util.dp
import com.gaboardi.githubtest.util.px
import kotlinx.android.synthetic.main.fragment_users_query.*

class UsersQueryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users_query, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersRecycler.adapter = UsersAdapter()
        usersRecycler.addItemDecoration(SpacingItemDecorator(16.px, 16.px))
        usersRecycler.setLayoutReference(R.layout.shimmer_user_item)
        usersRecycler.shimmer()
        Handler().postDelayed({usersRecycler.stopShimmering()}, 5000)
    }
}
