document.addEventListener('DOMContentLoaded', () => {
    
    // --- Terminal Logic ---
    const terminalInput = document.getElementById('terminal-input');
    const terminalOutput = document.getElementById('terminal-output');
    const terminalWindow = document.getElementById('terminal');

    if(terminalWindow) {
        terminalWindow.addEventListener('click', () => {
            if (terminalInput) terminalInput.focus();
        });
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
        
        // Initial focus on the input
        terminalInput.focus();
    }

    function handleCommand(command) {
        const cmd = command.toLowerCase().trim();
        switch(cmd) {
            case 'help':
                printToTerminal("Available commands:");
                printToTerminal("  'about'    - Who am I?");
                printToTerminal("  'skills'   - What's in my tech stack?");
                printToTerminal("  'projects' - Show me the projects.");
                printToTerminal("  'contact'  - How to get in touch.");
                printToTerminal("  'clear'    - Clear the terminal screen.");
                break;
            case 'about':
                printToTerminal("Graduated from Loyola University Chicago with a degree in Computer Science. Passionate about building elegant solutions and analyzing data.");
                break;
            case 'skills':
                printToTerminal("Running Skills.exe...");
                printToTerminal("Languages: Python, Java, JavaScript, C++, SQL, R...");
                printToTerminal("Frontend: HTML, CSS, React...");
                printToTerminal("Backend: Flask, Node.js, MySQL...");
                break;
            case 'projects':
                printToTerminal("Accessing Projects Folder...");
                printToTerminal("  - Movie Genre Classification (ML)");
                printToTerminal("  - Pomodoro Focus Timer (Web App)");
                printToTerminal("  - Java & Web Games Portfolio"); // << THE TYPO WAS HERE
                printToTerminal("  - GIS Food Desert Analysis");
                break;
            case 'contact':
                printToTerminal("Let's connect!");
                printToTerminal("  LinkedIn: linkedin.com/in/lilyanapatamia");
                printToTerminal("  GitHub:   github.com/lpatamia1");
                printToTerminal("  Email:    lpatamia@luc.edu");
                break;
            case 'clear':
                if (terminalOutput) terminalOutput.innerHTML = "";
                break;
            default:
                printToTerminal(`'${command}' is not recognized as a command. Type 'help'.`);
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
        item.addEventListener('click', () => {
            updatePlayer(item);
        });
    });

    if (controls) {
        const prevButton = controls.children[0];
        const nextButton = controls.children[3];

        nextButton.addEventListener('click', () => {
            let activeItem = document.querySelector('.playlist li.active');
            if (!activeItem) return;
            let nextItem = activeItem.nextElementSibling;
            if (!nextItem) {
                nextItem = playlistItems[0];
            }
            updatePlayer(nextItem);
        });

        prevButton.addEventListener('click', () => {
            let activeItem = document.querySelector('.playlist li.active');
            if (!activeItem) return;
            let prevItem = activeItem.previousElementSibling;
            if (!prevItem) {
                prevItem = playlistItems[playlistItems.length - 1];
            }
            updatePlayer(prevItem);
        });
    }

    if (playlistItems.length > 0) {
        updatePlayer(playlistItems[0]);
    }

});