package edu.put.myapplication

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.put.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (trailList.size == 0) {
            populateTrails()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Some Action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_advanced, R.id.nav_intermediate, R.id.nav_beginner, R.id.nav_home, R.id.nav_timer
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun populateTrails() {
        val jsonTrails = parseJsonToModel(this)
        jsonTrails.forEach { trail ->
            trailList.add(
                Trail(
                    trail.name,
                    trail.description,
                    trail.img,
                    resources.getIdentifier(trail.img, "drawable", this.packageName),
                    trail.difficulty,
                    trail.stages
                )
            )
        }
    }

    private fun parseJsonToModel(context: Context): List<Trail> {
        return Gson().fromJson(
            context.assets.open("hiking_trails.json").bufferedReader().use { it.readText() },
            object : TypeToken<List<Trail>>() {}.type)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
// TODO
// 1. Do szczegółu dodać czas przejscia poszczególnych odcinków szlaku w zaleznosci od stylu chodzenia
// 2. Wersja na tablet
// 3. Stoper we fragmencie, działa po zminimalizowaniu aplikacji. Dodac ikony do start stop,
// zapamietanie wynikow i mozliwosc wglądu w nie.
// 4. Dark theme
// 5. Wyszukiwanie szlaku w pasku narzędzi
// 6. Animacje strzałek albo Launch albo przechodzenia pomiedzy szlakami
// 7. Aparat