// static/jobs.js
console.log("✅ jobs.js loaded");

const jobList = document.getElementById("jobList");
const searchBox = document.getElementById("jobSearch");

// 🧾 Render all jobs
function renderJobs(list) {
  jobList.innerHTML = "";

  if (!list || list.length === 0) {
    jobList.innerHTML = "<p style='text-align:center; color:#888;'>No matches found.</p>";
    return;
  }

  list.forEach(job => {
  const card = document.createElement("div");
  card.classList.add("job-card");

  // The Company column already has clickable <a> links (from Flask)
  card.innerHTML = `
    <h3 style="margin:0 0 0.3rem; color:#4f4848;">${job.Company}</h3>
    <p><strong>Role:</strong> ${job.Role}</p>
    <p><strong>Status:</strong> ${job.Status}</p>
    <p><strong>Location:</strong> ${job.Location || "—"}</p>
    <p><strong>Source:</strong> ${job.Source}</p>
    <p><strong>Date Applied:</strong> ${job["Date Applied"] || "—"}</p>

    ${
      job["Job Type"] || job.Keywords
        ? `
          <div class="badge-row" style="margin:0.4rem 0 0.6rem;">
            ${
              job["Job Type"]
                ? `<span style="
                    background:#e0f2f1;
                    color:#00695c;
                    padding:0.25rem 0.6rem;
                    border-radius:8px;
                    font-size:0.8rem;
                    margin-right:0.3rem;
                    font-weight:500;">
                    💼 ${job["Job Type"]}
                  </span>`
                : ""
            }
            ${
              job.Keywords
                ? job.Keywords.split(/[,;]+/)
                    .map(k => `<span style="
                      background:#ede9fe;
                      color:#5b21b6;
                      padding:0.25rem 0.6rem;
                      border-radius:8px;
                      font-size:0.8rem;
                      margin-right:0.3rem;
                      font-weight:500;">🏷️ ${k.trim()}</span>`)
                    .join(" ")
                : ""
            }
          </div>
        `
        : ""
    }

    ${
      job.Notes
        ? `<p style="font-size:0.9rem; color:#777;">📝 ${job.Notes}</p>`
        : ""
    }
  `;

  jobList.appendChild(card);
});

}

// 🔍 Search bar
searchBox.addEventListener("input", () => {
  const term = searchBox.value.toLowerCase();
  const filtered = jobs.filter(j =>
    (j.Company && j.Company.toLowerCase().includes(term)) ||
    (j.Role && j.Role.toLowerCase().includes(term)) ||
    (j.Status && j.Status.toLowerCase().includes(term))
  );
  renderJobs(filtered);
});

// 🚀 Initial load
renderJobs(jobs);
