package io.keiji.sample.mastodonclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            val fragment = MainFragment()
            supportFragmentManager.beginTransaction()
                    .add(
                            R.id.fragment_container,
                            fragment,
                            MainFragment::class.java.simpleName
                    )
                    .commit()
        }
    }

}