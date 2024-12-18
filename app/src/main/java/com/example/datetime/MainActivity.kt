package com.example.datetime

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.datetime.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var bitmap: Uri? = null
    private var date: LocalDate? = null
    private val imagePicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            bitmap = it
            binding.avatarIV.setImageURI(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.apply {
            avatarIV.setOnClickListener {
                imagePicker.launch(PickVisualMediaRequest
                    .Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build())
            }
            dateOfBirthET.setOnClickListener {
                val picker = MaterialDatePicker.Builder
                    .datePicker()
                    .setTitleText(R.string.select_birth_date)
                    .build()
                picker.addOnPositiveButtonClickListener {
                    picker.selection?.let {
                        date = it.toLocalDateViaUTC()
                        dateOfBirthET.setText(date?.format())
                    }
                }
                picker.show(supportFragmentManager, "DatePicker")
            }
            saveBTN.setOnClickListener {
                if (bitmap == null || date == null || nameET.text.isBlank() || lastNameET.text.isBlank()) {
                    Toast.makeText(this@MainActivity, getString(R.string.fill_all_before_proceed), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val intent = Intent(this@MainActivity, CardActivity::class.java).apply {
                    putExtra(CardActivity.KEY_PERSON, Person(
                        name = nameET.text.toString(),
                        lastName = lastNameET.text.toString(),
                        dateOfBirth = date!!,
                        bitmap = bitmap!!.toString()
                    ))
                }
                startActivity(intent)
            }
        }
    }
}

data class Person(
    val name: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val bitmap: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readSerializable() as LocalDate,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(lastName)
        parcel.writeSerializable(dateOfBirth)
        parcel.writeString(bitmap)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}

