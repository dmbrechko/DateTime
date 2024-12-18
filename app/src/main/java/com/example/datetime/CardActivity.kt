package com.example.datetime

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.datetime.databinding.ActivityCardBinding
import java.time.LocalDate
import java.time.MonthDay
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

class CardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val person = intent.getParcelableExtra<Person>(KEY_PERSON) ?: throw IllegalStateException("Person must be supplied")
        binding.apply {
            setSupportActionBar(toolbar)
            avatarIV.setImageURI(Uri.parse(person.bitmap))
            nameTV.text = person.name
            lastNameTV.text = person.lastName
            val date = person.dateOfBirth
            val now = LocalDate.now()
            val age = date.until(now, ChronoUnit.YEARS)
            val monthDayOfBirth = MonthDay.from(date)
            val nextBirthday = now.with(nextMonthDay(monthDayOfBirth))
            val monthsToBirthDay = now.until(nextBirthday, ChronoUnit.MONTHS)
            val daysToBirthDay = now.plusMonths(monthsToBirthDay).until(nextBirthday, ChronoUnit.DAYS)
            val dateInfo = "Age: $age years, left until next birthday: $monthsToBirthDay months, $daysToBirthDay days"
            dateInfoTV.text = dateInfo
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.card_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_exit -> {
                moveTaskToBack(true)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val KEY_PERSON = "key person"
    }
}