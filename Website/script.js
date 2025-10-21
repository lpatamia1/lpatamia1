document.addEventListener('DOMContentLoaded', () => {
    
    // --- Draggable Windows & Minimize/Restore Logic ---
    const draggableWindows = document.querySelectorAll('.window.draggable');
    
    draggableWindows.forEach(win => {
        const titleBar = win.querySelector('.title-bar');
        const minimizeButton = win.querySelector('.btn-minimize');

        if (!titleBar) return;

        // --- NEW: Restore by clicking the title bar ---
        titleBar.addEventListener('click', () => {
            if (win.classList.contains('minimized')) {
                win.classList.remove('minimized');
            }
        });
        
        // --- NEW: Minimize button logic ---
        if (minimizeButton) {
            minimizeButton.addEventListener('click', (e) => {
                e.stopPropagation(); // Prevents the title bar click from firing
                win.classList.add('minimized');
            });
        }

        // --- UPDATED: Dragging logic ---
        let isDragging = false;
        let offsetX, offsetY;

        titleBar.addEventListener('mousedown', (e) => {
            // UPDATED: Do not drag if window is minimized or a button is clicked
            if (win.classList.contains('minimized') || e.target.classList.contains('button')) {
                isDragging = false;
                return;
            }
            win.style.position = 'absolute';

            isDragging = true;
            offsetX = e.clientX - win.offsetLeft;
            offsetY = e.clientY - win.offsetTop;
            win.style.zIndex = 20;
            draggableWindows.forEach(otherWin => { if (otherWin !== win) otherWin.style.zIndex = 10; });
        });

        document.addEventListener('mousemove', (e) => {
            if (isDragging) {
                win.style.left = `${e.clientX - offsetX}px`;
                win.style.top = `${e.clientY - offsetY}px`;
            }
        });

        document.addEventListener('mouseup', () => {
            isDragging = false;
        });
    });

    // --- Terminal Logic ---
    const terminalInput = document.getElementById('terminal-input');
    const terminalOutput = document.getElementById('terminal-output');
    const terminalWindow = document.getElementById('terminal');

    if(terminalWindow) {
        terminalWindow.addEventListener('click', () => { if (terminalInput) terminalInput.focus(); });
    }

    if(terminalInput) {
        terminalInput.addEventListener('keydown', function(event) {
            if (event.key === "Enter") {
                const command = terminalInput.value.trim();
                if (command) {
                    printToTerminal(`> ${command}`);
                    handleCommand(command);
                    terminalInput.value = "";
                }
            }
        });
        terminalInput.focus();
    }

    function handleCommand(command) {
        const cmd = command.toLowerCase().trim();
        switch(cmd) {
            case 'help':
                printToTerminal("Available commands: about, skills, projects, contact, clear");
                break;
            case 'about':
                printToTerminal("Graduated from Loyola University Chicago (CS). Passionate about building elegant solutions and analyzing data.");
                break;
            case 'skills':
                printToTerminal("Running Skills.exe... Languages, Frontend, Backend, and Data Science tools.");
                break;
            case 'projects':
                printToTerminal("Accessing Projects Folder... Movie Genre Classification, Pomodoro Timer, Games Portfolio, and more.");
                break;
            case 'contact':
                printToTerminal("Let's connect! LinkedIn: lilyanapatamia, GitHub: lpatamia1, Email: lpatamia@luc.edu");
                break;
            case 'clear':
                if (terminalOutput) terminalOutput.innerHTML = "";
                break;
            default:
                printToTerminal(`'${command}' is not a recognized command. Type 'help'.`);
        }
    }

    function printToTerminal(text) {
        if (!terminalOutput) return;
        const p = document.createElement('p');
        p.textContent = text;
        terminalOutput.appendChild(p);
        terminalOutput.scrollTop = terminalOutput.scrollHeight;
    }

    // --- Retro Player Logic ---
    const currentTrackDisplay = document.getElementById('current-track');
    const playlistItems = document.querySelectorAll('.playlist li');
    const controls = document.querySelector('.player-controls');

    function updatePlayer(selectedTrack) {
        if (!currentTrackDisplay || !selectedTrack) return;
        currentTrackDisplay.textContent = selectedTrack.textContent.replace('▶ ', '');
        playlistItems.forEach(item => {
            item.classList.remove('active');
            item.textContent = item.textContent.replace('▶ ', '');
        });
        selectedTrack.classList.add('active');
        selectedTrack.textContent = '▶ ' + selectedTrack.textContent;
    }

    playlistItems.forEach((item) => {
        item.addEventListener('click', () => { updatePlayer(item); });
    });

    if (controls) {
        const prevButton = controls.children[0];
        const nextButton = controls.children[3];
        nextButton.addEventListener('click', () => {
            let activeItem = document.querySelector('.playlist li.active');
            if (!activeItem) return;
            let nextItem = activeItem.nextElementSibling || playlistItems[0];
            updatePlayer(nextItem);
        });
        prevButton.addEventListener('click', () => {
            let activeItem = document.querySelector('.playlist li.active');
            if (!activeItem) return;
            let prevItem = activeItem.previousElementSibling || playlistItems[playlistItems.length - 1];
            updatePlayer(prevItem);
        });
    }

    if (playlistItems.length > 0) {
        updatePlayer(playlistItems[0]);
    }

    // --- Easter Egg Logic ---
    const dangerButton = document.getElementById('danger-button');
    const screensaver = document.getElementById('screensaver');
    const dvdLogo = document.getElementById('dvd-logo');
    if (dangerButton && screensaver && dvdLogo) {
        let x = 100, y = 100, dx = 2, dy = 2;
        const colors = ['#ff69b4', '#00ffff', '#ffff00', '#00ff00', '#ffa500'];
        let colorIndex = 0;
        let animationFrameId;
        function animate() {
            const rect = screensaver.getBoundingClientRect();
            const logoRect = dvdLogo.getBoundingClientRect();
            x += dx; y += dy;
            if (x + logoRect.width >= rect.width || x <= 0) { dx = -dx; changeColor(); }
            if (y + logoRect.height >= rect.height || y <= 0) { dy = -dy; changeColor(); }
            dvdLogo.style.left = x + 'px';
            dvdLogo.style.top = y + 'px';
            animationFrameId = requestAnimationFrame(animate);
        }
        function changeColor() {
            colorIndex = (colorIndex + 1) % colors.length;
            dvdLogo.querySelector('text').setAttribute('fill', colors[colorIndex]);
        }
        dangerButton.addEventListener('click', () => {
            screensaver.classList.remove('hidden');
            if (animationFrameId) cancelAnimationFrame(animationFrameId);
            animationFrameId = requestAnimationFrame(animate);
        });
    }
});