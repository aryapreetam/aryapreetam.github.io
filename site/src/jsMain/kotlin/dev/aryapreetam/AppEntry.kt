package dev.aryapreetam

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.*
import org.w3c.dom.HTMLLinkElement
import kotlinx.browser.document

@InitSilk
fun initStyles(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyleBase("html, body") { Modifier.fillMaxHeight() }

    // Load JetBrains Mono font and other fonts
    loadJetBrainsMonoFont()
}

// Load JetBrains Mono font
private fun loadJetBrainsMonoFont() {
    // Check if the font is already loaded
    val existingFont = document.querySelector("link[href*='JetBrains+Mono']")
    if (existingFont == null) {
        // Preconnect to Google Fonts
        val preconnect1 = document.createElement("link") as HTMLLinkElement
        preconnect1.rel = "preconnect"
        preconnect1.href = "https://fonts.googleapis.com"
        document.head?.appendChild(preconnect1)

        val preconnect2 = document.createElement("link") as HTMLLinkElement
        preconnect2.rel = "preconnect"
        preconnect2.href = "https://fonts.gstatic.com"
        preconnect2.setAttribute("crossorigin", "")
        document.head?.appendChild(preconnect2)

        // Load JetBrains Mono font
        val fontLink = document.createElement("link") as HTMLLinkElement
        fontLink.rel = "stylesheet"
        fontLink.href =
            "https://fonts.googleapis.com/css2?family=JetBrains+Mono:ital,wght@0,100..800;1,100..800&display=swap"
        document.head?.appendChild(fontLink)
    }
}

// Global utility function to load highlight.js theme
fun loadHighlightJsTheme(colorMode: ColorMode) {
    val themeUrl = when (colorMode) {
        ColorMode.LIGHT -> "/highlight.js/styles/a11y-light.min.css"
        ColorMode.DARK -> "/highlight.js/styles/a11y-dark.min.css"
    }

    // Remove existing highlight themes
    val existingThemes = document.querySelectorAll("link[data-highlight-theme]")
    for (i in 0 until existingThemes.length) {
        existingThemes.item(i)?.let { element ->
            element.parentNode?.removeChild(element)
        }
    }

    // Add new theme
    val link = document.createElement("link") as HTMLLinkElement
    link.rel = "stylesheet"
    link.href = themeUrl
    link.setAttribute("data-highlight-theme", colorMode.name.lowercase())
    document.head?.appendChild(link)
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    var colorMode by ColorMode.currentState

    // Load appropriate highlight.js theme on app startup and color mode change
    LaunchedEffect(colorMode) {
        loadHighlightJsTheme(colorMode)
    }

    SilkApp {
        Surface(SmoothColorStyle.toModifier().fillMaxHeight()) {
            content()
        }
    }
}
