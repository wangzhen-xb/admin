---
name: dev-helper
description: Development helper for Vue 3 + TypeScript projects
---
# dev-helper

A comprehensive development helper for Vue 3 + TypeScript projects, designed to streamline development workflows, generate boilerplate code, and improve code quality.

## Prerequisites

- Python 3.7+
- Node.js 18.12.0+
- pnpm 8.10.0+

## Features

- **Code Generator**: Generate Vue components, pages, API services, and more
- **Project Analyzer**: Analyze project structure and provide optimization suggestions
- **Code Quality Checker**: Check code quality and provide improvement recommendations
- **Development Tools**: Integrate common development tools and commands
- **Documentation Generator**: Generate project documentation and API documentation

## How to Use

### 1. Generate Code

#### Generate Vue Component

```bash
python skills/dev-helper/scripts/generate.py component "ComponentName" --type=basic
```

#### Generate Page

```bash
python skills/dev-helper/scripts/generate.py page "PageName"
```

#### Generate API Service

```bash
python skills/dev-helper/scripts/generate.py api "ApiName"
```

### 2. Analyze Project

```bash
python skills/dev-helper/scripts/analyze.py project
```

### 3. Check Code Quality

```bash
python skills/dev-helper/scripts/check.py quality
```

### 4. Run Development Tools

#### Start Development Server

```bash
python skills/dev-helper/scripts/tools.py dev
```

#### Build Project

```bash
python skills/dev-helper/scripts/tools.py build
```

#### Run Lint

```bash
python skills/dev-helper/scripts/tools.py lint
```

## Project Structure

```
dev-helper/
├── data/
│   ├── templates/
│   │   ├── component/
│   │   ├── page/
│   │   └── api/
│   └── rules/
│       ├── quality.json
│       └── structure.json
├── scripts/
│   ├── generate.py
│   ├── analyze.py
│   ├── check.py
│   └── tools.py
└── SKILL.md
```

## Templates

The skill includes templates for:

- **Basic Components**: Vue 3 components with TypeScript support
- **Pages**: Complete page structures with routing
- **API Services**: Axios-based API services with TypeScript types
- **Hooks**: Custom Vue composition API hooks

## Configuration

You can customize the behavior by editing the configuration files in the `data/rules` directory.

## Examples

### Generate a Basic Component

```bash
python skills/dev-helper/scripts/generate.py component "Button" --type=basic
```

### Analyze Project Structure

```bash
python skills/dev-helper/scripts/analyze.py project
```

### Run Development Server

```bash
python skills/dev-helper/scripts/tools.py dev
```
