package dev.aryapreetam.components.layouts

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.zIndex
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

  // Full height flexbox container with semantic HTML
  Div(
        attrs = {
          attr("role", "document")
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
    // Skip to main content link for accessibility
    A(
      href = "#main-content",
      attrs = {
        attr("class", "skip-link")
        style {
          position(Position.Absolute)
          top((-40).px)
          left(8.px)
          zIndex(9999)
          padding(8.px, 12.px)
          backgroundColor(if (colorMode.isLight) Color("#2d3748") else Color("#f7fafc"))
          color(if (colorMode.isLight) Color.white else Color("#2d3748"))
          textDecoration("none")
          borderRadius(4.px)
          fontSize(14.px)
          fontWeight(600)
        }
        onFocus { event ->
          (event.target as? org.w3c.dom.HTMLElement)?.style?.top = "8px"
        }
        onBlur { event ->
          (event.target as? org.w3c.dom.HTMLElement)?.style?.top = "-40px"
        }
        }
      ) {
        Text("Skip to main content")
      }

    // Header with theme toggle
    Header(
      attrs = {
        style {
          display(DisplayStyle.Flex)
          justifyContent(JustifyContent.FlexEnd)
          padding(16.px)
          position(Position.Relative)
          zIndex(100)
        }
      }
    ) {
      Button(
        onClick = {
          val newMode = colorMode.opposite
          colorMode = newMode
          // Save theme to localStorage
          localStorage.setItem("kobweb-color-mode", if (newMode.isDark) "dark" else "light")
        },
        modifier = Modifier
          .borderRadius(50.percent)
          .padding(8.px)
          .backgroundColor(if (colorMode.isLight) Color("#f7fafc") else Color("#4a5568"))
          .border(0.px, LineStyle.None, Color.transparent)
      ) {
        if (colorMode.isLight) FaMoon() else FaSun()
        // Add visually hidden text for screen readers
        Span(
          attrs = {
            style {
              position(Position.Absolute)
              width(1.px)
              height(1.px)
              padding(0.px)
              property("margin", "-1px")
              property("overflow", "hidden")
              property("clip", "rect(0, 0, 0, 0)")
              property("white-space", "nowrap")
              property("border", "0")
            }
          }
        ) {
          Text(if (colorMode.isLight) "Switch to dark mode" else "Switch to light mode")
        }
      }
    }

    // Main content area - grows to fill available space with semantic HTML
    Main(
      attrs = {
        id("main-content")
        attr("role", "main")
        style {
          flexGrow(1)
          maxWidth(1024.px)
          property("margin", "0 auto")
          padding(0.px, 20.px, 40.px, 20.px)
          width(100.percent)
        }
      }
    ) {
      content()
    }

    // Footer - always at bottom with semantic HTML
      Footer(
        attrs = {
          attr("role", "contentinfo")
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
            attr("aria-label", "Learn more about Kobweb framework")
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