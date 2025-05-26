---
title: Building
layout: default
parent: Getting Started
nav_order: 3
---

# Building

To build the project run in root folder of the project
``` shell
./mvnw clean package
```

If you desire to skip tests execution during build run
``` shell
./mvnw clean package -DskipTests
```

Or to skip test compilation and execution run
``` shell
./mvnw clean package -Dmaven.test.skip=true
```