import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

val LazyListState.elevation
    @Composable
    get() = if (firstVisibleItemIndex == 0) minOf(
        with(LocalDensity.current){ (firstVisibleItemScrollOffset / 4).toDp()},
        AppBarDefaults.TopAppBarElevation
    )
    else AppBarDefaults.TopAppBarElevation


val LazyGridState.elevation
    get() = if (firstVisibleItemIndex == 0) minOf(
        firstVisibleItemScrollOffset.dp,
        AppBarDefaults.TopAppBarElevation
    )
    else AppBarDefaults.TopAppBarElevation


@Composable
fun MaterialTheme.overlayColor(@FloatRange(from = 0.0, to = 1.0) alpha: Float = 0.2f) =
    colors.onBackground.copy(alpha = alpha)


fun Modifier.topSafeDrawing(): Modifier = composed {
    this.windowInsetsPadding(insetsTopSafeDrawing())
}

fun Modifier.centerSafeDrawing(): Modifier = composed {
    this.windowInsetsPadding(insetsCenterSafeDrawing())
}

fun Modifier.bottomSafeDrawing(withIme: Boolean = true): Modifier = composed {
    windowInsetsPadding(insetsBottomSafeDrawing(withIme))
}

fun Modifier.onlyBottomSafeDrawing(withIme: Boolean = true): Modifier = composed {
    windowInsetsPadding(insetsOnlyBottomSafeDrawing(withIme))
}

@Composable
fun insetsTopSafeDrawing(): WindowInsets =
    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)

@Composable
fun insetsCenterSafeDrawing(): WindowInsets =
    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)

@Composable
fun insetsBottomSafeDrawing(withIme: Boolean = true): WindowInsets =
    if (withIme) {
        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
    } else {
        WindowInsets.safeDrawing.exclude(WindowInsets.ime).union(WindowInsets.navigationBars)
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
    }

@Composable
fun insetsOnlyBottomSafeDrawing(withIme: Boolean = true): WindowInsets =
    if (withIme) {
        WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
    } else {
        WindowInsets.safeDrawing.exclude(WindowInsets.ime).union(WindowInsets.navigationBars)
            .only(WindowInsetsSides.Bottom)
    }