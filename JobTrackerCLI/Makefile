# ==============================================================
# 🧭 Job Application Tracker - Makefile (v3.1)
# Compiles, runs, and tests all Java sources recursively
# ==============================================================
JAVAC = javac
JAVA  = java
SRC_DIR = tracker
MAIN = tracker.JobApplicationTracker
JUNIT_JAR = lib/junit-platform-console-standalone.jar

# --------------------------------------------------------------
# 🧩 Source files (recursive, excluding tests)
# --------------------------------------------------------------
SOURCES := $(shell find $(SRC_DIR) -name "*.java" ! -path "*/test/*")
CLASSES := $(SOURCES:.java=.class)

# --------------------------------------------------------------
# 🏗️ Default target: build all
# --------------------------------------------------------------
all:
	@echo "🧱 Building all Java source files from $(SRC_DIR)/..."
	@$(JAVAC) -d . $(SOURCES)
	@echo "✅ All Java files compiled successfully!"

# --------------------------------------------------------------
# ▶️ Run the CLI
# --------------------------------------------------------------
run: all
	@echo "🚀 Launching Job Application Tracker..."
	@echo "-------------------------------------------"
	@$(JAVA) $(MAIN)
	@echo "-------------------------------------------"
	@echo "🏁 Program exited successfully."

# --------------------------------------------------------------
# 🧪 Run JUnit 5 Tests
# --------------------------------------------------------------
test:
	@echo "🧪 Compiling source and test files with JUnit..."
	@$(JAVAC) -cp .:$(JUNIT_JAR) -d . $(shell find tracker -name "*.java")
	@echo "✅ Compilation successful!"
	@echo "🔍 Running automated JUnit tests..."
	@$(JAVA) -jar $(JUNIT_JAR) --class-path . --scan-class-path
	@echo "✅ All tests executed successfully!"

# --------------------------------------------------------------
# 🧹 Clean build artifacts
# --------------------------------------------------------------
clean:
	@echo "🧹 Cleaning compiled class files..."
	@find $(SRC_DIR) -name "*.class" -type f -delete
	@rm -f spotbugs-report.txt checkstyle-report.txt
	@echo "✨ Clean complete!"

# --------------------------------------------------------------
# 🔄 Rebuild everything
# --------------------------------------------------------------
rebuild: clean all

.PHONY: all run clean rebuild test
