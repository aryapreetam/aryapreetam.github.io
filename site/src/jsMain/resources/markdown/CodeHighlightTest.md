---
title: "Code Highlighting Test"
description: "Testing syntax highlighting with various programming languages"
date: "2024-01-21"
tags: ["testing", "code", "syntax"]
author: "Arya Preetam"
layout: "dev.aryapreetam.components.layouts.MarkdownLayout"
---

# Code Highlighting Test

This post tests code block syntax highlighting with different programming languages.

## Kotlin Code

```kotlin
@Composable
fun HelloWorld(name: String = "World") {
    val greeting = remember { "Hello, $name!" }
    
    Text(
        text = greeting,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(16.dp)
    )
}

data class User(
    val id: Long,
    val name: String,
    val email: String?
)
```

## JavaScript Code

```js
function fetchUserData(userId) {
    return fetch(`/api/users/${userId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            return null;
        });
}

const greetUser = (user) => {
    console.log(`Hello, ${user.name}!`);
};
```

## Python Code

```python
from typing import Optional, List
import asyncio

class UserService:
    def __init__(self, api_client):
        self.api_client = api_client
    
    async def get_user(self, user_id: int) -> Optional[dict]:
        try:
            response = await self.api_client.get(f"/users/{user_id}")
            return response.json()
        except Exception as e:
            print(f"Error fetching user {user_id}: {e}")
            return None
    
    async def get_multiple_users(self, user_ids: List[int]) -> List[dict]:
        tasks = [self.get_user(user_id) for user_id in user_ids]
        results = await asyncio.gather(*tasks, return_exceptions=True)
        return [r for r in results if r is not None]
```

## YAML Configuration

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
  namespace: production
data:
  database.host: "localhost"
  database.port: "5432"
  database.name: "myapp"
  logging.level: "INFO"
  features:
    - authentication
    - analytics
    - caching
```

## Bash Script

```bash
#!/bin/bash

# Setup script for development environment
PROJECT_DIR="/workspace/blog"
NODE_VERSION="18.17.0"

check_dependencies() {
    echo "Checking system dependencies..."
    
    if ! command -v node &> /dev/null; then
        echo "Node.js not found. Installing version $NODE_VERSION..."
        curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
        source ~/.bashrc
        nvm install $NODE_VERSION
        nvm use $NODE_VERSION
    fi
    
    if ! command -v ./gradlew &> /dev/null; then
        echo "Gradle wrapper not found!"
        exit 1
    fi
}

setup_project() {
    echo "Setting up project..."
    cd $PROJECT_DIR
    ./gradlew clean build
    
    if [ $? -eq 0 ]; then
        echo "✅ Project setup completed successfully!"
    else
        echo "❌ Project setup failed!"
        exit 1
    fi
}

check_dependencies
setup_project
```

## Inline Code Examples

Here are some examples of `inline code` that should be styled with JetBrains Mono font:

- Use `kubectl get pods` to list all pods
- The `Array.prototype.map()` method creates a new array
- Import with `import { useState } from 'react'`
- Run the server with `./gradlew run`
- Set environment variable: `export NODE_ENV=production`

## JSON Data

```json:package.json
{
  "name": "My Blog",
  "version": "1.0.0",
  "description": "A modern blog built with Kobweb",
  "author": {
    "name": "Arya Preetam",
    "email": "arya@example.com"
  },
  "dependencies": {
    "kobweb": "^0.16.0",
    "kotlin": "1.9.0"
  },
  "scripts": {
    "dev": "./gradlew kobwebStart -t",
    "build": "./gradlew kobwebExport"
  },
  "keywords": ["kotlin", "kobweb", "blog", "web"]
}
```

## Build Configuration

```kotlin:build.gradle.kts
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kobwebx.markdown)
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(libs.kobweb.core)
                implementation(libs.kobweb.silk)
            }
        }
    }
}
```