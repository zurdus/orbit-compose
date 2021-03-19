@file:Suppress("unused", "UnnecessaryVariable")

package kiwi.orbit.app.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import androidx.constraintlayout.compose.atMost
import kiwi.orbit.OrbitTheme
import kiwi.orbit.app.SubScreen
import kiwi.orbit.controls.BadgeSecondary
import kiwi.orbit.foundation.ColorTokens
import kiwi.orbit.icons.Icons

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun XItineraryScreen(onUpClick: () -> Unit) {
    SubScreen(
        title = "Itinerary",
        onUpClick = onUpClick,
        withBackground = false,
    ) {
        Column() {
            ConstraintLayout {
                var isVisible by remember { mutableStateOf(true) }
                val ref = createRef()
                val barrier = createBottomBarrier(ref)
                Button(
                    onClick = { isVisible = !isVisible },
                    Modifier
                        .height(if (isVisible) 30.dp else 40.dp)
                        .constrainAs(ref) {
                            top.linkTo(parent.top)
                        }
                ) { Text("Toggle") }
                Text(
                    "Test",
                    Modifier.constrainAs(createRef()) {
                        top.linkTo(barrier)
                    }
                )
            }

            Box(Modifier.padding(16.dp)) {
                XItineraryScreen2()
            }
        }
    }
}

@Preview
@Composable
fun XItineraryScreen2() {
    Itineraries()
}

@Composable
fun Itineraries() {
    ConstraintLayout(
        Modifier.fillMaxWidth()
    ) {
        val itinerariesCount = 2
        val refsCount = 5
        val timeRefs = List(itinerariesCount * refsCount) { createRef() }
        val timeRefsChunked = timeRefs.chunked(refsCount)
        val timeAnchor = createEndBarrier(*timeRefs.toTypedArray())

        var lastAnchor: ConstraintLayoutBaseScope.HorizontalAnchor = createGuidelineFromTop(0.dp)

        for (i in 0 until itinerariesCount) {
            lastAnchor = Itinerary(
                timeRefs = timeRefsChunked[i],
                timeAnchor = timeAnchor,
                topAnchor = lastAnchor,
                topMargin = if (i == 0) 0.dp else CardVerticalMargin
            )
        }
    }

}

@SuppressLint("ComposableNaming")
@Composable
private fun ConstraintLayoutScope.Itinerary(
    timeRefs: List<ConstrainedLayoutReference>,
    timeAnchor: ConstraintLayoutBaseScope.VerticalAnchor,
    topAnchor: ConstraintLayoutBaseScope.HorizontalAnchor,
    topMargin: Dp,
): ConstraintLayoutBaseScope.HorizontalAnchor {
    val bottomSpace = createRef()

    val (fromBottomAnchor, fromDotRef) = ItineraryPlace(
        timeRef = timeRefs[0],
        dateRef = timeRefs[1],
        timeAnchor = timeAnchor,
        topAnchor = topAnchor,
        topMargin = CardVerticalPadding + topMargin,
    )
    val detailBottomAnchor = ItineraryDetail(timeRefs[2], timeAnchor, fromBottomAnchor)
    val (toBottomAnchor, toDotRef) = ItineraryPlace(
        timeRef = timeRefs[3],
        dateRef = timeRefs[4],
        timeAnchor = timeAnchor,
        topAnchor = detailBottomAnchor,
        topMargin = 16.dp,
    )
    Spacer(
        Modifier
            .height(CardVerticalPadding)
            .constrainAs(bottomSpace) {
                top.linkTo(toBottomAnchor)
            }
    )

    Card(
        Modifier
            .zIndex(-2f)
            .constrainAs(createRef()) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                top.linkTo(topAnchor, margin = topMargin)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(bottomSpace.bottom)
            },
        content = {}
    )
    val lineColor = ColorTokens.CloudNormalActive
    Canvas(
        Modifier
            .zIndex(-1f)
            .constrainAs(createRef()) {
                width = Dimension.value(2.dp)
                height = Dimension.fillToConstraints
                centerHorizontallyTo(fromDotRef)
                top.linkTo(fromDotRef.top, margin = 4.dp)
                bottom.linkTo(toDotRef.bottom, margin = 4.dp)
            }
    ) {
        val width = 2.dp.toPx()
        drawLine(
            color = lineColor,
            start = Offset(width / 2, 0f),
            end = Offset(width / 2, size.height),
            strokeWidth = width,
        )
    }

    return bottomSpace.bottom
}

@SuppressLint("ComposableNaming")
@Composable
private fun ConstraintLayoutScope.ItineraryPlace(
    timeRef: ConstrainedLayoutReference,
    dateRef: ConstrainedLayoutReference,
    timeAnchor: ConstraintLayoutBaseScope.VerticalAnchor,
    topAnchor: ConstraintLayoutBaseScope.HorizontalAnchor,
    topMargin: Dp,
): Pair<ConstraintLayoutBaseScope.HorizontalAnchor, ConstrainedLayoutReference> {
    val (cityRef, airportRef, dotRef) = createRefs()
    Text(
        "18:25",
        style = OrbitTheme.typography.bodyNormal,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.End,
        modifier = Modifier
            .constrainAs(timeRef) {
                width = Dimension.preferredWrapContent.atMost(MaxTimeColumnWidth)
                top.linkTo(topAnchor, margin = topMargin)
                linkTo(
                    start = parent.start,
                    end = timeAnchor,
                    startMargin = CardHorizontalPadding,
                    bias = 1f,
                )
            }
    )
    Text(
        "Mon, 30. 1.",
        style = OrbitTheme.typography.bodySmall,
        color = OrbitTheme.colors.surfaceContentAlt,
        textAlign = TextAlign.End,
        modifier = Modifier
            .constrainAs(dateRef) {
                width = Dimension.preferredWrapContent.atMost(MaxTimeColumnWidth)
                top.linkTo(timeRef.bottom)
                linkTo(
                    start = parent.start,
                    end = timeAnchor,
                    startMargin = CardHorizontalPadding,
                    bias = 1f,
                )
            }
    )
    Text(
        "Prague · PRG",
        style = OrbitTheme.typography.bodyNormal,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .constrainAs(cityRef) {
                width = Dimension.fillToConstraints
                top.linkTo(topAnchor, margin = topMargin)
                start.linkTo(timeAnchor, 32.dp)
                end.linkTo(parent.end, CardHorizontalPadding)
            }
    )
    Text(
        "Václav Havel Airport Prague",
        style = OrbitTheme.typography.bodySmall,
        color = OrbitTheme.colors.surfaceContentAlt,
        modifier = Modifier
            .constrainAs(airportRef) {
                width = Dimension.fillToConstraints
                top.linkTo(cityRef.bottom)
                start.linkTo(cityRef.start)
                end.linkTo(cityRef.end)
            }
    )
    val color = ColorTokens.InkLight
    Canvas(
        Modifier
            .size(8.dp)
            .constrainAs(dotRef) {
                top.linkTo(timeRef.bottom)
                bottom.linkTo(dateRef.top)
                start.linkTo(timeRef.end)
                end.linkTo(cityRef.start)
            }
    ) {
        drawCircle(color)
    }

    return Pair(createBottomBarrier(dateRef, airportRef), dotRef)
}

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ConstraintLayoutScope.ItineraryDetail(
    timeRef: ConstrainedLayoutReference,
    timeAnchor: ConstraintLayoutBaseScope.VerticalAnchor,
    topAnchor: ConstraintLayoutBaseScope.HorizontalAnchor,
): ConstraintLayoutBaseScope.HorizontalAnchor {
    var visibleDetail by remember { mutableStateOf(true) }

    val (toggleRef, iconRef, badgesRef, detailsRef) = createRefs()

    Text(
        "1h 20m",
        style = OrbitTheme.typography.bodySmall,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.End,
        modifier = Modifier
            .constrainAs(timeRef) {
                width = Dimension.preferredWrapContent.atMost(MaxTimeColumnWidth)
                centerVerticallyTo(badgesRef)
                linkTo(
                    start = parent.start,
                    end = timeAnchor,
                    startMargin = CardHorizontalPadding,
                    bias = 1f,
                )
            }
    )

    IconButton(
        onClick = { visibleDetail = !visibleDetail },
        modifier = Modifier
            .constrainAs(toggleRef) {
                centerVerticallyTo(badgesRef)
                end.linkTo(parent.end, margin = CardHorizontalPadding / 2)
            }
    ) {
        val rotation = animateFloatAsState(
            targetValue = if (visibleDetail) 0f else 180f,
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

    Icon(
        painter = Icons.AirplaneDown,
        contentDescription = null,
        modifier = Modifier
            .size(16.dp)
            .constrainAs(iconRef) {
                centerVerticallyTo(badgesRef)
                start.linkTo(timeRef.end, margin = 8.dp)
            },
    )

    LazyRow(
        Modifier
            .constrainAs(badgesRef) {
                width = Dimension.fillToConstraints
                top.linkTo(topAnchor, margin = 16.dp)
                start.linkTo(iconRef.end, margin = 8.dp)
                end.linkTo(toggleRef.start, margin = 8.dp)
            },
    ) {
        val badges = listOf("Ryanair", "Economy", "Wheelchair", "Ryanair", "Economy", "Wheelchair")
        itemsIndexed(badges) { i, item ->
            BadgeSecondary(Modifier.padding(start = if (i == 0) 0.dp else 4.dp)) {
                Text(text = item)
            }
        }
    }

    Box(
        Modifier
            .constrainAs(detailsRef) {
                width = Dimension.fillToConstraints
                height = Dimension.preferredWrapContent.atLeast(1.dp)
                top.linkTo(badgesRef.bottom)
                start.linkTo(timeAnchor, margin = 4.dp)
                end.linkTo(parent.end, margin = CardHorizontalPadding)
            }
    ) {
        AnimatedVisibility(visible = visibleDetail) {
            ItineraryDetails()
        }
        Spacer(modifier = Modifier.size(40.dp))
    }

    return detailsRef.bottom
}

@Composable
fun ItineraryDetails() {
    ProvideTextStyle(value = OrbitTheme.typography.bodySmall) {
        Column {
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
            top = 16.dp,
            start = 28.dp,
        )
    )

    val colorBackground = OrbitTheme.colors.surface
    val colorBorder = ColorTokens.CloudNormalActive
    Column(
        Modifier
            .wrapContentHeight()
            .drawBehind {
                val width = (16.dp + 8.dp).toPx()
                val height = size.height
                val cornerRadius = CornerRadius(width / 2, width / 2)
                val strokeWidth = 1.dp.toPx()
                drawRoundRect(
                    color = colorBackground,
                    size = Size(width, height),
                    cornerRadius = cornerRadius,
                )
                drawRoundRect(
                    color = colorBorder,
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(width - strokeWidth, height - strokeWidth),
                    cornerRadius = cornerRadius,
                    style = Stroke(width = strokeWidth),
                )
            }
    ) {
        for (row in data) {
            Row(Modifier.padding(top = 8.dp)) {
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
        Spacer(Modifier.height(8.dp))
    }
}

private val MaxTimeColumnWidth = 76.dp
private val CardVerticalMargin = 16.dp
private val CardHorizontalPadding = 16.dp
private val CardVerticalPadding = 12.dp
