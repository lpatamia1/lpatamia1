function makeChart(ctx, labels, data, label, color) {
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{ label, data, backgroundColor: color }]
    },
    options: { responsive: true, plugins: { legend: { display: false } } }
  });
}

makeChart(
  document.getElementById('statusChart'),
  Object.keys(statusData),
  Object.values(statusData),
  'Applications by Status',
  '#c084fc'
);

makeChart(
  document.getElementById('sourceChart'),
  Object.keys(sourceData),
  Object.values(sourceData),
  'Top Sources',
  '#22c55e'
);

new Chart(document.getElementById('timelineChart'), {
  type: 'line',
  data: {
    labels: Object.keys(weeklyData),
    datasets: [{
      label: 'Applications per Week',
      data: Object.values(weeklyData),
      fill: true,
      borderColor: '#ec4899',
      backgroundColor: 'rgba(236, 72, 153, 0.3)'
    }]
  },
  options: { responsive: true }
});
