---
title: Tests Report
layout: default
parent: Development
nav_order: 1
---

# Tests Report

To generate the tests report, in the root folder of the project, run
```shell
mvn site
```
This will generate HTML reports in `target/site` directory. Open the `surefire-report.html` file in the browser. You will get an output like below image.

![Test Report HTML][test-screenshot]

[test-screenshot]: /images/test-screenshot.png