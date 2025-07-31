package dev.aryapreetam.components.layouts

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.icons.fa.FaMoon
import com.varabyte.kobweb.silk.components.icons.fa.FaSun
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

/**
 * Base page layout for the site - minimal and clean like kobweb-ghp-demo
 */
@Layout
@Composable
fun PageLayout(content: @Composable () -> Unit) {
  var colorMode by ColorMode.currentState

  // Load JetBrains Mono font if not already loaded
  LaunchedEffect(Unit) {
    // Load JetBrains Mono font with preload for performance
    if (document.querySelector("link[href*='JetBrains+Mono']") == null) {
      val preloadLink = document.createElement("link").apply {
        setAttribute("rel", "preload")
        setAttribute("as", "font")
        setAttribute("type", "font/woff2")
        setAttribute("href", "https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500;600&display=swap")
        setAttribute("crossorigin", "")
      }
      document.head?.appendChild(preloadLink)

      val linkElement = document.createElement("link").apply {
        setAttribute("rel", "stylesheet")
        setAttribute("href", "https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500;600&display=swap")
      }
      document.head?.appendChild(linkElement)
    }

    // Also load from CDN as backup
    if (document.querySelector("link[href*='jetbrains-mono']") == null) {
      val backupLink = document.createElement("link").apply {
        setAttribute("rel", "stylesheet")
        setAttribute("href", "https://cdn.jsdelivr.net/npm/@xz/fonts@1/serve/jetbrains-mono.min.css")
      }
      document.head?.appendChild(backupLink)
    }

    // Add global CSS to ensure JetBrains Mono is applied to code elements
    val globalCodeStyleId = "global-jetbrains-mono"
    if (document.getElementById(globalCodeStyleId) == null) {
      val style = document.createElement("style").apply {
        setAttribute("id", globalCodeStyleId)
        textContent = """
          @import url('https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500;600&display=swap');
          
          code, pre code, .hljs, 
          code[class*="language-"], 
          pre[class*="language-"] code {
            font-family: 'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace !important;
            font-feature-settings: 'liga' 0 !important;
            font-variant-ligatures: none !important;
          }
        """.trimIndent()
      }
      document.head?.appendChild(style)
    }
  }

  // Load theme from localStorage on first render
  LaunchedEffect(Unit) {
    try {
      val savedTheme = localStorage.getItem("kobweb-color-mode")
      console.log("Loaded theme from localStorage: $savedTheme")
      when (savedTheme) {
        "dark" -> if (colorMode.isLight) {
          console.log("Setting dark mode")
          colorMode = ColorMode.DARK
        }

        "light" -> if (colorMode.isDark) {
          console.log("Setting light mode")
          colorMode = ColorMode.LIGHT
        }

        null -> {
          console.log("No saved theme, using current: ${if (colorMode.isLight) "light" else "dark"}")
          // Set default theme to localStorage
          localStorage.setItem("kobweb-color-mode", if (colorMode.isLight) "light" else "dark")
        }
      }
    } catch (e: Exception) {
      console.error("Error loading theme from localStorage", e)
    }
  }

  // Full height flexbox container
  Div(
        attrs = {
          style {
            minHeight(100.vh)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            backgroundColor(if (colorMode.isLight) Color.white else Color("#1a202c"))
            color(if (colorMode.isLight) Color("#2d3748") else Color("#e2e8f0"))
            fontFamily(
              "system-ui",
              "-apple-system",
              "BlinkMacSystemFont",
              "Segoe UI",
              "Helvetica",
              "Arial",
              "sans-serif"
            )
            lineHeight("1.5")
          }
        }
    ) {
      // Theme toggle positioned absolutely in top right
      Button(
        onClick = {
          val newMode = colorMode.opposite
          colorMode = newMode
          // Save theme to localStorage
          localStorage.setItem("kobweb-color-mode", if (newMode.isDark) "dark" else "light")
            },
            modifier = Modifier
              .position(Position.Fixed)
              .top(16.px)
              .right(16.px)
              .borderRadius(50.percent)
              .padding(8.px)
              .backgroundColor(if (colorMode.isLight) Color("#f7fafc") else Color("#4a5568"))
              .border(0.px, LineStyle.None, Color.transparent)
              .zIndex(1000)
        ) {
          if (colorMode.isLight) FaMoon() else FaSun()
        }

      // Main content area - grows to fill available space
      Main(
            attrs = {
              style {
                flexGrow(1)
                maxWidth(1024.px)
                property("margin", "0 auto")
                padding(32.px, 20.px, 40.px, 20.px)
                width(100.percent)
              }
            }
        ) {
            content()
        }

      // Footer - always at bottom
      Footer(
            attrs = {
              style {
                property("text-align", "center")
                paddingTop(16.px)
                paddingLeft(16.px)
                paddingRight(16.px)
                paddingBottom(8.px)
                fontSize(14.px)
                color(if (colorMode.isLight) Color("#718096") else Color("#a0aec0"))
                flexShrink(0)
              }
            }
        ) {
        Text("Built with ")
        A(
          href = "https://kobweb.varabyte.com",
          attrs = {
            attr("target", "_blank")
            attr("rel", "noopener noreferrer")
            style {
              color(if (colorMode.isLight) Color("#3182ce") else Color("#63b3ed"))
              textDecoration("none")
            }
          }
        ) {
          Text("Kobweb")
        }
        }
    }
}