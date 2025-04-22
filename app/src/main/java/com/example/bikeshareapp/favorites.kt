package com.example.bikeshareapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class favorites : AppCompatActivity() {


    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private val favoriteStations = mutableListOf<CombinedStation>()
    val fullStationList = MainActivity.allStations


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorites)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        recyclerView = findViewById<RecyclerView>(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        dbRef = FirebaseDatabase.getInstance().reference.child("favorites").child(userId)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteStations.clear()

                for (favSnap in snapshot.children) {
                    val stationName = favSnap.key ?: continue

                    val matched = fullStationList.find { it.name == stationName }
                    if (matched != null) {
                        favoriteStations.add(matched)
                    }

                }

                recyclerView.adapter = CombinedStationAdapter(favoriteStations)
            }



            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@favorites, "Error loading favorites", Toast.LENGTH_SHORT).show()
            }
        })

    }
}