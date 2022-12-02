package com.practice.branchinternapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.branchinternapp.util.AuthTokenManager
import com.practice.branchinternapp.R
import com.practice.branchinternapp.databinding.FragmentListOfMessageBinding
import com.practice.branchinternapp.domain.data.MessageListResponse
import com.practice.branchinternapp.domain.viewmodel.NewViewModel
import com.practice.branchinternapp.presentation.adapter.ListOfMessageAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListOfMessageFragment : Fragment() {

    lateinit var binding: FragmentListOfMessageBinding
    lateinit var navController: NavController
    lateinit var viewModel: NewViewModel
    private lateinit var listOfMessageAdapter: ListOfMessageAdapter

    @Inject
    lateinit var tokenManager: AuthTokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Oncreate", "oncreatemessagaefraglist")
        binding = FragmentListOfMessageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[NewViewModel::class.java]


        val authToken = arguments?.getString("auth_token")
        viewModel.listofMssgResponse(authToken.toString())
        listMessages(authToken)

        //back
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
                activity?.finishAffinity()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.i("Oncreate", "oncreatemessagaefragistview")
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.logoutBtn.setOnClickListener {
            tokenManager.saveToken(null)
            navController.navigate(R.id.action_listOfMessageFragment_to_authFragment)
        }

    }

    private fun listMessages(authToken: String?) {

        lifecycleScope.launchWhenStarted {

            viewModel.mssglistofState.collect { response ->
                when (response) {
                    is NewViewModel.MessageListState.Success -> {
                        hideProgressBar()
                        setUpRecyclerView(response.data, authToken)
                        Log.i("responseList", response.data.toString())
                    }
                    is NewViewModel.MessageListState.Error -> {
                        Toast.makeText(requireContext(), "Error Occured.", Toast.LENGTH_SHORT)
                        Log.i("responseList", response.message)
                    }
                    is NewViewModel.MessageListState.Loading -> {
                        showProgressBar()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Sorry for delay.", Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(data: ArrayList<MessageListResponse>, authToken: String?) {
        val hashSet = HashSet<String>()
        val resSet = HashSet<MessageListResponse>()

        data.sortByDescending {
            it.timestamp
        }
        for (i in data) {

            Log.i(
                "messageis",
                i.body.toString() + " " + i.timestamp.toString() + " " + i.user_id.toString()
            )

            if (!hashSet.contains(i.user_id)) {
                hashSet.add(i.user_id.toString())
                resSet.add(i)
            }
        }

        data.clear()

        data.addAll(resSet)
        data.sortByDescending {
            it.timestamp
        }


        Log.i("valuevataoobject", data.toString())
        listOfMessageAdapter = ListOfMessageAdapter(data, navController, authToken!!)
        binding.listOfMessagesRecyclerview.apply {
            adapter = listOfMessageAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.listOfMessagesRecyclerview.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.listOfMessagesRecyclerview.visibility = View.VISIBLE
    }
}