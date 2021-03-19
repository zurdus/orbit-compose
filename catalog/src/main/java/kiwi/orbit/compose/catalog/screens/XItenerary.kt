package kiwi.orbit.compose.catalog.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kiwi.orbit.compose.catalog.SubScreen
import kiwi.orbit.compose.icons.Icons
import kiwi.orbit.compose.ui.OrbitTheme
import kiwi.orbit.compose.ui.controls.BadgeSecondary
import kiwi.orbit.compose.ui.controls.Card
import kiwi.orbit.compose.ui.controls.Icon
import kiwi.orbit.compose.ui.controls.Text
import kiwi.orbit.compose.ui.foundation.ContentEmphasis
import kiwi.orbit.compose.ui.foundation.LocalContentEmphasis
import kiwi.orbit.compose.ui.foundation.ProvideMergedTextStyle

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun XItineraryScreen(onUpClick: () -> Unit) {
    SubScreen(
        title = "Itinerary",
        onUpClick = onUpClick,
    ) {
        Column {
            Box(Modifier.padding(16.dp)) {
                XItineraryScreen2()
            }
        }
    }
}

@Preview
@Composable
fun XItineraryScreen2() {
    Column {
        Card(elevation = 2.dp) { ItinerarySegment() }
        Spacer(Modifier.size(16.dp))
        val warning = OrbitTheme.colors.warning.main
        Card(elevation = 2.dp) {
            Box(
                Modifier.drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawLine(
                            color = warning,
                            start = Offset.Zero,
                            end = Offset(0f, size.height),
                            strokeWidth = 4.dp.toPx(),
                        )
                    }
                }
            ) {
                Column {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(OrbitTheme.colors.warning.subtle)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.AlertCircle, contentDescription = null, tint = OrbitTheme.colors.warning.main)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            "Affected connection",
                            style = OrbitTheme.typography.bodyNormal,
                            color = OrbitTheme.colors.warning.strong
                        )
                    }

                    ItinerarySegment()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ItinerarySegment() {
    val lineColor = Color(0xFFDCE3E9)
    Column(
        Modifier
            .padding(vertical = 4.dp)
    ) {
        Box(
            Modifier.drawBehind {
                val baseX = MainAxisOffset.toPx()
                drawLine(
                    color = lineColor,
                    start = Offset(baseX, size.height / 2),
                    end = Offset(baseX, size.height)
                )
                drawCircle(
                    color = lineColor,
                    radius = 4.dp.toPx(),
                    center = Offset(baseX, size.height / 2),
                )
            }
        ) {
            ItineraryPlace()
        }
        var visibleDetail by remember { mutableStateOf(false) }
        Box(
            Modifier.drawBehind {
                val baseX = MainAxisOffset.toPx()
                drawLine(
                    color = lineColor,
                    start = Offset(baseX, 0f),
                    end = Offset(baseX, size.height)
                )
            }
        ) {
            ItineraryDetail(
                expanded = visibleDetail,
                onExpandToggle = { visibleDetail = !visibleDetail },
            )
        }
        val bgColor = OrbitTheme.colors.surface.background
        AnimatedVisibility(visible = visibleDetail) {
            Box(
                Modifier.drawBehind {
                    drawRect(bgColor)
                    val baseX = MainAxisOffset.toPx()
                    drawLine(
                        color = lineColor,
                        start = Offset(baseX, 0f),
                        end = Offset(baseX, size.height),
                    )
                }
            ) {
                ItineraryDetails()
            }
        }
        Box(
            Modifier.drawBehind {
                val baseX = MainAxisOffset.toPx()
                drawLine(
                    color = lineColor,
                    start = Offset(baseX, 0f),
                    end = Offset(baseX, size.height / 2)
                )
                drawCircle(
                    color = lineColor,
                    radius = 4.dp.toPx(),
                    center = Offset(baseX, size.height / 2),

                    )
            }
        ) {
            ItineraryPlace()
        }
    }
}

@Composable
private fun ItineraryPlace() {
    Row(
        Modifier
            .padding(vertical = 8.dp)
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            Modifier
                .requiredWidth(width = MainAxisOffset - 4.dp)
                .padding(end = 8.dp, start = 4.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                "18:25",
                style = OrbitTheme.typography.bodyNormal,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
            )
            CompositionLocalProvider(
                LocalContentEmphasis provides ContentEmphasis.Minor
            ) {
                Text(
                    "Mon, 30. 1.",
                    style = OrbitTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                )
            }
        }
        Spacer(
            Modifier
                .size(8.dp)
        )
        Column(
            Modifier.padding(start = 8.dp)
        ) {
            Text(
                "Prague · PRG",
                style = OrbitTheme.typography.bodyNormal,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
            )
            CompositionLocalProvider(
                LocalContentEmphasis provides ContentEmphasis.Minor
            ) {
                Text(
                    "Václav Havel Airport Prague",
                    style = OrbitTheme.typography.bodySmall,
                    modifier = Modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ItineraryDetail(
    expanded: Boolean,
    onExpandToggle: () -> Unit,
) {

    Row(
        Modifier
            .clickable { onExpandToggle() }
            .padding(vertical = 8.dp)
            .padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "1h 20m",
            style = OrbitTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier
                .requiredWidth(width = MainAxisOffset - 8.dp)
                .padding(end = 8.dp, start = 4.dp),
        )

        Icon(
            painter = Icons.AirplaneDown,
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
        )

        LazyRow(
            Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        ) {
            val badges = listOf("Ryanair", "Economy", "Wheelchair", "Ryanair", "Economy", "Wheelchair")
            itemsIndexed(badges) { i, item ->
                BadgeSecondary(Modifier.padding(start = if (i == 0) 0.dp else 4.dp)) {
                    Text(text = item)
                }
            }
        }

        val rotation = animateFloatAsState(
            targetValue = if (expanded) 0f else 180f,
            animationSpec = tween(),
        )
        Icon(
            painter = Icons.ChevronUp,
            contentDescription = null,
            modifier = Modifier.graphicsLayer {
                rotationX = rotation.value
            }
        )
    }
}

@Composable
fun ItineraryDetails() {
    ProvideMergedTextStyle(value = OrbitTheme.typography.bodySmall) {
        Column(
            Modifier
                .padding(start = MainAxisOffset - 12.dp, end = 16.dp)
        ) {
            ItineraryDetail(
                "Connection info",
                listOf(
                    Triple(Icons.Airplane, "Carrier", "Ryanair"),
                    Triple(Icons.InformationCircle, "Connection number", "RA 83459"),
                )
            )
            ItineraryDetail(
                "Seating info",
                listOf(
                    Triple(Icons.Seat, "Seat pitch", "76 cm"),
                    Triple(Icons.Seat, "Seat width", "43 cm"),
                    Triple(Icons.Seat, "Seat recline", "7 cm"),
                    Triple(Icons.Entertainment, "Audio & video on demand", "No"),
                    Triple(Icons.PowerPlug, "In-seat power", "No"),
                    Triple(Icons.Wifi, "Wi-Fi on board", "Yes"),
                )
            )
            ItineraryDetail(
                "Some other pretty long multi-line info header with many words",
                listOf(
                    Triple(
                        Icons.InformationCircle,
                        "Some very very very long multi-line label with extra many words",
                        "Value"
                    ),
                    Triple(Icons.CheckCircle, "A normal label", null),
                )
            )
        }
    }
}

@Composable
fun ItineraryDetail(
    title: String,
    data: List<Triple<Painter, String, String?>>,
) {
    Text(
        title,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(
            top = 8.dp,
            bottom = 4.dp,
            start = 32.dp,
        )
    )

    val colorBackground = OrbitTheme.colors.surface.main
    Column(
        Modifier
            .padding(bottom = 8.dp)
            .drawBehind {
                val width = (16.dp + 8.dp).toPx()
                val height = size.height
                val cornerRadius = CornerRadius(width / 2, width / 2)
                drawRoundRect(
                    color = colorBackground,
                    size = Size(width, height),
                    cornerRadius = cornerRadius,
                )
            }
    ) {
        for (row in data) {
            Row(Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                Icon(
                    painter = row.first,
                    contentDescription = null,
                    Modifier
                        .padding(start = 4.dp, end = 12.dp)
                        .size(16.dp)
                )

                Text(row.second, Modifier.weight(1f))

                val description = row.third
                if (description != null) {
                    Text(
                        description,
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

private val MainAxisOffset = 85.dp
