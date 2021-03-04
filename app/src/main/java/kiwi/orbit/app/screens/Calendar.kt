@file:Suppress("UNUSED_PARAMETER")

package kiwi.orbit.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt

val positions: MutableMap<String, Rect> = mutableMapOf()
val dragOffsetChannel = Channel<Offset>()

@Composable
@Preview
fun CalendarScreen() {

    var selectedDays by remember { mutableStateOf(Pair(LocalDate.parse("2020-10-18"), LocalDate.parse("2020-11-03"))) }

    val onDateSelect = { date: LocalDate ->
        if (selectedDays.first == selectedDays.second) {
            selectedDays = selectedDays.copy(second = date)
        } else {
            selectedDays = Pair(date, date)
        }
    }

    val onDragSelect = { date: LocalDate ->
        selectedDays = selectedDays.copy(second = date)
    }

    var startPos: Offset? = remember { null }

    rememberCoroutineScope().launch(Dispatchers.Default) {
        @Suppress("EXPERIMENTAL_API_USAGE")
        dragOffsetChannel
            .receiveAsFlow()
            //.debounce(100)
            .collectLatest { dragOffset ->
                for ((t, u) in positions) {
                    if (u.contains(dragOffset)) {
                        onDragSelect(LocalDate.parse(t))
                        break
                    }
                }
            }
    }

    positions.clear()

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Column {
        Box(Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .onGloballyPositioned {
                if (startPos == null) {
                    startPos = it.positionInWindow()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    dragOffsetChannel.offer(Offset(offsetX, offsetY))
                }
            }
            .width(48.dp)
            .height(48.dp)
            .background(Color.Red)
        )

        Month(LocalDate.parse("2020-10-01"), selectedDays, onDateSelect, onDragSelect)
        Month(LocalDate.parse("2020-11-01"), selectedDays, onDateSelect, onDragSelect)
        Month(LocalDate.parse("2020-12-01"), selectedDays, onDateSelect, onDragSelect)
        Month(LocalDate.parse("2021-01-01"), selectedDays, onDateSelect, onDragSelect)
        Month(LocalDate.parse("2021-02-01"), selectedDays, onDateSelect, onDragSelect)
    }

}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun Month(
    firstDay: LocalDate,
    selected: Pair<LocalDate, LocalDate>?,
    onDateSelect: (LocalDate) -> Unit,
    onDragSelect: (LocalDate) -> Unit,
    weekStartsOnMonday: Boolean = true
) {

    val weeksDays: List<List<LocalDate>> = remember(firstDay) {
        val days = buildList {
            repeat(firstDay.month.length(false)) { i ->
                add(firstDay.plus(i, DateTimeUnit.DAY))
            }
        }

        val sunMonDiff = if (weekStartsOnMonday) -1 else 0
        val missingDays = days.first().dayOfWeek.value - 1
        days
            .groupBy {
                (it.dayOfMonth + missingDays + sunMonDiff).div(7)
            }
            .toSortedMap()
            .values
            .toList()
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {

        Text(
            text = firstDay.month.getDisplayName(
                TextStyle.FULL_STANDALONE,
                Locale.getDefault()
            ) + " " + firstDay.year.toString()
        )

        weeksDays.forEach { weekDays ->
            Week(weekDays, selected, onDateSelect, onDragSelect)
        }

    }
}

@Composable
private fun Week(
    days: List<LocalDate>,
    selected: Pair<LocalDate, LocalDate>?,
    onDateSelect: (LocalDate) -> Unit,
    onDragSelect: (LocalDate) -> Unit,
) {
    ConstraintLayout(Modifier.fillMaxWidth()) {
        val refs = List(days.size) { createRef() }
        val isFirstWeek = days.size < 7 && days.first().dayOfMonth < 7

        if (selected != null) {
            var firstDaySelected =
                days.indexOfFirst { it >= selected.first && it <= selected.second }
            var lastDaySelected = days.indexOfLast { it >= selected.first && it <= selected.second }
            if (firstDaySelected != -1 && lastDaySelected == -1) {
                lastDaySelected = days.lastIndex
            }
            if (firstDaySelected == -1 && lastDaySelected != -1) {
                firstDaySelected = 0
            }
            if (firstDaySelected != -1 && lastDaySelected != -1) {
                Box(
                    Modifier
                        .zIndex(0f)
                        .constrainAs(createRef()) {
                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                            centerVerticallyTo(parent)
                            start.linkTo(refs[firstDaySelected].start)
                            end.linkTo(refs[lastDaySelected].end)
                        }
                        .background(Color.LightGray)
                        .border(1.dp, Color.Blue)
                )
            }
        }

        days.forEachIndexed { index, day ->
            Day(
                day,
                onDateSelect,
                Modifier
                    .zIndex(1f)
                    .constrainAs(refs[index]) {
                        width = Dimension.percent(1f / 7)
                        height = Dimension.wrapContent
                        if (isFirstWeek) {
                            end.linkTo(refs.getOrNull(index + 1)?.start ?: parent.end)
                        } else {
                            start.linkTo(refs.getOrNull(index - 1)?.end ?: parent.start)
                        }
                    }
            )
        }
    }

}

@Composable
private fun Day(
    date: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .onGloballyPositioned { coords ->
                val pCoords = coords.parentLayoutCoordinates?.parentLayoutCoordinates?.parentLayoutCoordinates
                positions[date.toString()] = pCoords
                    ?.localBoundingBoxOf(coords)
                    ?.copy() ?: return@onGloballyPositioned
            }
//            .clickable(onClick = { onDateSelect(date) })
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Text(text = date.dayOfMonth.toString(), modifier = Modifier.align(Alignment.Center))
    }
}
