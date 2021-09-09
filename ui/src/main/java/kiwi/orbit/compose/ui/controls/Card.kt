package kiwi.orbit.compose.ui.controls

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kiwi.orbit.compose.ui.OrbitTheme
import kiwi.orbit.compose.ui.foundation.contentColorFor

@Composable
public fun Card(
    modifier: Modifier = Modifier,
    shape: Shape = OrbitTheme.shapes.normal,
    backgroundColor: Color = OrbitTheme.colors.surface.main,
    contentColor: Color = contentColorFor(backgroundColor),
    border: BorderStroke? = null,
    elevation: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        border = border,
        content = content
    )
}

@Composable
public fun Card(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = OrbitTheme.shapes.normal,
    backgroundColor: Color = OrbitTheme.colors.surface.main,
    contentColor: Color = contentColorFor(backgroundColor),
    border: BorderStroke? = null,
    elevation: Dp = 1.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = LocalIndication.current,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
        border = border,
        elevation = elevation,
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        content = content
    )
}
