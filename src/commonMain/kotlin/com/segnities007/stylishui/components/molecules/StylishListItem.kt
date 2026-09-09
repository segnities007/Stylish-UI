package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.components.atoms.StylishText
import com.segnities007.stylishui.components.models.StylishTextRole
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme

/** A labeled row with optional supporting text and one optional interaction target. */
@Composable
public fun StylishListItem(
    title: String,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    require(title.isNotBlank()) { "List item title must not be blank" }
    val colors = StylishTheme.colorScheme
    val textColor = colors.onSurface.copy(alpha = if (enabled) 1f else 0.38f)
    val target = if (onClick == null) modifier else modifier.clickable(
        enabled = enabled, role = Role.Button, onClick = onClick,
    )
    ListItem(
        headlineContent = { StylishText(title) },
        supportingContent = supportingText?.let { text ->
            { StylishText(text, role = StylishTextRole.Supporting) }
        },
        modifier = target.stylishTestTag("list_item")
            .heightIn(min = StylishTheme.dimensions.iconButtonMinSize),
        colors = ListItemDefaults.colors(
            containerColor = colors.surface,
            headlineColor = textColor,
            supportingColor = colors.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.38f),
        ),
    )
}

@Preview(name = "List item — light")
@Preview(name = "List item — dark", uiMode = 0x20)
@Composable
private fun StylishListItemPreview() {
    StylishTheme { StylishListItem("Account", "Manage your details", onClick = {}) }
}
