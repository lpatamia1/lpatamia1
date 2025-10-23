from flask import Flask, render_template
import pandas as pd

# ✅ define app before using @app.route
app = Flask(__name__)

@app.route('/')
def dashboard():
    try:
        df = pd.read_csv('applications.csv')
    except Exception as e:
        return f"<h2>⚠️ Error loading applications.csv: {e}</h2>"

    total = len(df)

    # 🧮 summary stats
    interviews = (df['Status'].str.contains('INTERVIEW', case=False, na=False)).sum()
    rejected = (df['Status'].str.contains('REJECT', case=False, na=False)).sum()
    hired = (df['Status'].str.contains('HIRE', case=False, na=False)).sum()
    active = (df['Status'].str.contains('APPLIED', case=False, na=False)).sum()

    # 🟪 by status
    by_status = df['Status'].value_counts().to_dict()

    # 🟩 by source
    by_source = df['Source'].value_counts().nlargest(8).to_dict()

    # 💗 weekly trend
    df['Date Applied'] = pd.to_datetime(df['Date Applied'], errors='coerce')
    df = df.dropna(subset=['Date Applied'])
    df['Week'] = df['Date Applied'].dt.to_period('W').apply(lambda r: r.start_time)

    # Convert Timestamp → str for JSON safety
    weekly_apps = {
        str(k.date()): int(v)
        for k, v in df.groupby('Week').size().to_dict().items()
    }

    return render_template(
        'index.html',
        total=total,
        interviews=interviews,
        rejected=rejected,
        hired=hired,
        active=active,
        by_status=by_status,
        by_source=by_source,
        weekly_apps=weekly_apps
    )

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
