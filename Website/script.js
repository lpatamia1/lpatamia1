document.addEventListener('DOMContentLoaded', () => {

  // --- Draggable Windows ---
  const draggableWindows = document.querySelectorAll('.window.draggable');
  draggableWindows.forEach(win => {
    const titleBar = win.querySelector('.title-bar');
    const minimizeButton = win.querySelector('.btn-minimize');
    if (!titleBar) return;

    titleBar.addEventListener('click', () => {
      if (win.classList.contains('minimized')) win.classList.remove('minimized');
    });

    if (minimizeButton) {
      minimizeButton.addEventListener('click', (e) => {
        e.stopPropagation();
        win.classList.add('minimized');
      });
    }

    let isDragging = false, offsetX, offsetY;
    titleBar.addEventListener('mousedown', (e) => {
      if (win.classList.contains('minimized') || e.target.classList.contains('button')) return;
      isDragging = true;
      offsetX = e.clientX - win.offsetLeft;
      offsetY = e.clientY - win.offsetTop;
      win.style.zIndex = 20;
      draggableWindows.forEach(o => { if (o !== win) o.style.zIndex = 10; });
    });
    document.addEventListener('mousemove', (e) => {
      if (isDragging) {
        win.style.left = `${e.clientX - offsetX}px`;
        win.style.top = `${e.clientY - offsetY}px`;
      }
    });
    document.addEventListener('mouseup', () => isDragging = false);
  });

  // --- Terminal Logic ---
  const terminalInput = document.getElementById('terminal-input');
  const terminalOutput = document.getElementById('terminal-output');
  const printToTerminal = (text) => {
    const p = document.createElement('p');
    p.textContent = text;
    terminalOutput.appendChild(p);
    terminalOutput.scrollTop = terminalOutput.scrollHeight;
  };

  if (terminalInput) {
    terminalInput.addEventListener('keydown', e => {
      if (e.key === "Enter") {
        const command = terminalInput.value.trim().toLowerCase();
        terminalInput.value = "";
        const responses = {
          help: "Commands: about, skills, projects, contact, clear",
          about: "Loyola CS Grad. Loves matcha, clean code, and chaos.exe 💻",
          skills: "Running Skills.exe... Python, JS, React, Flask, SQL, Tailwind...",
          projects: "Loading Projects Folder... 🎬 Pomodoro Timer, Dashboards, APIs",
          contact: "LinkedIn: lilyanapatamia | GitHub: lpatamia1 | Email: lpatamia@luc.edu"
        };
        if (command === "clear") terminalOutput.innerHTML = "";
        else printToTerminal(responses[command] || `'${command}' not found. Try 'help'.`);
      }
    });
  }

  // --- Player ---
  const currentTrackDisplay = document.getElementById('current-track');
  const playlistItems = document.querySelectorAll('.playlist li');
  function updatePlayer(t) {
    if (!t) return;
    currentTrackDisplay.textContent = t.textContent.replace('▶ ', '');
    playlistItems.forEach(i => i.classList.remove('active'));
    t.classList.add('active');
    if (!t.textContent.startsWith('▶ ')) t.textContent = '▶ ' + t.textContent;
  }
  playlistItems.forEach(i => i.addEventListener('click', () => updatePlayer(i)));
  if (playlistItems.length) updatePlayer(playlistItems[0]);

  // --- Funny Warning.exe Chaos ---
  const dangerButton = document.getElementById('danger-button');
  const screensaver = document.getElementById('screensaver');
  const dvdLogo = document.getElementById('dvd-logo');
  const terminal = document.getElementById('terminal');

  if (dangerButton && screensaver && dvdLogo) {
    dangerButton.addEventListener('click', () => {
      // 🌸 Flash and shake
      document.body.style.transition = "background-color 0.1s";
      document.body.style.backgroundColor = "#ff91cf";
      document.body.classList.add('shake');
      setTimeout(() => document.body.classList.remove('shake'), 1000);

      // 💀 Chaotic Popups
      const emojis = ['💀','🔥','🐛','⚠️','💾','🌀','🍵','💻','😵‍💫'];
      for (let i = 0; i < 8; i++) {
        const popup = document.createElement("div");
        popup.className = "window popup";
        popup.style.left = Math.random() * window.innerWidth * 0.8 + "px";
        popup.style.top = Math.random() * window.innerHeight * 0.7 + "px";
        popup.style.animation = "bouncePopup 0.4s ease alternate infinite";
        popup.innerHTML = `
          <div class="title-bar"><div class="title">Error_${i + 1}.exe</div></div>
          <div class="content" style="text-align:center;">
            <p>${emojis[Math.floor(Math.random()*emojis.length)]} SYSTEM PANIC ${emojis[Math.floor(Math.random()*emojis.length)]}</p>
          </div>
        `;
        document.body.appendChild(popup);
        setTimeout(() => popup.remove(), 4500);
      }

      // 💿 Hyperactive DVD rave mode
      setTimeout(() => {
        screensaver.classList.remove("hidden");
        let x = 100, y = 100, dx = 4, dy = 4;
        const colors = ['#ff69b4','#00ffff','#ffff00','#00ff00','#ffa500','#ff00ff','#ff4444'];
        let colorIndex = 0, scale = 1, direction = 1, rotation = 0;

        function moveLogo() {
          const rect = dvdLogo.getBoundingClientRect();
          if (x + rect.width >= window.innerWidth || x <= 0) { dx = -dx; changeColor(); }
          if (y + rect.height >= window.innerHeight || y <= 0) { dy = -dy; changeColor(); }
          x += dx; y += dy;
          scale += 0.01 * direction;
          if (scale > 1.3 || scale < 0.9) direction *= -1;
          rotation += 3;
          dvdLogo.style.left = x + "px";
          dvdLogo.style.top = y + "px";
          dvdLogo.style.transform = `scale(${scale}) rotate(${rotation}deg)`;
          requestAnimationFrame(moveLogo);
        }
        function changeColor() {
          colorIndex = (colorIndex + 1) % colors.length;
          dvdLogo.querySelector('text').setAttribute('fill', colors[colorIndex]);
        }
        moveLogo();
      }, 1000);

      // 🧠 Fake System Reboot
      setTimeout(() => {
        screensaver.classList.add("hidden");
        document.body.style.backgroundColor = "#0c0c0c";
        terminal.scrollIntoView({ behavior: "smooth" });
        terminalOutput.innerHTML = "";
        const lines = [
          "💀 System Error Detected...",
          "⚙️ Rebooting PinkOS v3.1...",
          "💾 Loading Matcha Protocols... ████████ 88%",
          "🎵 Booting Lo-Fi Kernel...",
          "🌸 Desktop Recovered Successfully!"
        ];
        lines.forEach((line, i) => setTimeout(() => printToTerminal(line), i * 1000));
        setTimeout(() => {
        document.body.style.backgroundColor = "#fae6f3";
        const dangerWindow = document.getElementById('danger-window');
        if (dangerWindow) {
        dangerWindow.classList.add('poof');
        setTimeout(() => dangerWindow.remove(), 800); // matches animation duration
        }
        }, 6000);
      }, 20000);
    });
  }
});
