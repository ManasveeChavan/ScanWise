package com.scanwise.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.scanwise.app.ui.AppViewModel
import com.scanwise.app.ui.history.HistoryScreen
import com.scanwise.app.ui.result.ResultScreen
import com.scanwise.app.ui.scanner.ScannerScreen
import com.scanwise.app.ui.settings.SettingsScreen
import com.scanwise.app.ui.theme.ScanWiseTheme

private object Routes {
    const val SCANNER = "scanner"
    const val RESULT = "result"
    const val HISTORY = "history"
    const val SETTINGS = "settings"
}

class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels {
        AppViewModel.factory((application as ScanWiseApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScanWiseTheme {
                Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                    ScanWiseApp(viewModel)
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun ScanWiseApp(viewModel: AppViewModel) {
    val navController = rememberNavController()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val lastResult by viewModel.lastResult.collectAsState()
    val history by viewModel.historyFlow.collectAsState(initial = emptyList())
    val stats by viewModel.stats.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    val navItems = listOf(
        Triple(Routes.SCANNER, "Scan", Icons.Filled.QrCodeScanner),
        Triple(Routes.HISTORY, "History", Icons.Filled.History),
        Triple(Routes.SETTINGS, "Settings", Icons.Filled.Settings),
    )

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            NavigationBar {
                navItems.forEach { (route, label, icon) ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SCANNER,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.SCANNER) {
                ScannerScreen(isAnalyzing = isAnalyzing) { url ->
                    viewModel.scanUrl(url) {
                        navController.navigate(Routes.RESULT)
                    }
                }
            }
            composable(Routes.RESULT) {
                lastResult?.let { result ->
                    ResultScreen(
                        result = result,
                        onOpenUrl = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        onBlockDomain = { domain ->
                            viewModel.blockDomain(domain, result.url)
                        },
                        onReport = { /* future: submit report to a threat intel service */ },
                    )
                }
            }
            composable(Routes.HISTORY) {
                HistoryScreen(
                    history = history,
                    stats = stats,
                    onDelete = { id -> viewModel.deleteScans(listOf(id)) },
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen()
            }
        }
    }
}
