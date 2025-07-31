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

// JavaScript interop for Prism.js
external object Prism {
  fun highlightElement(element: HTMLElement)
  fun highlightAll()
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
  var copyButtonText by remember { mutableStateOf("Copy") }
  var isButtonHovered by remember { mutableStateOf(false) }

  // Load JetBrains Mono font
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

    // Add custom CSS for fonts
    val customStyleId = "code-block-fonts"
    if (document.getElementById(customStyleId) == null) {
      val style = document.createElement("style").apply {
        setAttribute("id", customStyleId)
        textContent = """
          /* Code content - JetBrains Mono */
          pre code, code, .token {
            font-family: 'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace !important;
            font-feature-settings: 'liga' 0;
          }
          
          /* UI elements - System font */
          .code-block-ui {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif !important;
          }
        """.trimIndent()
      }
      document.head?.appendChild(style)
    }
  }

  // Load Prism.js theme based on color mode
  LaunchedEffect(colorMode) {
    // Remove existing Prism theme
    val existingTheme = document.getElementById("prism-theme")
    existingTheme?.remove()

    // Load appropriate theme based on color mode
    val themeLink = document.createElement("link").apply {
      setAttribute("id", "prism-theme")
      setAttribute("rel", "stylesheet")
      setAttribute(
        "href",
        if (colorMode.isLight) {
          "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism.min.css"
        } else {
          "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism-okaidia.min.css"
        }
      )
    }
    document.head?.appendChild(themeLink)
  }

  // Apply syntax highlighting when the component mounts or updates
  LaunchedEffect(text, lang) {
    window.setTimeout({
      codeRef.value?.let { element ->
        try {
          if (js("typeof Prism !== 'undefined'") as Boolean) {
            Prism.highlightElement(element)
          }
        } catch (e: Exception) {
          console.log("Prism.js error:", e)
        }
      }
    }, 100)
  }

  fun copyToClipboard() {
    try {
      js("navigator.clipboard.writeText(text)")
      copyButtonText = "Copied!"
      // Reset after 2 seconds
      window.setTimeout({
        copyButtonText = "Copy"
      }, 2000)
    } catch (e: Exception) {
      console.log("Failed to copy:", e)
    }
  }

  // Map language aliases for Prism.js
  val prismLang = when (lang?.lowercase()) {
    "kt", "kts", "kotlin" -> "kotlin"
    "gradle", "gradle.kts", "build.gradle.kts" -> "kotlin"
    "js" -> "javascript"
    "py" -> "python"
    "yml" -> "yaml"
    "sh" -> "bash"
    else -> lang
  }

  // Container wrapper to handle filename positioning
  Div(
    attrs = {
      style {
        margin(16.px, 0.px)
        property("position", "relative")
        property("overflow", "visible") // Ensure filename box isn't clipped
      }
      onMouseEnter { isHovered = true }
      onMouseLeave { isHovered = false }
    }
  ) {
    // Filename tab (positioned outside main container to avoid clipping)
    filename?.let {
      Div(
        attrs = {
          classes("code-block-ui")
          style {
            property("position", "absolute")
            property("top", "-15px") // Position relative to wrapper, not main container
            property("left", "12px") // Start after rounded corner
            property("z-index", "15") // Higher z-index to ensure visibility above all elements
            backgroundColor(if (colorMode.isLight) rgba(243, 244, 246, 1) else rgba(31, 41, 55, 1))
            color(if (colorMode.isLight) rgba(75, 85, 99, 1) else rgba(209, 213, 219, 1))
            padding(4.px, 12.px)
            fontSize(11.px)
            fontWeight(500)
            // Add border similar to main container
            border(1.px, LineStyle.Solid, if (colorMode.isLight) rgba(229, 231, 235, 1) else rgba(75, 85, 99, 1))
            borderRadius(4.px) // Small radius for clean look
          }
        }
      ) {
        Text(it)
      }
    }

    // Main code container
    Div(
      attrs = {
        style {
          property("position", "relative")
          borderRadius(8.px)
          border(1.px, LineStyle.Solid, if (colorMode.isLight) rgba(229, 231, 235, 1) else rgba(75, 85, 99, 1))
          backgroundColor(if (colorMode.isLight) rgba(249, 250, 251, 1) else rgba(17, 24, 39, 1))
          property("overflow", "hidden")
          // Add top margin when filename exists to accommodate the filename box
          if (filename != null) {
            marginTop(8.px)
          }
        }
      }
    ) {
      // Copy button (positioned in top-right corner)
      Button(
        attrs = {
          classes("code-block-ui")
          style {
            property("position", "absolute")
            property("top", "8px")
            property("right", "12px")
            property("z-index", "10")
            backgroundColor(
              if (isButtonHovered) {
                if (colorMode.isLight) rgba(107, 114, 128, 0.1) else rgba(156, 163, 175, 0.1)
              } else Color.transparent
            )
            border(0.px, LineStyle.None, Color.transparent)
            color(if (colorMode.isLight) rgba(107, 114, 128, 1) else rgba(156, 163, 175, 1))
            property("cursor", "pointer")
            padding(4.px, 8.px)
            borderRadius(4.px)
            fontSize(11.px)
            opacity(if (isHovered) 1 else 0)
            property("transition", "opacity 0.2s, background-color 0.2s")
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            property("gap", "4px")
          }
          onClick { copyToClipboard() }
          onMouseEnter { isButtonHovered = true }
          onMouseLeave { isButtonHovered = false }
        }
      ) {
        I(attrs = { classes("fa", "fa-copy") })
        Text(copyButtonText)
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
        Code(
          attrs = {
            classes(prismLang?.let { "language-$it" } ?: "nohighlight")
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
          }
        ) {
          Text(text)
        }
      }
    }
  }
}