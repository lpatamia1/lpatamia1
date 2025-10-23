from flask import Flask, render_template
import pandas as pd

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
    import re
    
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

    # 💗 Monthly trend — formatted for chart alignment & includes October
    weekly_apps = {}
    if 'Date Applied' in df.columns:
        dates = pd.to_datetime(
            df['Date Applied'].astype(str).str.strip(),
            errors='coerce',
            infer_datetime_format=True,
            dayfirst=False
        ).dropna()

        if not dates.empty:
            # Convert to monthly periods
            periods = dates.dt.to_period('M')

            # Force full continuous range (start → latest parsed or current month)
            today_p = pd.Period(pd.Timestamp.today(), freq='M')
            start_p = periods.min()
            end_p = max(periods.max(), today_p)
            full_range = pd.period_range(start=start_p, end=end_p, freq='M')

            # Count occurrences and include missing months as 0
            counts = periods.value_counts().reindex(full_range, fill_value=0).sort_index()

            # 🔥 Explicitly print to verify October count
            print("📊 Monthly counts:")
            print(counts)

            # ✅ Create ordered dict with pretty month labels (e.g., "Apr 2025")
            weekly_apps = {
                p.to_timestamp().strftime('%b %Y'): int(counts[p])
                for p in full_range
            }

    # 🧾 Job list for search section
    jobs = df.to_dict(orient='records')

    # 🪄 Sort by chronological key to preserve order in the chart
    weekly_apps = dict(sorted(weekly_apps.items(), key=lambda x: x[0]))

    # 🎨 Render all to dashboard
    return render_template(
        'index.html',
        total=total,
        interviews=interviews,
        rejected=rejected,
        hired=hired,
        active=active,
        by_status=by_status,
        by_source=by_source,
        weekly_apps=weekly_apps,
        jobs=jobs
    )


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
