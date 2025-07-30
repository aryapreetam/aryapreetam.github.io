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
    if (document.querySelector("link[href*='jetbrains-mono']") == null) {
      val linkElement = document.createElement("link").apply {
        setAttribute("rel", "stylesheet")
        setAttribute("href", "https://cdn.jsdelivr.net/npm/@xz/fonts@1/serve/jetbrains-mono.min.css")
      }
      document.head?.appendChild(linkElement)
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
                maxWidth(800.px)
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
                padding(20.px)
                fontSize(14.px)
                color(if (colorMode.isLight) Color("#718096") else Color("#a0aec0"))
                flexShrink(0)
              }
            }
        ) {
            Text("Built with Kobweb")
        }
    }
}