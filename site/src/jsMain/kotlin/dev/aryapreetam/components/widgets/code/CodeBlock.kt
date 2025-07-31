package dev.aryapreetam.components.widgets.code

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.fa.FaCopy
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement

// JavaScript interop for highlight.js
external object hljs {
  fun highlightElement(element: HTMLElement)
  fun configure(options: dynamic)
  fun registerLanguage(name: String, definition: dynamic)
}

@Composable
fun CodeBlock(
  text: String,
  modifier: Modifier = Modifier,
  lang: String? = null,
  filename: String? = null
) {
  val codeRef = remember { mutableStateOf<HTMLElement?>(null) }
  var colorMode by ColorMode.currentState
  var isHovered by remember { mutableStateOf(false) }

  // Load JetBrains Mono font and highlight.js themes
  LaunchedEffect(Unit) {
    // Load JetBrains Mono font with higher priority
    if (document.querySelector("link[href*='JetBrains+Mono']") == null) {
      val fontLink = document.createElement("link").apply {
        setAttribute("rel", "preload")
        setAttribute("as", "font")
        setAttribute("type", "font/woff2")
        setAttribute("href", "https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500;600&display=swap")
        setAttribute("crossorigin", "")
      }
      document.head?.appendChild(fontLink)

      // Also add as stylesheet
      val styleLink = document.createElement("link").apply {
        setAttribute("rel", "stylesheet")
        setAttribute("href", "https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500;600&display=swap")
      }
      document.head?.appendChild(styleLink)
    }

    // Load highlight.js theme based on color mode
    val themeId = "highlight-theme"
    document.getElementById(themeId)?.remove()

    val themeLink = document.createElement("link").apply {
      setAttribute("id", themeId)
      setAttribute("rel", "stylesheet")
      setAttribute(
        "href",
        if (colorMode.isLight) "/highlight.js/styles/a11y-light.min.css" else "/highlight.js/styles/a11y-dark.min.css"
      )
    }
    document.head?.appendChild(themeLink)

    // Add custom CSS to force JetBrains Mono
    val customStyleId = "jetbrains-mono-force"
    if (document.getElementById(customStyleId) == null) {
      val style = document.createElement("style").apply {
        setAttribute("id", customStyleId)
        textContent = """
          pre code, code, .hljs {
            font-family: 'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace !important;
            font-feature-settings: 'liga' 0;
          }
        """.trimIndent()
      }
      document.head?.appendChild(style)
    }
  }

  // Apply syntax highlighting when the component mounts or updates
  LaunchedEffect(text, lang, colorMode) {
    window.setTimeout({
      codeRef.value?.let { element ->
        try {
          if (js("typeof hljs !== 'undefined'") as Boolean) {
            // Configure highlight.js with better language detection
            hljs.configure(js("{ ignoreUnescapedHTML: true, languages: ['kotlin', 'json', 'python', 'javascript', 'bash', 'yaml', 'xml', 'gradle'] }"))
            hljs.highlightElement(element)
          }
        } catch (e: Exception) {
          console.log("Highlight.js error:", e)
        }
      }
    }, 100)
  }

  fun copyToClipboard() {
    try {
      js("navigator.clipboard.writeText(text)")
      console.log("Code copied to clipboard")
    } catch (e: Exception) {
      console.log("Failed to copy:", e)
    }
  }

  // Container with relative positioning for copy button
  Div(
    attrs = {
      style {
        property("position", "relative")
        margin(16.px, 0.px)
        borderRadius(8.px)
        border(1.px, LineStyle.Solid, if (colorMode.isLight) rgba(229, 231, 235, 1) else rgba(75, 85, 99, 1))
        backgroundColor(if (colorMode.isLight) rgba(249, 250, 251, 1) else rgba(17, 24, 39, 1))
        property("overflow", "hidden")
      }
      onMouseEnter { isHovered = true }
      onMouseLeave { isHovered = false }
    }
  ) {
    // Header with filename and copy button (always show copy button)
    Div(
      attrs = {
        style {
          backgroundColor(if (colorMode.isLight) rgba(243, 244, 246, 1) else rgba(31, 41, 55, 1))
          property("border-bottom", "1px solid ${if (colorMode.isLight) "rgb(229, 231, 235)" else "rgb(75, 85, 99)"}")
          padding(8.px, 16.px)
          fontSize(12.px)
          fontFamily("'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace")
          color(if (colorMode.isLight) rgba(107, 114, 128, 1) else rgba(156, 163, 175, 1))
          display(DisplayStyle.Flex)
          justifyContent(JustifyContent.SpaceBetween)
          alignItems(AlignItems.Center)
        }
      }
    ) {
      // Show filename if available, otherwise show language
      Span {
        Text(filename ?: lang?.let { it.uppercase() } ?: "CODE")
      }

      // Copy button (always visible on hover)
      Button(
        attrs = {
          style {
            backgroundColor(Color.transparent)
            border(0.px, LineStyle.None, Color.transparent)
            color(if (colorMode.isLight) rgba(107, 114, 128, 1) else rgba(156, 163, 175, 1))
            property("cursor", "pointer")
            padding(4.px, 8.px)
            borderRadius(4.px)
            fontSize(11.px)
            fontFamily("'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace")
            opacity(if (isHovered) 1 else 0)
            property("transition", "opacity 0.2s")
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            property("gap", "4px")
          }
          onClick { copyToClipboard() }
        }
      ) {
        I(attrs = { classes("fa", "fa-copy") })
        Text("Copy")
      }
    }

    // Code content
    Pre(
      attrs = {
        style {
          margin(0.px)
          padding(16.px)
          backgroundColor(Color.transparent)
          property("overflow", "auto")
          fontSize(14.px)
          property("line-height", "1.5")
          fontFamily("'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace !important")
        }
      }
    ) {
      Code(attrs = {
        // Map language aliases
        val hlLang = when (lang?.lowercase()) {
          "kt", "kts", "kotlin" -> "kotlin"
          "gradle", "gradle.kts", "build.gradle.kts" -> "kotlin"
          "js" -> "javascript"
          "py" -> "python"
          "yml" -> "yaml"
          else -> lang
        }
        classes(hlLang?.let { "language-$it" } ?: "nohighlight")

        style {
          fontFamily("'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace !important")
          fontSize(14.px)
          property("line-height", "1.5")
          whiteSpace("pre")
          display(DisplayStyle.Block)
          backgroundColor(Color.transparent)
          color(Color("inherit"))
          fontWeight(400)
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
}