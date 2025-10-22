from flask import Flask, render_template_string
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go

app = Flask(__name__)

@app.route('/')
def dashboard():
    try:
        df = pd.read_csv('applications.csv')
    except Exception as e:
        return f"<h2>⚠️ Error loading applications.csv: {e}</h2>"

    total = len(df)

    # 🧮 Quick summary stats
    interviews = (df['Status'].str.contains('INTERVIEW', case=False, na=False)).sum()
    rejected = (df['Status'].str.contains('REJECT', case=False, na=False)).sum()
    hired = (df['Status'].str.contains('HIRE', case=False, na=False)).sum()
    active = (df['Status'].str.contains('APPLIED', case=False, na=False)).sum()
    success_rate = 100 * (interviews + hired) / max(total, 1)
    # 🌍 Global Overview (pie chart of major categories)
    global_data = {
        "Applied": active,
        "Interviewed": interviews,
        "Rejected": rejected,
        "Hired": hired
    }
    fig_global = px.pie(
        names=list(global_data.keys()),
        values=list(global_data.values()),
        title=None,
        color_discrete_sequence=px.colors.qualitative.Pastel
    )
    fig_global.update_traces(
        textinfo='label+percent',
        insidetextorientation='radial',
        textfont=dict(size=13, color="#333", family="Inter"),
        hovertemplate="<b>%{label}</b><br>%{value} applications<extra></extra>"
    )
    fig_global.update_layout(
        height=320,
        showlegend=False,
        margin=dict(t=20, b=20),
        paper_bgcolor='rgba(0,0,0,0)',
        plot_bgcolor='rgba(0,0,0,0)'
    )

    top_source = df['Source'].value_counts().idxmax() if not df['Source'].empty else "—"
    top_location = df['Location'].value_counts().idxmax() if not df['Location'].empty else "—"

    # 🧩 Status Distribution
    status_counts = df['Status'].value_counts().reset_index()
    status_counts.columns = ['Status', 'Count']
    fig_status = px.bar(
        status_counts,
        x='Status', y='Count',
        title=None,
        color='Status',
        color_discrete_sequence=px.colors.qualitative.Pastel,
        text='Count',
        height=340  # smaller height
    )
    fig_status.update_traces(textposition='outside', textfont=dict(size=13))
    fig_status.update_yaxes(range=[0, status_counts['Count'].max() * 1.3])  # more headroom

    fig_status.update_layout(
        plot_bgcolor='rgba(0,0,0,0)',
        paper_bgcolor='rgba(0,0,0,0)',
        font=dict(color="#333", size=13),
        title_font=dict(size=20, color="#9b5de5"),
        margin=dict(t=60, b=40)
    )

    # 🌐 Top Sources
    source_counts = df['Source'].value_counts().head(7).reset_index()
    source_counts.columns = ['Source', 'Count']
    fig_sources = px.bar(
        source_counts,
        x='Source', y='Count',
        title=None,
        color='Source',
        color_discrete_sequence=px.colors.qualitative.Set3,
        text='Count',
        height=320
    )
    fig_sources.update_traces(textposition='outside')
    fig_sources.update_yaxes(range=[0, source_counts['Count'].max() * 1.25])

    fig_sources.update_layout(
        plot_bgcolor='rgba(0,0,0,0)',
        paper_bgcolor='rgba(0,0,0,0)',
        font=dict(color="#333", size=13),
        title_font=dict(size=20, color="#00bb8f"),
        margin=dict(t=60, b=40)
    )

    # 📅 Weekly Application Trend
    df['Date Applied'] = pd.to_datetime(df['Date Applied'], errors='coerce')
    df = df.dropna(subset=['Date Applied'])
    df['Week'] = df['Date Applied'].dt.to_period('W').apply(lambda r: r.start_time)
    trend = df.groupby('Week').size().reset_index(name='Applications')

    fig_trend = go.Figure()
    fig_trend.add_trace(go.Scatter(
        x=trend['Week'],
        y=trend['Applications'],
        mode='lines+markers',
        line=dict(color='#ff4fa3', width=3),
        fill='tozeroy',
        fillcolor='rgba(255,79,163,0.2)',
        name='Applications per Week'
    ))
    fig_trend.update_layout(
        title=None,
        xaxis_title='Week',
        yaxis_title='Applications',
        font=dict(color="#333", size=13),
        plot_bgcolor='rgba(0,0,0,0)',
        paper_bgcolor='rgba(0,0,0,0)',
        title_font=dict(size=20, color="#ff4fa3"),
        margin=dict(t=60, b=40),
        height=330
    )
    # 🌍 Global Applications Map
    # Extract city/state/country from your CSV's "Location" column
    # 🇺🇸 US Application Map
    geo_df = df.copy()
    geo_df['Location'] = geo_df['Location'].fillna("Unknown")

    # Basic location-to-coordinate map (customize or expand as you add data)
    coords = {
        "Chicago, IL": (41.8781, -87.6298),
        "New York, NY": (40.7128, -74.0060),
        "Los Angeles, CA": (34.0522, -118.2437),
        "San Francisco, CA": (37.7749, -122.4194),
        "Seattle, WA": (47.6062, -122.3321),
        "Austin, TX": (30.2672, -97.7431),
        "Boston, MA": (42.3601, -71.0589),
        "Atlanta, GA": (33.7490, -84.3880),
        "Miami, FL": (25.7617, -80.1918),
        "Denver, CO": (39.7392, -104.9903),
        "Dallas, TX": (32.7767, -96.7970),
        "Washington, DC": (38.9072, -77.0369),
    }

    geo_df[['Lat', 'Lon']] = geo_df['Location'].apply(
        lambda loc: pd.Series(coords.get(loc, (None, None)))
    )
    geo_df = geo_df.dropna(subset=['Lat', 'Lon'])

    fig_globe = px.scatter_geo(
        geo_df,
        lat='Lat',
        lon='Lon',
        hover_name='Company' if 'Company' in df.columns else 'Location',
        color='Status',
        title=None,
        scope='usa',  # 🔹 This zooms into just the USA
        size_max=15,
        color_discrete_sequence=px.colors.qualitative.Set3
    )
    fig_globe.update_layout(
        margin=dict(t=0, b=0, l=0, r=0),
        geo=dict(
            showland=True,
            landcolor='rgb(245, 245, 245)',
            showcountries=True,
            countrycolor='rgb(217, 217, 217)',
        ),
        height=380
    )


    # 🌸 Dashboard HTML
    html = f"""
    <html>
    <head>
        <title>Job Application Dashboard</title>
        <style>
            body {{
                font-family: 'Inter', 'Segoe UI', sans-serif;
                background-color: #f8f3fa;
                color: #333;
                margin: 0;
                padding: 0;
            }}
            h1 {{
                text-align: center;
                color: #9b5de5;
                font-size: 2.5em;
                margin-top: 40px;
                margin-bottom: 8px;
            }}
            .metric {{
                text-align: center;
                font-size: 1.1em;
                color: #444;
                margin-bottom: 25px;
                line-height: 1.6;
            }}
            .summary-cards {{
                display: flex;
                justify-content: center;
                gap: 20px;
                flex-wrap: wrap;
                margin-bottom: 30px;
            }}
            .stat-card {{
                background: #fff;
                border-radius: 14px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
                width: 190px;
                text-align: center;
                padding: 14px 10px;
                transition: transform 0.2s ease;
            }}
            .stat-card:hover {{
                transform: scale(1.05);
            }}
            .stat-card h3 {{
                margin: 4px 0;
                color: #9b5de5;
                font-size: 0.95em;
            }}
            .stat-card p {{
                margin: 0;
                font-size: 1.1em;
                font-weight: bold;
                color: #333;
            }}
            .card {{
                background: #fff;
                border-radius: 14px;
                box-shadow: 0 3px 12px rgba(0,0,0,0.08);
                padding: 20px;
                margin: 20px auto;
                width: 80%;
                transition: transform 0.2s ease;
            }}
            .card:hover {{
                transform: translateY(-3px);
            }}
            .chart {{
                margin-top: 5px;
            }}
        </style>
    </head>
    <body>
        <h1>📊 Job Application Dashboard</h1>

        <div class="metric">
            <strong>{total}</strong> total applications logged 🌸<br>
            <span style="color:#9b5de5;">💬 {interviews}</span> interviews • 
            <span style="color:#ff595e;">❌ {rejected}</span> rejected • 
            <span style="color:#00bb8f;">✅ {hired}</span> hired • 
            <span style="color:#ffca3a;">🕐 {active}</span> active
        </div>

        <div class="summary-cards">
            <div class="stat-card">
                <h3>🌐 Top Source</h3>
                <p>{top_source}</p>
            </div>
            <div class="stat-card">
                <h3>📍 Top Location</h3>
                <p>{top_location}</p>
            </div>
            <div class="stat-card">
                <h3>📈 Success Rate</h3>
                <p>{success_rate:.1f}%</p>
            </div>
        </div>
        <div class="card" style="width:85%; margin:auto; margin-top:25px;">
            <h2>🗺️ Global Application Map</h2>
            <div class="chart">{fig_globe.to_html(full_html=False, include_plotlyjs='cdn')}</div>
        </div>
        <div class="card" style="width:70%; margin:auto; margin-top:30px;">
            <h2>Global Application Breakdown</h2>
            <div class="chart">{fig_global.to_html(full_html=False, include_plotlyjs='cdn')}</div>
        </div>
        <div class="card">
            <h2>Status Distribution</h2>
            <div class="chart">{fig_status.to_html(full_html=False, include_plotlyjs='cdn')}</div>
        </div>
        <div class="card">
            <h2>Top Sources</h2>
            <div class="chart">{fig_sources.to_html(full_html=False, include_plotlyjs=False)}</div>
        </div>

        <div class="card">
            <h2>Weekly Application Trend</h2>
            <div class="chart">{fig_trend.to_html(full_html=False, include_plotlyjs=False)}</div>
        </div>
    </body>
    </html>
    """

    return render_template_string(html)

if __name__ == '__main__':
    app.run(debug=True)
