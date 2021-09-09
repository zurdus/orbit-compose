package kiwi.orbit.compose.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kiwi.orbit.compose.catalog.screens.AlertScreen
import kiwi.orbit.compose.catalog.screens.BadgeScreen
import kiwi.orbit.compose.catalog.screens.ButtonScreen
import kiwi.orbit.compose.catalog.screens.CheckboxScreen
import kiwi.orbit.compose.catalog.screens.ColorsScreen
import kiwi.orbit.compose.catalog.screens.IconsScreen
import kiwi.orbit.compose.catalog.screens.IllustrationsScreen
import kiwi.orbit.compose.catalog.screens.MainScreen
import kiwi.orbit.compose.catalog.screens.RadioScreen
import kiwi.orbit.compose.catalog.screens.StepperScreen
import kiwi.orbit.compose.catalog.screens.SwitchScreen
import kiwi.orbit.compose.catalog.screens.TagScreen
import kiwi.orbit.compose.catalog.screens.TextFieldScreen
import kiwi.orbit.compose.catalog.screens.TypographyScreen
import kiwi.orbit.compose.catalog.screens.XItineraryScreen
import kiwi.orbit.compose.catalog.screens.XProfileScreen

private object MainDestinations {
    const val MAIN = "main"

    const val COLORS = "colors"
    const val ICONS = "icons"
    const val ILLUSTRATIONS = "illustrations"
    const val TYPOGRAPHY = "typography"

    const val ALERT = "alert"
    const val BADGE = "badge"
    const val BUTTON = "button"
    const val CHECKBOX = "checkbox"
    const val RADIO = "radio"
    const val STEPPER = "stepper"
    const val SWITCH = "switch"
    const val TAG = "tag"
    const val TEXT_FIELD = "text_field"

    const val X_ITINERARY = "x_itinerary"
    const val X_PROFILE = "x_profile"
}

@Composable
fun NavGraph(
    startDestination: String = MainDestinations.MAIN,
    onToggleTheme: () -> Unit,
) {
    val navController = rememberNavController()

    val actions = remember(navController) { MainActions(navController) }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.MAIN) {
            MainScreen(actions, onToggleTheme)
        }

        composable(MainDestinations.COLORS) {
            ColorsScreen(actions::navigateUp)
        }
        composable(MainDestinations.ICONS) {
            IconsScreen(actions::navigateUp)
        }
        composable(MainDestinations.ILLUSTRATIONS) {
            IllustrationsScreen(actions::navigateUp)
        }
        composable(MainDestinations.TYPOGRAPHY) {
            TypographyScreen(actions::navigateUp)
        }

        composable(MainDestinations.ALERT) {
            AlertScreen(actions::navigateUp)
        }
        composable(MainDestinations.BADGE) {
            BadgeScreen(actions::navigateUp)
        }
        composable(MainDestinations.BUTTON) {
            ButtonScreen(actions::navigateUp)
        }
        composable(MainDestinations.CHECKBOX) {
            CheckboxScreen(actions::navigateUp)
        }
        composable(MainDestinations.RADIO) {
            RadioScreen(actions::navigateUp)
        }
        composable(MainDestinations.STEPPER) {
            StepperScreen(actions::navigateUp)
        }
        composable(MainDestinations.SWITCH) {
            SwitchScreen(actions::navigateUp)
        }
        composable(MainDestinations.TAG) {
            TagScreen(actions::navigateUp)
        }
        composable(MainDestinations.TEXT_FIELD) {
            TextFieldScreen(actions::navigateUp)
        }

        composable(MainDestinations.X_ITINERARY) {
            XItineraryScreen(actions::navigateUp)
        }

        composable(MainDestinations.X_PROFILE) {
            XProfileScreen(actions::navigateUp)
        }
    }
}

class MainActions(
    private val navController: NavHostController,
) {
    fun navigateUp() {
        navController.navigateUp()
    }

    fun showColors() {
        navController.navigate(MainDestinations.COLORS)
    }

    fun showIcons() {
        navController.navigate(MainDestinations.ICONS)
    }

    fun showIllustrations() {
        navController.navigate(MainDestinations.ILLUSTRATIONS)
    }

    fun showTypography() {
        navController.navigate(MainDestinations.TYPOGRAPHY)
    }

    fun showAlert() {
        navController.navigate(MainDestinations.ALERT)
    }

    fun showBadge() {
        navController.navigate(MainDestinations.BADGE)
    }

    fun showButton() {
        navController.navigate(MainDestinations.BUTTON)
    }

    fun showCheckbox() {
        navController.navigate(MainDestinations.CHECKBOX)
    }

    fun showRadio() {
        navController.navigate(MainDestinations.RADIO)
    }

    fun showStepper() {
        navController.navigate(MainDestinations.STEPPER)
    }

    fun showSwitch() {
        navController.navigate(MainDestinations.SWITCH)
    }

    fun showTag() {
        navController.navigate(MainDestinations.TAG)
    }

    fun showTextField() {
        navController.navigate(MainDestinations.TEXT_FIELD)
    }

    fun showXItinerary() {
        navController.navigate(MainDestinations.X_ITINERARY)
    }

    fun showXProfile() {
        navController.navigate(MainDestinations.X_PROFILE)
    }
}
