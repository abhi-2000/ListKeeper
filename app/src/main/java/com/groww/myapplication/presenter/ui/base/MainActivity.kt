package com.groww.myapplication.presenter.ui.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.FirebaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.groww.myapplication.R
import com.groww.myapplication.data.MySharedPreferences
import com.groww.myapplication.databinding.ActivityMainBinding
import com.groww.myapplication.domain.usecase.AddNewItemUseCase
import com.groww.myapplication.presenter.ui.main.RecyclerViewAdapter
import com.groww.myapplication.presenter.viewModel.BaseViewModel
import com.groww.myapplication.presenter.viewModel.BaseViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var baseViewModelFactory: BaseViewModelFactory

    @Inject
    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    private lateinit var binding: ActivityMainBinding
    private lateinit var baseViewModel: BaseViewModel

    @Inject
    lateinit var database: DatabaseReference

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory)[BaseViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initialize MySharedPreferences
        val mySharedPreferences = MySharedPreferences(this)

        CoroutineScope(Dispatchers.Main).launch() {
            binding.addNew.setOnClickListener {
                val dialog = BottomSheetDialog(this@MainActivity)

                val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
                dialog.setCancelable(true)
                dialog.setContentView(view)
                dialog.show()
                val titleText = dialog.findViewById<EditText>(R.id.titleEditText)
                val descriptionText = dialog.findViewById<EditText>(R.id.descriptionEditText)
                val addButton = dialog.findViewById<Button>(R.id.sheetAddButton)

                addButton?.setOnClickListener {
                    val retrievedValue = mySharedPreferences.getInt("intValue", 0)

                    Toast.makeText(this@MainActivity, "Toast", Toast.LENGTH_LONG).show()
                    if (titleText?.text.toString() != "" && descriptionText?.text.toString() != "") {
                        baseViewModel.writeNewItem(
                            database,
                            titleText?.text.toString(),
                            descriptionText?.text.toString(),
                            retrievedValue
                        )
                        mySharedPreferences.saveInt("intValue", retrievedValue + 1)

                        dialog.dismiss()
                    } else {
                        Log.i("TAG", "onCreate: Empty fields")
                    }
                }

                // Read from the database
//                myRef?.addValueEventListener(object : ValueEventListener {
//
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//                        val value = snapshot.getValue<String>()
//                        Log.d("TAG", "Value is: " + value)
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.w("TAG", "Failed to read value.", error.toException())
//                    }
//
//
//                })
            }
        }

    }

    override fun onResume() {
        CoroutineScope(Dispatchers.Main).async {
            binding.recyclerView.adapter = recyclerViewAdapter
            baseViewModel.getAllItems(recyclerViewAdapter,database)
        }
//
//        if(recyclerViewAdapter.itemCount==0){
//            binding.noItemAvailableLayout.isVisible=true
//            binding.recyclerView.isVisible=false
//        }
//        else{
//            binding.noItemAvailableLayout.isVisible=false
//            binding.recyclerView.isVisible=true
//
//        }
        super.onResume()
    }
}


/*
// Read from the database
myRef.addValueEventListener(object: ValueEventListener {

override fun onDataChange(snapshot: DataSnapshot) {
    // This method is called once with the initial value and again
    // whenever data at this location is updated.
    val value = snapshot.getValue<String>()
    Log.d(TAG, "Value is: " + value)
}

override fun onCancelled(error: DatabaseError) {
    Log.w(TAG, "Failed to read value.", error.toException())
}

})
 */