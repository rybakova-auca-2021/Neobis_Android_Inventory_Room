package com.example.inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.inventory.databinding.ActivityMainBinding
import com.example.inventory.fargments.ArchiveFragment
import com.example.inventory.fargments.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainFragment = MainFragment()
        val archiveFragment = ArchiveFragment()

        setCurrentFragment(mainFragment)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId) {
                R.id.mainFragment-> setCurrentFragment(mainFragment)
                R.id.archiveFragment-> setCurrentFragment(archiveFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}