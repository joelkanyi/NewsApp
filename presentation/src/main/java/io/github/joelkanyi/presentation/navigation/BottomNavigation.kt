package io.github.joelkanyi.presentation.navigation

import io.github.joelkanyi.designsystem.R

enum class BottomNavigation(
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val route: Any
) {
    News(
        label = "News",
        selectedIcon = R.drawable.ic_home_filled,
        unselectedIcon = R.drawable.ic_home_outlined,
        route = Destinations.NewsList
    ),
    Favorites(
        label = "Favorites",
        selectedIcon = R.drawable.ic_favorite_filled,
        unselectedIcon = R.drawable.ic_favorite_outlined,
        route = Destinations.Favorites
    ),
}
