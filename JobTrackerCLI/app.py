from flask import Flask, render_template
import pandas as pd
import numpy as np
import re

app = Flask(__name__)
@app.route('/')
def dashboard():
    try:
        df = pd.read_csv('applications.csv')
    except Exception as e:
        return f"<h2>⚠️ Error loading applications.csv: {e}</h2>"

    # 🧹 Replace NaN with empty strings for safe JSON conversion
    df = df.fillna("")

    # 🔗 Convert Markdown-style [Text](URL) → clickable <a> link
    def convert_markdown_links(text):
        if isinstance(text, str):
            return re.sub(
                r'\[([^\]]+)\]\((https?://[^\)]+)\)',
                r'<a href="\2" target="_blank" style="color:#4e6a57; text-decoration:none; font-weight:600;">\1 ↗</a>',
                text
            )
        return text

    # 🪄 Apply only to the “Company” column
    if 'Company' in df.columns:
        df['Company'] = df['Company'].apply(convert_markdown_links)

    total = len(df)

    # 🧮 Summary stats
    interviews = (df['Status'].str.contains('INTERVIEW', case=False, na=False)).sum()
    rejected = (df['Status'].str.contains('REJECT', case=False, na=False)).sum()
    hired = (df['Status'].str.contains('HIRE', case=False, na=False)).sum()
    active = (df['Status'].str.contains('APPLIED', case=False, na=False)).sum()

    # 🟪 By status
    by_status = df['Status'].value_counts().to_dict()

    # 🟩 By source
    by_source = df['Source'].value_counts().nlargest(8).to_dict()
    print("📄 CSV Columns:", list(df.columns))

    # 🔍 Count research-related applications
    research_keywords = ["Research", "R&D", "Scientist"]
    research_count = df['Role'].str.contains('|'.join(research_keywords), case=False, na=False).sum()

    # 📅 Max applications submitted in a single day
    if 'Date Applied' in df.columns:
        max_single_day = df['Date Applied'].value_counts().max()
    else:
        max_single_day = 0

    # 💗 Monthly trend
    weekly_apps = {}
    if 'Date Applied' in df.columns:
        dates = pd.to_datetime(
            df['Date Applied'].astype(str).str.strip(),
            errors='coerce',
            infer_datetime_format=True,
            dayfirst=False
        ).dropna()

        if not dates.empty:
            periods = dates.dt.to_period('M')
            today_p = pd.Period(pd.Timestamp.today(), freq='M')
            start_p = periods.min()
            end_p = max(periods.max(), today_p)
            full_range = pd.period_range(start=start_p, end=end_p, freq='M')

            counts = periods.value_counts().reindex(full_range, fill_value=0).sort_index()

            weekly_apps = {
                p.to_timestamp().strftime('%b %Y'): int(counts[p])
                for p in full_range
            }

    # 🔥 Streak calculation
    streak = 0
    last_dates = sorted(pd.to_datetime(df['Date Applied'], errors='coerce').dropna().unique(), reverse=True)

    if last_dates:
        today = pd.Timestamp.today().normalize()
        current = today
        for d in last_dates:
            if d == current or d == current - pd.Timedelta(days=1):
                streak += 1
                current = d
            else:
                break

    # 🧾 Job list
    jobs = df.to_dict(orient='records')
    weekly_apps = dict(sorted(weekly_apps.items(), key=lambda x: x[0]))

    # ⭐ Skill match star chart
    skill_match = {
        "Python": 8,
        "Java": 7,
        "Flask": 6,
        "SQL": 5,
        "React": 4,
        "Git": 9
    }

    # 🧠 Technical prep tracker
    tech_prep = {
        "Data Structures": 70,
        "System Design": 40,
        "LeetCode Practice": 60,
        "Behavioral Prep": 80,
        "Portfolio Updates": 50
    }

    # 🔥 Skill heatmap
    skills = list(skill_match.keys())
    companies = [j['Company'] for j in jobs[:6]] if jobs else []
    heatmap_values = np.random.randint(40, 100, size=(len(skills), len(companies))).tolist() if companies else []

    # 📝 Learning log
    learning_log = [
        {"Date": "2025-10-01", "Topic": "SQL Joins Review", "Hours": 2, "Reflection": "Improved efficiency using subqueries"},
        {"Date": "2025-10-08", "Topic": "Flask Blueprints", "Hours": 3, "Reflection": "Modularized dashboard routes"},
        {"Date": "2025-10-14", "Topic": "Chart.js Customization", "Hours": 1, "Reflection": "Learned gradient fills"},
    ]

    # 🎨 Render to dashboard (✅ now includes all new data)
    return render_template(
        'index.html',
        streak=streak,
        research_count=research_count,
        max_single_day=max_single_day,
        total=total,
        interviews=interviews,
        rejected=rejected,
        hired=hired,
        active=active,
        by_status=by_status,
        by_source=by_source,
        weekly_apps=weekly_apps,
        jobs=jobs,
        skill_match=skill_match,
        tech_prep=tech_prep,
        heatmap_values=heatmap_values,
        skills=skills,
        companies=companies,
        learning_log=learning_log
    )

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
