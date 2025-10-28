console.log("✅ jobs.js synced");

// 🧾 Convert markdown links to text + URL
function extractUrl(markdown) {
  const match = markdown.match(/\((https?:\/\/.*?)\)/);
  return match ? match[1] : "#";
}
function extractText(markdown) {
  const match = markdown.match(/\[(.*?)\]/);
  return match ? match[1] : markdown;
}

const jobList = document.getElementById("jobList");
const searchBox = document.getElementById("jobSearch");

function renderJobs(list = jobs) {
  jobList.innerHTML = "";

  list.forEach(job => {
    const companyText = extractText(job.Company);
    const companyUrl = extractUrl(job.Company);

    const card = document.createElement("div");
    card.classList.add("job-card");

    // ✅ Set all data needed for modal
    card.dataset.company = companyText;
    card.dataset.role = job.Role || "";
    card.dataset.status = job.Status || "";
    card.dataset.type = job.Type || "";
    card.dataset.location = job.Location || "";
    card.dataset.date = job["Date Applied"] || "";
    card.dataset.source = job.Source || "";
    card.dataset.notes = job.Notes || "";

    card.innerHTML = `
      <h3><a class="company-link" href="${companyUrl}" target="_blank">${companyText}</a></h3>
      <p><strong>Role:</strong> ${job.Role}</p>
      <p><strong>Status:</strong> ${job.Status}</p>
      <p><strong>Location:</strong> ${job.Location}</p>
      <p><strong>Date:</strong> ${job["Date Applied"]}</p>
    `;

    jobList.appendChild(card);
  });

  attachModalListeners(); // ✅ rebind clicks to new cards
}

// 🔍 Search listener
searchBox.addEventListener("input", () => {
  const input = searchBox.value.toLowerCase();
  const filtered = jobs.filter(j =>
    (j.Company && j.Company.toLowerCase().includes(input)) ||
    (j.Role && j.Role.toLowerCase().includes(input)) ||
    (j.Status && j.Status.toLowerCase().includes(input))
  );
  renderJobs(filtered);
});

// 🐱 Modal trigger for cards
function attachModalListeners() {
  document.querySelectorAll(".job-card").forEach(card => {
    card.addEventListener("click", () => {
      modalCompany.innerText = card.dataset.company;
      modalRole.innerText = card.dataset.role;
      modalStatus.innerText = card.dataset.status;
      modalType.innerText = card.dataset.type;
      modalLocation.innerText = card.dataset.location;
      modalDate.innerText = card.dataset.date;
      modalSource.innerText = card.dataset.source;
      modalNotes.innerText = card.dataset.notes;

      const catMessages = [
        "ฅ^•ﻌ•^ฅ You’re one application closer!",
        "(=＾ᆺ＾=) Keep going!",
        "૮ ˃⤙˂ ა Proud of you.",
        "=^._.^= You make progress look cute.",
        "(*ฅ́˘ฅ̀*)♡ You totally got this."
      ];
      catHelper.innerText = catMessages[Math.floor(Math.random() * catMessages.length)];

      jobModal.style.display = "flex";
    });
  });
}

// 🚀 Initial load
renderJobs();
