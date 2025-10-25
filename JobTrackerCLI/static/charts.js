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

  const labels = window.timelineLabels; 
  const data = window.timelineData;

  const g = makeGradient(ctx, 'rgba(72, 162, 236, 0.4)', 'rgba(72, 129, 236, 0.1)');

  new Chart(ctx, {
    type: 'line',
    data: {
      labels,
      datasets: [{
        label: 'Applications per Month',
        data,
        fill: true,
        tension: 0.35,
        borderColor: '#4894ecff',
        backgroundColor: g,
        pointRadius: 4,
        pointBackgroundColor: '#488aec96'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      layout: { padding: getChartPadding() },
      plugins: {
        title: {
          display: true,
          text: '📅 Application Trends',
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
          ticks: { color: '#444', font: { size: 13 }, autoSkip: false }, // ✅ all months visible
          grid: { display: false }
        },
        y: {
          beginAtZero: true,
          grace: '5%',
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

// ⭐ Skill Radar Chart
{
  const radarCanvas = document.getElementById('skillRadar');
  if (radarCanvas) {
    const ctx = radarCanvas.getContext('2d');
    new Chart(ctx, {
      type: 'radar',
      data: {
        labels: Object.keys(skillMatch),
        datasets: [{
          label: 'Skill Proficiency',
          data: Object.values(skillMatch),
          backgroundColor: 'rgba(239,68,68,0.25)', 
          borderColor: '#ec488cd5',
          borderWidth: 2,
          pointBackgroundColor: '#ec487fff'
        }]
      },
      options: {
        scales: { r: { suggestedMin: 0, suggestedMax: 10 } },
        plugins: {

        }
      }
    });
  }
}

// 🧠 Technical Prep Progress (blue gradient 💙)
{
  const prepCanvas = document.getElementById('prepBar');
  if (prepCanvas) {
    const ctx = prepCanvas.getContext('2d');
    const g = ctx.createLinearGradient(0, 0, 0, ctx.canvas.height);
    g.addColorStop(0, 'rgba(59, 130, 246, 0.9)');  // sky blue top (#3b82f6)
    g.addColorStop(1, 'rgba(147, 197, 253, 0.85)'); // light periwinkle fade (#93c5fd)

    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: Object.keys(techPrep),
        datasets: [{
          label: 'Completion %',
          data: Object.values(techPrep),
          backgroundColor: g,
          borderWidth: 0,  // clean, no outline
          borderRadius: 8
        }]
      },
      options: {
        scales: {
          y: { beginAtZero: true, max: 100 },
          x: { grid: { display: false } }
        },
        plugins: {

          legend: { display: false }
        }
      }
    });
  }
}

// 🔥 Skill Match Heatmap (stacked-bar style)
// 🔥 Skill Match Heatmap (stacked-bar style, red theme ❤️)
{
  const heatCanvas = document.getElementById('heatmap');
  if (heatCanvas) {
    const ctx = heatCanvas.getContext('2d');

    // ❤️ Smooth red gradients (deep → soft)
    const reds = [
      ['#b91c1c', '#f87171'], // Python
      ['#dc2626', '#fca5a5'], // Java
      ['#e11d48', '#fda4af'], // Flask
      ['#be123c', '#fecaca'], // SQL
      ['#ef4444', '#fca5a5'], // React
      ['#f43f5e', '#fda4af']  // Git
    ];

    const data = {
      labels: companies.map(c => c.replace(/<[^>]*>/g, '')),  // 🧼 strips HTML tags
      datasets: skills.map((skill, i) => {
        const g = ctx.createLinearGradient(0, 0, ctx.canvas.width, 0);
        g.addColorStop(0, reds[i % reds.length][0]);
        g.addColorStop(1, reds[i % reds.length][1]);
        return {
          label: skill,
          data: heatmapValues[i],
          backgroundColor: g,
          borderRadius: 5
        };
      })
    };

    new Chart(ctx, {
      type: 'bar',
      data,
      options: {
        indexAxis: 'y',
        plugins: {
          title: {
            display: true,
            color: '#7f1d1d',
            font: { size: 20, weight: 700 },
            padding: { bottom: 10 }
          },
          legend: {
            position: 'bottom',
            labels: {
              color: '#991b1b',
              font: { size: 13, weight: 500 },
              usePointStyle: true,
              boxWidth: 10
            }
          }
        },
        scales: {
          x: {
            stacked: true,
            max: 100,
            grid: { color: 'rgba(0,0,0,0.05)' },
            ticks: { color: '#7f1d1d', font: { size: 12 } }
          },
          y: {
            stacked: true,
            grid: { display: false },
            ticks: { color: '#991b1b', font: { size: 12, weight: 600 } }
          }
        }
      }
    });
  }
}

