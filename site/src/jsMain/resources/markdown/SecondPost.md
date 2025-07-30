---
title: "Building Components with Silk"
description: "Learn how to create reusable UI components using Kobweb's Silk UI framework"
date: "2024-01-20"
tags: ["kotlin", "kobweb", "silk", "ui-components"]
draft: false
layout: "dev.aryapreetam.components.layouts.MarkdownLayout"
---

# Building Components with Silk

In this post, we'll explore how to create reusable UI components using **Silk**, Kobweb's built-in UI framework.

## What is Silk?

Silk is Kobweb's answer to component libraries like Material-UI or Chakra UI. It provides:

- Pre-built components
- Theming support
- Responsive design utilities
- Accessibility features

## Creating a Custom Button

Here's how you can create a custom button component:

```kotlin
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant? = null
) {
    Button(
        onClick = { onClick() },
        modifier = ButtonStyle.toModifier(variant).then(modifier)
    ) {
        Text(text)
    }
}
```

## Styling with CSS

You can create custom styles using Kobweb's CSS-in-Kotlin approach:

```kotlin
val ButtonStyle = CssStyle {
    base {
        Modifier
            .padding(12.px)
            .borderRadius(8.px)
            .backgroundColor(Colors.Blue)
            .color(Colors.White)
    }
    
    hover {
        Modifier.backgroundColor(Colors.DarkBlue)
    }
}
```

## Next Steps

In the next post, we'll explore how to handle routing and navigation in Kobweb applications.

Stay tuned!