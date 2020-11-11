package io.keiji.sample.mastodonclient

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout

class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            val fragment = TootListFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, TootListFragment.TAG)
                    .commit()
        }
    }

}

