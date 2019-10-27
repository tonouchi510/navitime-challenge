package com.example.navitime_challenge

/*
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.navitime_challenge.network.NavitimeApi
import com.example.navitime_challenge.viewmodel.HomeViewModel
import com.google.firebase.firestore.FirebaseFirestore

class FragmentHome: Fragment(){

    private val TAG = "----------------------------------------------------"
    private lateinit var mActivity: MainActivity
    private lateinit var location: Location

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    lateinit var mFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = activity as MainActivity
        location = mActivity.location
        mFirestore = mActivity.mFirestore

        //val getRoute = NavitimeApi.retrofitService.getBestRoute()
        //Log.w(TAG, getRoute.toString())

        val orders = mFirestore.collection("orders")
            .whereEqualTo("status", 0)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        return binding.root

         */
    }

}

 */
