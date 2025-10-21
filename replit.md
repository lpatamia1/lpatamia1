# Lily Patamia - Portfolio Repository

## Overview
This is a personal portfolio repository showcasing projects, skills, and experience. The repository includes both a portfolio website and a Java-based job application tracker.

**Last Updated:** October 21, 2025

## Project Structure

### 1. Portfolio Website (`/Website`)
- **Description:** A retro-themed desktop portfolio with draggable windows, interactive terminal, and music player
- **Tech Stack:** HTML, CSS (VT323 font), Vanilla JavaScript
- **Features:**
  - Draggable window interface with minimize/maximize controls
  - Working terminal with custom commands (help, about, skills, projects, contact, clear)
  - Retro music player with playlist
  - Easter egg DVD screensaver
  - Responsive design with pink/lavender aesthetic

### 2. Job Application Tracker (`/ApplicationTracker`)
- **Description:** Java CLI application for tracking job applications with analytics and reporting
- **Tech Stack:** Java 22.3 (GraalVM)
- **Features:**
  - Track job applications with company, role, type, location, status, date, and source
  - View summary statistics and analytics
  - Search functionality by keywords
  - Export to README markdown format
  - Import seed data from markdown
  - Automatic backups with timestamped files
  - Static analysis via SpotBugs and Checkstyle
- **Build System:** Makefile
- **Key Commands:**
  - `make all` - Compile all Java files
  - `make run` - Run the tracker
  - `make clean` - Clean build files
  - `make static` - Run SpotBugs analysis
  - `make checkstyle` - Run Checkstyle linting

### 3. Data Science Projects
- **AmazonTopBooksPandasAnalysis.ipynb** - Pandas analysis of top-selling books
- **FoodDesertsChicago.ipynb** - Chicago food desert mapping and analysis
- **MSPB.ipynb** - Data analysis notebook

### 4. Other Content
- **Assets:** GIF animations for portfolio
- **Resume:** Markdown format resume
- **RoadMap.md:** Project roadmap and goals

## Recent Changes

**October 21, 2025:**
- Initial Replit environment setup
- Configured Portfolio Website to run on port 5000 via Python HTTP server
- Created .gitignore for Java build artifacts and system files
- Tested Java ApplicationTracker compilation successfully
- Set up deployment configuration

## Project Architecture

### Website Deployment
- **Development:** Python's built-in HTTP server serves static files from `/Website` on port 5000
- **Production:** Static file hosting via Replit Autoscale deployment
- **No backend needed** - Pure static HTML/CSS/JS

### Java Application
- **Standalone CLI tool** - Not web-accessible, runs in terminal
- **Data Storage:** Plain text file (`applications.txt`) with pipe-delimited format
- **Build Process:** Makefile-based compilation with Java 22.3

## How to Use

### Run the Portfolio Website
The website runs automatically when you start the Repl. View it in the webview panel.

### Run the Job Application Tracker
```bash
cd ApplicationTracker
make run
```

### Compile Java Files
```bash
cd ApplicationTracker
make all
```

### Run Code Quality Checks
```bash
cd ApplicationTracker
make checkstyle
make static
```

## User Preferences
- Aesthetic: Pink/lavender color scheme, retro/nostalgic design
- Focus areas: Software development, data science, environmental science
- Portfolio showcases full-stack, data analysis, and creative coding projects
