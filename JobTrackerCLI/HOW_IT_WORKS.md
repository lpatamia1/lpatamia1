# ⚙️ HOW IT WORKS — Job Application Tracker (Terminal Edition)

A command-line Java program that helps you log, manage, and visualize job applications right from your terminal.  
Built with simplicity, personality, and efficiency in mind.

---

## 💡 Overview
This app lets you:
- Add, view, and organize job applications in a local file (`applications.txt`)
- Track totals, progress rates, and recent activity
- Generate motivational feedback and weekly stats
- Automatically export a Markdown table for your GitHub portfolio

Every application entry is stored as plain text but formatted cleanly for human readability and version control.

---

## 🧩 Core Files & Logic

| File | Purpose |
|------|----------|
| **JobApplication.java** | Defines the structure of a single job record (company, role, type, location, status, date). |
| **JobApplicationTracker.java** | The main CLI — loads data, handles user commands, and generates stats. |
| **SeedData.java** | Optional: creates starter entries when no `applications.txt` exists. |
| **applications.txt** | Stores all job applications (one per line). |
| **README.md** | Auto-updated Markdown version of your application log. |
| **Makefile** | Simplifies running, cleaning, and building. |

---

## 🧠 Program Flow
```plaintext
1. Load applications.txt → parse into JobApplication objects.
2. Count total, active, and recent entries.
3. Calculate success rate = (active / total) × 100%.
4. Display progress bar and motivational message.
5. Allow user to:
   - View or filter entries
   - Add a new application
   - Export to README.md
   - Exit gracefully

## 💻 Example Output
-----------------------------------------------------------
📬 103 applications logged — 21 active, 18.2% showing progress.
🐾 Career Cat: you’re doing great — keep applying!
🕊️ You’ve applied to 4 jobs in the last 7 days.
-----------------------------------------------------------
