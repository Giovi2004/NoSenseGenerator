---
title: Deployment
layout: default
parent: Development
nav_order: 3
---

# Deployment

{: .warning }
> ## Security Considerations
> Before deploying this project to a production environment, be aware of these potential security risks and limitations

### Storage
- Files are currently saved in the system temp directory
- No automatic cleanup mechanism for generated files
- No file size limitations implemented
- No storage quota management

### Input/Output Security
- User input is not sanitized

### Authentication & Authorization
- No user authentication system
- No role-based access control
- No request limitations implemented

### Cloud Resources
- No usage quotas configured
- Watch out for Google Cloud billing - could lead to unexpected costs
- No monitoring/logging or alerting system in place

### Recommended Actions
1. Implement proper file storage with cleanup routines
2. Add input sanitization and validation
3. Set up user authentication and authorization
4. Configure rate limiting and usage quotas
5. Implement monitoring/logging and alerting