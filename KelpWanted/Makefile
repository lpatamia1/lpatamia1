# Simple Makefile to launch a local web server

run:
	@echo "🚀 Starting local server at http://localhost:8000 ..."
	@python3 -m http.server 8000

clean:
	@echo "🧹 Cleaning up temporary files..."
	@find . -type f -name '*.pyc' -delete
	@find . -type d -name '__pycache__' -exec rm -rf {} +
	@echo "✨ Clean complete!"