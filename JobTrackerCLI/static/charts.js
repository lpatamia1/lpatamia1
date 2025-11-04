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

// 🟣 STATUS CHART
{
  const ctx = document.getElementById('statusChart').getContext('2d');
  // Manually rename "other" → "APPLIED" for the x-axis
  const labels = Object.keys(statusData).map(l =>
    l.toLowerCase() === "other" ? "APPLIED" : l
  );
  const data = Object.values(statusData);

  const gradientStatus = ctx.createLinearGradient(0, 0, 0, ctx.canvas.height);
  gradientStatus.addColorStop(0, '#60a5fa'); // solid
  gradientStatus.addColorStop(1, '#a5c9f6'); // solid

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels,
      datasets: [{
        data,
        backgroundColor: gradientStatus,
        borderRadius: 10
      }]
    },
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
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: function(ctx) {
              const total = ctx.dataset.data.reduce((a, b) => a + b, 0);
              const value = ctx.raw;
              const percent = ((value / total) * 100).toFixed(1);
              return `${ctx.label}: ${value} (${percent}%)`;
            }
          }
        }
      },
      scales: {
        x: { ticks: { color: '#777' }, grid: { display: false } },
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

  const gradientSources = ctx.createLinearGradient(0, 0, 0, ctx.canvas.height);
  gradientSources.addColorStop(0, '#fca684ff');  // vibrant lilac
  gradientSources.addColorStop(1, '#ffe2d5ff');  // soft lavender haze

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels,
      datasets: [{
        data,
        backgroundColor: gradientSources,
        borderRadius: 10
      }]
    },
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
        legend: { display: false }
      },
      scales: {
        x: { ticks: { color: '#777' }, grid: { display: false } },
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

// 🧠 Technical Prep Progress 
{
  const prepCanvas = document.getElementById('prepBar');
  if (prepCanvas) {
    const ctx = prepCanvas.getContext('2d');
    const g = ctx.createLinearGradient(0, 0, 0, ctx.canvas.height);
    g.addColorStop(0, 'rgba(59, 130, 246, 0.9)');  // sky blue top (#3b82f6)
    g.addColorStop(0, '#3b82f6'); // solid sky blue
    g.addColorStop(1, '#93c5fd'); // solid periwinkle
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

// ⭐ Skill Match Radar Chart — smooth fill + glow + curved animation
{
  const radarCanvas = document.getElementById('skillRadar');
  if (radarCanvas) {
    const ctx = radarCanvas.getContext('2d');

    const gradientFill = ctx.createLinearGradient(0, 0, 0, ctx.canvas.height);
    gradientFill.addColorStop(0, "rgba(255,105,180,0.3)");
    gradientFill.addColorStop(1, "rgba(255,105,180,0.05)");

    new Chart(ctx, {
      type: 'radar',
      data: {
        labels: Object.keys(skillMatch),
        datasets: [{
          label: 'Skill Proficiency',
          data: Object.values(skillMatch),
          fill: true,
          backgroundColor: gradientFill,
          borderColor: '#ff2f72',
          pointBackgroundColor: '#ff2f72',
          pointBorderColor: "#ffffff",
          borderWidth: 3,
          pointRadius: 5,
          tension: 0.32 // ✅ smooth curves
        }]
      },
      options: {
        responsive: true,
        animation: {
          duration: 2000,
          easing: 'easeOutQuart'
        },
        plugins: {
          legend: {
            display: true,
            position: "top",
            labels: {
              color: "#4b4343",
              font: { size: 14, weight: 600 }
            }
          },
          tooltip: {
            callbacks: {
              label: ctx => `${ctx.label}: ${ctx.raw}/10`
            }
          }
        },
        scales: {
          r: {
            suggestedMin: 0,
            suggestedMax: 10,
            grid: {
              color: "rgba(0,0,0,0.06)"
            },
            angleLines: {
              color: "rgba(0,0,0,0.08)"
            },
            ticks: {
              showLabelBackdrop: false,
              color: "#777",
              font: { size: 12 }
            },
            pointLabels: {
              color: "#2f2d2d",
              font: { size: 15, weight: 600 },
              padding: 10
            }
          }
        }
      }
    });
  }
}
