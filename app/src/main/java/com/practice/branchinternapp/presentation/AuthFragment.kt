package com.practice.branchinternapp.presentation

import android.os.Bundle
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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.practice.branchinternapp.util.AuthTokenManager
import com.practice.branchinternapp.R
import com.practice.branchinternapp.databinding.FragmentAuthBinding
import com.practice.branchinternapp.domain.data.AuthModel
import com.practice.branchinternapp.domain.viewmodel.NewViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {
    lateinit var binding: FragmentAuthBinding
    lateinit var navController: NavController
    lateinit var viewModel: NewViewModel

    @Inject
    lateinit var tokenManager: AuthTokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[NewViewModel::class.java]
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
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        if (tokenManager.getToken() != null) {
            val bundle = bundleOf(
                "auth_token" to tokenManager.getToken()
            )
            navController.navigate(
                R.id.action_authFragment_to_listOfMessageFragment,
                bundle
            )
        }

        binding.loginBtn.setOnClickListener {

            if ((binding.emailIdAuth.text.isNotEmpty()) and (binding.paswdAuth.text.isNotEmpty()) and (binding.emailIdAuth.text.contains(
                    "@"
                ))
            ) {
                val authModel = AuthModel(
                    binding.emailIdAuth.text.toString(),
                    binding.paswdAuth.text.toString()
                )
                viewModel.listofResponse(
                    authModel
                )
                getTheOutputResponse()
            } else {
                Toast.makeText(context, "Wrong Email or password!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun getTheOutputResponse() {

        lifecycleScope.launchWhenStarted {
            viewModel.listofState.collect { response ->
                Log.i("blockt", response.toString())

                when (response) {

                    is NewViewModel.ListState.Success -> {

                        Log.i("successblockt", response.toString())
                        tokenManager.saveToken(response.data.auth_token)
                        val bundle = bundleOf(
                            "auth_token" to response.data.auth_token
                        )
                        navController.navigate(
                            R.id.action_authFragment_to_listOfMessageFragment,
                            bundle
                        )


                    }

                    is NewViewModel.ListState.Error -> {

                        Toast.makeText(context, "Wrong Email or password!", Toast.LENGTH_SHORT)
                            .show()

                    }

                    else -> {

                        Log.i("elseblockt", response.toString())
//                        Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }



}