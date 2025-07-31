package dev.aryapreetam.components.widgets.code

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import com.varabyte.kobweb.silk.theme.colors.shifted
import kotlinx.browser.document
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

val InlineCodeStyle = CssStyle.base {
  Modifier
    .color(if (colorMode.isLight) rgba(79, 70, 229, 1) else rgba(167, 139, 250, 1))
    .backgroundColor(if (colorMode.isLight) rgba(241, 245, 249, 1) else rgba(51, 65, 85, 1))
    .overflowWrap(OverflowWrap.BreakWord)
    .fontFamily("'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace")
    .fontSize(0.875.em)
    .padding(2.px, 6.px)
    .borderRadius(4.px)
    .border(1.px, LineStyle.Solid, if (colorMode.isLight) rgba(0, 0, 0, 0.05) else rgba(255, 255, 255, 0.1))
    .lineHeight(1.4)
    .fontWeight(500)
}

@Composable
fun InlineCode(text: String, modifier: Modifier = Modifier) {
  var colorMode by ColorMode.currentState

  // Ensure JetBrains Mono font is loaded
  LaunchedEffect(Unit) {
    if (document.querySelector("link[href*='jetbrains-mono']") == null) {
      val linkElement = document.createElement("link").apply {
        setAttribute("rel", "stylesheet")
        setAttribute("href", "https://cdn.jsdelivr.net/npm/@xz/fonts@1/serve/jetbrains-mono.min.css")
      }
      document.head?.appendChild(linkElement)
    }
  }

  Code(attrs = InlineCodeStyle.toModifier().then(modifier).toAttrs {
    style {
      // Ensure proper font family
      fontFamily("'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace")
    }
  }) {
    Text(text)
  }
}