package com.practice.branchinternapp.presentation

import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.branchinternapp.R
import com.practice.branchinternapp.databinding.FragmentMessageBinding
import com.practice.branchinternapp.domain.data.MessageListResponse
import com.practice.branchinternapp.domain.data.ParticularMessage
import com.practice.branchinternapp.domain.viewmodel.NewViewModel
import com.practice.branchinternapp.presentation.adapter.ListOfMessageAdapter
import com.practice.branchinternapp.presentation.adapter.ParticularMessageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment() {

    private lateinit var navController: NavController
    lateinit var viewModel: NewViewModel
    private var listofdata: ArrayList<MessageListResponse> = arrayListOf()
    private lateinit var listOfMessageAdapter: ParticularMessageAdapter
    private lateinit var binding: FragmentMessageBinding
    var check: String = null.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Oncreate", "oncreatemessagaefrag")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[NewViewModel::class.java]

        val authToken = arguments?.getString("auth_token")
        val threadId = arguments?.getInt("thread_id")

        viewModel.listofMssgResponse(authToken.toString())

        listMessages(authToken, threadId)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bundle = bundleOf(
                    "auth_token" to authToken
                )
                navController.navigate(R.id.action_messageFragment_to_listOfMessageFragment, bundle)
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val authToken = arguments?.getString("auth_token")
        val threadId = arguments?.getInt("thread_id")

        binding.sendMssgBtn.setOnClickListener {

            val particularMessage =
                ParticularMessage(threadId, binding.bodyEdittext.text.toString())
            Log.i("dataofdataclass1", particularMessage.toString())
            viewModel.listofParticularMssgResponse(authToken!!, particularMessage)
            listOfParticularMessage(authToken, threadId)

            binding.bodyEdittext.text.clear()
        }

    }

    private fun listOfParticularMessage(authToken: String?, threadId: Int?) {
        lifecycleScope.launchWhenStarted {

            viewModel.particularmsglistofState.collect { response ->
                when (response) {
                    is NewViewModel.ParticularMssgListState.Success -> {

                        Log.i(
                            "dataofdataclass2",
                            response.data.timestamp.toString() + " and " + check.toString()
                        )
                        if (check != response.data.timestamp.toString()) {
                            check = response.data.timestamp.toString()
                            listofdata.add(response.data)
                            setUpRecyclerView(listofdata, authToken, threadId)

                        } else {

                            check = response.data.timestamp.toString()
                        }

                    }
                    is NewViewModel.ParticularMssgListState.Error -> {
                        Toast.makeText(requireContext(), "Error Occured.", Toast.LENGTH_SHORT)
                        Log.i("responseList", response.message)
                    }

                    else -> {
                        Toast.makeText(requireContext(), "Sorry for delay.", Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }

    private fun listMessages(authToken: String?, threadId: Int?) {

        lifecycleScope.launchWhenStarted {

            viewModel.mssglistofState.collect { response ->
                when (response) {
                    is NewViewModel.MessageListState.Success -> {

                        listofdata.addAll(response.data)
                        setUpRecyclerView(listofdata, authToken, threadId)
                        Log.i("responseList", response.data.toString())

                    }
                    is NewViewModel.MessageListState.Error -> {
                        Toast.makeText(requireContext(), "Error Occured.", Toast.LENGTH_SHORT)
                        Log.i("responseList", response.message)
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Sorry for delay.", Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(
        data: ArrayList<MessageListResponse>,
        authToken: String?,
        threadId: Int?
    ) {

        val dummydata: ArrayList<MessageListResponse> = arrayListOf()
        Log.i("threadid", threadId.toString())
        for (i in data) {
            if (i.thread_id == threadId) {
                dummydata.add(i)
            }
        }
        dummydata.sortByDescending {
            it.timestamp
        }

        Log.i("valuevatdummy", dummydata.toString())
        listOfMessageAdapter = ParticularMessageAdapter(dummydata, navController, authToken!!)
        binding.threadOfMessagesRecyclerview.apply {
            adapter = listOfMessageAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        listOfMessageAdapter.notifyDataSetChanged()
    }


}