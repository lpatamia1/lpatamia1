// 🌈 Gradient helper
function makeGradient(ctx, c1, c2) {
  const g = ctx.createLinearGradient(0, 0, ctx.canvas.width, ctx.canvas.height);
  g.addColorStop(0, c1);
  g.addColorStop(1, c2);
  return g;
}

// 🧭 Responsive padding helper
function getChartPadding() {
  const w = window.innerWidth;
  if (w > 1200) return { top: 20, bottom: 40, left: 10, right: 10 };
  if (w > 800) return { top: 15, bottom: 30, left: 8, right: 8 };
  return { top: 10, bottom: 20, left: 6, right: 6 };
}

// 🎨 Palettes
const COLORS = {
  Applied: ['#c084fc', '#d4bcedff'],
  Interview: ['#60a5fa', '#a5c9f6ff'],
  Closed: ['#facc15', '#fef9c3'],
  Rejected: ['#fb7185', '#e3949dff'],
  Hired: ['#34d399', '#a7f3d0'],
  Other: ['#14b8a6', '#99f6e4']
};

const SOURCE_COLORS = [
  ['#3b7650ff', '#75a284ff'],
  ['#3b82f6', '#bfdbfe'],
  ['#a855f7', '#e9d5ff'],
  ['#f97316', '#fed7aa'],
  ['#ec4899', '#fbcfe8'],
  ['#14b8a6', '#99f6e4'],
  ['#eab308', '#fef08a'],
  ['#10b981', '#a7f3d0']
];

// 🟣 STATUS CHART
{
  const ctx = document.getElementById('statusChart').getContext('2d');
  const labels = Object.keys(statusData);
  const data = Object.values(statusData);
  const bgColors = labels.map(l => {
    const k = Object.keys(COLORS).find(c =>
      l.toUpperCase().includes(c.toUpperCase())
    ) || 'Other';
    return makeGradient(ctx, ...COLORS[k]);
  });

  new Chart(ctx, {
    type: 'bar',
    data: { 
      labels, 
      datasets: [{ 
        data, 
        backgroundColor: bgColors, 
        borderRadius: 6 }] },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      layout: { padding: getChartPadding() },
      plugins: {
        title: {
          display: true,
          text: '📂 Status Distribution',
          color: '#333',
          font: { size: 24, weight: 600 },
          align: 'center',
          padding: { top: 10, bottom: 20 }
        },
        legend: {
          display: false
        }
      },
      scales: {
        x: { ticks: { color: '#777' }, grid: { color: 'rgba(0,0,0,0.05)' } },
        y: { ticks: { color: '#777' }, grid: { color: 'rgba(0,0,0,0.05)' } }
      }
    }
  });
}

// 🟢 TOP SOURCES
{
  const ctx = document.getElementById('sourceChart').getContext('2d');
  const labels = Object.keys(sourceData);
  const data = Object.values(sourceData);
  const bgColors = labels.map((_, i) =>
    makeGradient(ctx, ...SOURCE_COLORS[i % SOURCE_COLORS.length])
  );

  new Chart(ctx, {
    type: 'bar',
    data: { labels, datasets: [{ data, backgroundColor: bgColors, borderRadius: 6 }] },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      layout: { padding: getChartPadding() },
      plugins: {
        title: {
          display: true,
          text: '🌍 Top Sources',
          color: '#333',
          font: { size: 24, weight: 600 },
          align: 'center',
          padding: { top: 10, bottom: 20 }
        },
        legend: {
          display: false
        }
      },
      scales: {
        x: { ticks: { color: '#777' }, grid: { color: 'rgba(0,0,0,0.05)' } },
        y: { ticks: { color: '#777' }, grid: { color: 'rgba(0,0,0,0.05)' } }
      }
    }
  });
}

// 💗 WEEKLY TREND
{
  const ctx = document.getElementById('timelineChart').getContext('2d');
  const labels = Object.keys(weeklyData).map(d => {
    const date = new Date(d);
    return date.toLocaleString('default', { month: 'short' });
  });
  const data = Object.values(weeklyData);
  const g = makeGradient(ctx, 'rgba(236,72,153,0.4)', 'rgba(236,72,153,0.1)');

  new Chart(ctx, {
    type: 'line',
    data: {
      labels,
      datasets: [{
        label: 'Applications per Month',
        data,
        fill: true,
        tension: 0.35,
        borderColor: '#ec4899',
        backgroundColor: g,
        pointRadius: 4,
        pointBackgroundColor: '#ec4899'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      layout: { padding: getChartPadding() },
      plugins: {
        title: {
          display: true,
          text: '📅 Weekly Application Trend',
          color: '#333',
          font: { size: 24, weight: 600 },
          align: 'center',
          padding: { top: 10, bottom: 20 }
        },
        legend: {
          position: 'bottom',
          align: 'center',
          labels: {
            boxWidth: 14,
            padding: 12,
            color: '#555',
            font: { size: 13 }
          }
        }
      },
      scales: {
        x: {
          ticks: { color: '#444', font: { size: 13 } },
          grid: { display: false }
        },
        y: {
          ticks: { color: '#666', font: { size: 12 } },
          grid: { color: 'rgba(0,0,0,0.05)' }
        }
      }
    }
  });
}

// 🌀 Responsive resizing
window.addEventListener('resize', () => {
  Object.values(Chart.instances).forEach(chart => {
    chart.options.layout.padding = getChartPadding();
    chart.update();
  });
});
