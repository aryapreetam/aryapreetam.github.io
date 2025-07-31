package dev.aryapreetam.components.widgets.code

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLinkElement
import kotlinx.browser.document
import kotlinx.browser.window

// JavaScript interop for highlight.js
external object hljs {
  fun highlightElement(element: HTMLElement)
  fun configure(options: dynamic)
}

val CodeBlockStyle = CssStyle {
  base {
    Modifier
      .maxWidth(90.vw)
      .fillMaxWidth()
      .backgroundColor(colorMode.toPalette().background)
      .borderRadius(8.px)
      .border(1.px, LineStyle.Solid, rgba(128, 128, 128, 0.2))
      .overflow(Overflow.Auto)
      .margin(16.px, 0.px)
  }
  Breakpoint.MD {
    Modifier.maxWidth(100.percent)
  }
}

@Composable
fun CodeBlock(text: String, modifier: Modifier = Modifier, lang: String? = null) {
  val codeRef = remember { mutableStateOf<HTMLElement?>(null) }
  var colorMode by ColorMode.currentState

  // Apply syntax highlighting when the component mounts or updates
  LaunchedEffect(text, lang, colorMode) {
    codeRef.value?.let { element ->
      try {
        // Ensure highlight.js is loaded before attempting to highlight
        if (js("typeof hljs !== 'undefined'") as Boolean) {
          // Configure highlight.js if needed
          hljs.configure(js("{ ignoreUnescapedHTML: true }"))

          // Apply highlighting
          hljs.highlightElement(element)
        } else {
          // Fallback: try to load highlight.js dynamically after a delay
          window.setTimeout({
            if (js("typeof hljs !== 'undefined'") as Boolean) {
              hljs.configure(js("{ ignoreUnescapedHTML: true }"))
              hljs.highlightElement(element)
            }
          }, 100)
        }
      } catch (e: Exception) {
        console.log("Highlight.js error:", e)
      }
    }
  }

  Pre(
    attrs = CodeBlockStyle.toModifier().then(modifier).toAttrs {
      style {
        padding(16.px)
        fontSize(14.px)
        lineHeight("1.5")
      }
    }
  ) {
    Code(attrs = {
      classes(lang?.let { "language-$it" } ?: "nohighlight")
      style {
        fontFamily("'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace")
        fontSize(14.px)
        lineHeight("1.5")
        whiteSpace("pre")
        display(DisplayStyle.Block)
        width(100.percent)
      }
      ref { element ->
        codeRef.value = element
        onDispose { codeRef.value = null }
      }
    }) {
      Text(text)
    }
  }
}