package dev.aryapreetam.components.widgets.code

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import com.varabyte.kobweb.silk.theme.colors.shifted
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

val InlineCodeStyle = CssStyle.base {
  Modifier
    .color(colorMode.toPalette().color.shifted(colorMode, byPercent = -0.1f))
    .backgroundColor(colorMode.toPalette().background.shifted(colorMode, byPercent = 0.05f))
    .overflowWrap(OverflowWrap.BreakWord)
    .fontFamily("'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace")
    .fontSize(0.9.em)
    .padding(2.px, 6.px)
    .borderRadius(4.px)
    .border(1.px, LineStyle.Solid, rgba(128, 128, 128, 0.15))
    .lineHeight(1.4)
}

@Composable
fun InlineCode(text: String, modifier: Modifier = Modifier) {
  Code(attrs = InlineCodeStyle.toModifier().then(modifier).toAttrs()) {
    Text(text)
  }
}