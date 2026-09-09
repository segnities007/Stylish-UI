package com.segnities007.stylishui.catalog.materialfree

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishSurface
import com.segnities007.stylishui.components.atoms.StylishText
import com.segnities007.stylishui.components.atoms.StylishIcon
import com.segnities007.stylishui.components.models.StylishIcons
import com.segnities007.stylishui.components.atoms.rememberStylishSnackbarHostState
import com.segnities007.stylishui.components.models.StylishSnackbarDuration
import com.segnities007.stylishui.components.molecules.StylishColumn
import com.segnities007.stylishui.components.molecules.StylishListItem
import com.segnities007.stylishui.components.molecules.StylishSnackbarHost
import com.segnities007.stylishui.theme.stylishLayerColor
import kotlinx.coroutines.launch

/** Flexible consumer proving theme, content colors, state types and feedback use Stylish imports. */
@Composable
internal fun ComponentScreen() {
    val snackbar = rememberStylishSnackbarHostState()
    val scope = rememberCoroutineScope()
    StylishSurface(color = stylishLayerColor(level = 0.25f)) {
        StylishColumn {
            StylishIcon(StylishIcons.Person, "Account")
            StylishListItem("Account", "Your preferences")
            StylishButton(onClick = {
                scope.launch {
                    snackbar.showSnackbar("Saved", duration = StylishSnackbarDuration.Short)
                }
            }) { StylishText("Save") }
            StylishSnackbarHost(hostState = snackbar)
        }
    }
}
