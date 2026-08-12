package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A horizontally scrolling carousel that centers a focal item and shows
 * smaller sibling items on both sides (multi-browse layout).
 *
 * This is the Finish-layer wrapper around the Material 3
 * [HorizontalMultiBrowseCarousel] under the [ExperimentalMaterial3Api]
 * opt-in, so callers can use the carousel without importing the
 * experimental `androidx.compose.material3.carousel` API. The wrapper
 * remembers its own [CarouselState] from [itemCount] and forwards all
 * sizing parameters unchanged; the default fling behavior advances at most
 * one item per fling ([CarouselDefaults.singleAdvanceFlingBehavior]). Item
 * appearance is fully owned by the caller through [content]; for the Stylish
 * look, clip each item with
 * [CarouselItemScope.maskClip][CarouselItemScope.maskClip] using
 * [RoundedCornerShape] with
 * [StylishTheme.dimensions.connectedCornerRadius].
 *
 * @param itemCount A lambda returning the number of carousel items. Used to
 *   construct the default [state] via [rememberCarouselState]; ignored when
 *   a custom [state] is supplied.
 * @param preferredItemWidth The preferred width of the focal item, in dp.
 * @param state The [CarouselState] controlling scroll position and the
 *   current item. Defaults to [rememberCarouselState] with [itemCount].
 * @param modifier Modifier applied to the carousel root.
 * @param itemSpacing The gap between adjacent items. Defaults to 0 dp
 *   (matching the M3 default).
 * @param flingBehavior The fling behavior after a swipe gesture. Defaults to
 *   [CarouselDefaults.singleAdvanceFlingBehavior], which snaps to the next
 *   item.
 * @param userScrollEnabled Whether the user can scroll the carousel.
 *   Defaults to `true`.
 * @param minSmallItemWidth The minimum width a small (non-focal) item may
 *   shrink to. Defaults to [CarouselDefaults.MinSmallItemSize] (40 dp).
 * @param maxSmallItemWidth The maximum width a small (non-focal) item may
 *   grow to. Defaults to [CarouselDefaults.MaxSmallItemSize] (56 dp).
 * @param contentPadding Padding around the whole content, applied after
 *   clipping. Defaults to no padding.
 * @param content A composable that renders the carousel item at the given
 *   index. Receives [CarouselItemScope] for size-aware helpers such as
 *   [CarouselItemScope.maskClip].
 *
 * @see HorizontalMultiBrowseCarousel
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
public fun StylishCarousel(
    itemCount: () -> Int,
    preferredItemWidth: Dp,
    state: CarouselState = rememberCarouselState(itemCount = itemCount),
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 0.dp,
    flingBehavior: TargetedFlingBehavior =
        CarouselDefaults.singleAdvanceFlingBehavior(state = state),
    userScrollEnabled: Boolean = true,
    minSmallItemWidth: Dp = CarouselDefaults.MinSmallItemSize,
    maxSmallItemWidth: Dp = CarouselDefaults.MaxSmallItemSize,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable CarouselItemScope.(index: Int) -> Unit,
) {
    HorizontalMultiBrowseCarousel(
        state = state,
        preferredItemWidth = preferredItemWidth,
        modifier = modifier,
        itemSpacing = itemSpacing,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        minSmallItemWidth = minSmallItemWidth,
        maxSmallItemWidth = maxSmallItemWidth,
        contentPadding = contentPadding,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(name = "Stylish carousel", showBackground = true, widthDp = 393, heightDp = 200)
@Composable
private fun StylishCarouselPreview() {
    StylishTheme(darkTheme = false) {
        val primary = MaterialTheme.colorScheme.primary
        val tertiary = MaterialTheme.colorScheme.tertiary
        val secondary = MaterialTheme.colorScheme.secondary
        val error = MaterialTheme.colorScheme.error
        val colors = remember(primary, tertiary, secondary, error) {
            listOf(primary, tertiary, secondary, error)
        }
        StylishCarousel(
            itemCount = { colors.size },
            preferredItemWidth = 240.dp,
            modifier = Modifier.fillMaxWidth(),
            itemSpacing = StylishTheme.dimensions.inlineSpacing,
        ) { index ->
            Box(
                Modifier
                    .maskClip(RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius))
                    .background(colors[index % colors.size])
                    .height(120.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "項目 $index",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
