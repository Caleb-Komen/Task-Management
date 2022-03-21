package com.taskmanager.taskmanagement.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.databinding.ActivityMainBinding
import com.taskmanager.taskmanagement.databinding.NewProjectDialogBinding
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.ui.auth.AuthActivity
import com.taskmanager.taskmanagement.ui.projects.NewProjectDialog
import com.taskmanager.taskmanagement.ui.projects.ProjectsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.dashboardFragment, R.id.projectsFragment), binding.drawerLayout)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    fun navigateToAuthActivity(){
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    fun showNewProjectDialog(){
        val intent = Intent(this, NewProjectDialog::class.java)
        startActivity(intent)
    }

}