package io.github.joelkanyi.presentation.navigation

import io.github.joelkanyi.designsystem.R

enum class BottomNavigation(
    val label: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val route: Any,
) {
    News(
        label = io.github.joelkanyi.presentation.R.string.news,
        selectedIcon = R.drawable.ic_home_filled,
        unselectedIcon = R.drawable.ic_home_outlined,
        route = Destinations.NewsList
    ),
    Favorites(
        label = io.github.joelkanyi.presentation.R.string.favorites,
        selectedIcon = R.drawable.ic_favorite_filled,
        unselectedIcon = R.drawable.ic_favorite_outlined,
        route = Destinations.Favorites
    ),
}
