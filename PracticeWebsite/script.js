async function loadCSV() {
  try {
    const response = await fetch('applications.csv');
    const csvText = await response.text();

    // Detect delimiter: if the first line contains ';', use that
    const firstLine = csvText.split('\n')[0];
    const delimiter = firstLine.includes(';') ? ';' : ',';

    const lines = csvText.trim().split('\n');
    const headers = lines[0].split(delimiter);

    const data = lines.slice(1).map(line => {
      const values = [];
      let current = '';
      let inQuotes = false;

      for (let i = 0; i < line.length; i++) {
        const char = line[i];
        if (char === '"' && line[i + 1] !== '"') {
          inQuotes = !inQuotes;
        } else if (char === delimiter && !inQuotes) {
          values.push(current);
          current = '';
        } else {
          current += char;
        }
      }

      values.push(current);
      const obj = {};
      headers.forEach((h, idx) => {
        obj[h.trim()] = values[idx]?.replace(/^"|"$/g, '').trim();
      });
      return obj;
    });

    console.log('✅ Parsed CSV rows:', data.length);
    return data;
  } catch (err) {
    console.error('Error loading CSV:', err);
    return [];
  }
}

// 🪄 Convert Markdown-style [Text](URL) to clickable link
function convertMarkdownLinks(text) {
  if (!text) return '';
  return text.replace(
    /\[([^\]]+)\]\(([^)]+)\)/g,
    (match, label, url) => {
      const safeUrl = url === '#' ? '#' : url.trim();
      const color = safeUrl === '#' ? '#777' : '#4e6a57';
      const pointer = safeUrl === '#' ? 'default' : 'pointer';
      return `<a href="${safeUrl}" target="_blank" style="color:${color}; font-weight:600; text-decoration:none; cursor:${pointer};">${label}${safeUrl !== '#' ? ' ↗' : ''}</a>`;
    }
  );
}

// 🎯 Add icons or emojis depending on job status
function formatStatus(status) {
  if (!status) return '';
  const s = status.toLowerCase();
  if (s.includes('applied')) return '✓ Applied';
  if (s.includes('interview')) return '➤ Interview';
  if (s.includes('hire')) return '★ Hired';
  if (s.includes('reject')) return '✗ Rejected';
  if (s.includes('closed')) return '⤫ Closed';
  if (s.includes('other')) return '• Other';

  return status;
}

let allJobs = [];

async function init() {
  const csvJobs = await loadCSV();
  allJobs = csvJobs;
  renderTable(csvJobs);
  updateCount(csvJobs.length, allJobs.length);

  // 🔎 Attach live search listener
  document.getElementById('searchInput').addEventListener('input', e => {
    const query = e.target.value.toLowerCase();
    const filtered = allJobs.filter(job =>
      Object.values(job).some(val =>
        val && val.toLowerCase().includes(query)
      )
    );

    renderTable(filtered);
    updateCount(filtered.length, allJobs.length);
  });
}

function renderTable(data) {
  const tableBody = document.getElementById('tableBody');
  tableBody.innerHTML = '';

  if (!data.length) {
    console.warn("⚠️ No data found in CSV");
    tableBody.innerHTML = `<tr><td colspan="4">⚠️ No data found in CSV</td></tr>`;
    return;
  }

  data.forEach(job => {
    const row = document.createElement('tr');
    const companyHTML = convertMarkdownLinks(job.Company || '');
    const role   = job.Role || '';
    const loc    = job.Location || '';
    const status = formatStatus(job.Status || '');
    const notes  = job.Notes && job.Notes.trim() ? job.Notes : '—';

    row.innerHTML = `
        <td>${companyHTML}</td>
        <td>${job.Role || ''}</td>
        <td>${job.Location || ''}</td>
        <td>${status || ''}</td>
        <td>${job.Notes || ''}</td>
    `;
    tableBody.appendChild(row);
  });
}

function updateCount(filtered, total) {
  const countEl = document.getElementById('resultCount');
  if (!countEl) return;

  if (filtered === total) {
    countEl.textContent = `Showing all ${total} applications.`;
  } else if (filtered === 0) {
    countEl.textContent = `No matches found.`;
  } else {
    countEl.textContent = `Showing ${filtered} of ${total} applications.`;
  }
}

init();
