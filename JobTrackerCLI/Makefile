# ==============================================================
# 🧭 Job Application Tracker - Makefile (v3.0)
# Compiles, runs, and checks all Java sources recursively
# ==============================================================
JAVAC = javac
JAVA  = java
SRC_DIR = tracker
MAIN = tracker.JobApplicationTracker

# --------------------------------------------------------------
# 🧩 Source files (recursive)
# --------------------------------------------------------------
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
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

# --------------------------------------------------------------
# 🧪 Test compilation (verbose)
# --------------------------------------------------------------
test:
	@echo "🧪 Test compiling with detailed warnings..."
	@$(JAVAC) -Xlint:all -Xdiags:verbose -d . $(SOURCES)
	@echo "✅ Test build finished successfully."

.PHONY: all run clean rebuild test
