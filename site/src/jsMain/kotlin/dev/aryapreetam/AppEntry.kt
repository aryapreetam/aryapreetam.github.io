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

    // Optimized font loading - fonts are now preloaded in build.gradle.kts
    // Just ensure they're applied properly to code elements
    loadFontStyles()
}

// Optimized font loading without duplicating requests
private fun loadFontStyles() {
    // Only add CSS styles, don't load fonts again since they're preloaded
    val globalCodeStyleId = "global-jetbrains-mono"
    if (document.getElementById(globalCodeStyleId) == null) {
        val style = document.createElement("style").apply {
            setAttribute("id", globalCodeStyleId)
            textContent = """
                code, pre code, .hljs, 
                code[class*="language-"], 
                pre[class*="language-"] code {
                    font-family: 'JetBrains Mono', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace !important;
                    font-feature-settings: 'liga' 0 !important;
                    font-variant-ligatures: none !important;
                    font-display: swap;
                }
            """.trimIndent()
        }
        document.head?.appendChild(style)
    }
}

// Global utility function to load highlight.js theme with integrity checks
fun loadHighlightJsTheme(colorMode: ColorMode) {
    val themeUrl = when (colorMode) {
        ColorMode.LIGHT -> "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/a11y-light.min.css"
        ColorMode.DARK -> "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/a11y-dark.min.css"
    }

    val integrity = when (colorMode) {
        ColorMode.LIGHT -> "sha512-Vkn75cTacPzd6nqnFF0oHeIjgoAp3jNKAUDR9N7kBJSqYlWyBYRAh+MKEfxs6LJUvTQxKkxl8k2+zSKkELV2hA=="
        ColorMode.DARK -> "sha512-1xVFABKbIQJrRx/Hx9G8PhW9j1lEbhPqjFMI8LhOHk2RfJuCYk4R5/4GvdKxOmGjBJZPJqG/EKu5J6AcLmjP7w=="
    }

    // Remove existing highlight themes
    val existingThemes = document.querySelectorAll("link[data-highlight-theme]")
    for (i in 0 until existingThemes.length) {
        existingThemes.item(i)?.let { element ->
            element.parentNode?.removeChild(element)
        }
    }

    // Add new theme with integrity check
    val link = document.createElement("link") as HTMLLinkElement
    link.rel = "stylesheet"
    link.href = themeUrl
    link.setAttribute("integrity", integrity)
    link.setAttribute("crossorigin", "anonymous")
    link.setAttribute("data-highlight-theme", colorMode.name.lowercase())
    document.head?.appendChild(link)
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    var colorMode by ColorMode.currentState

    // Theme loading is now handled in individual CodeBlock components using Prism.js
    // This provides better reliability and .kts file support as identified in dev logs

    SilkApp {
        Surface(SmoothColorStyle.toModifier().fillMaxHeight()) {
            content()
        }
    }
}
