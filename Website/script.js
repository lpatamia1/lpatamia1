document.addEventListener('DOMContentLoaded', () => {
    const terminalInput = document.getElementById('terminal-input');
    const terminalOutput = document.getElementById('terminal-output');

    // Focus on the input when the terminal window is clicked
    const terminalWindow = document.getElementById('terminal');
    if(terminalWindow) {
        terminalWindow.addEventListener('click', () => {
            terminalInput.focus();
        });
    }

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
                printToTerminal("  - Java & Web Games Portfolio"); // CORRECTED THIS LINE
                printToTerminal("  - GIS Food Desert Analysis");
                break;
            case 'contact':
                printToTerminal("Let's connect!");
                printToTerminal("  LinkedIn: linkedin.com/in/lilyanapatamia");
                printToTerminal("  GitHub:   github.com/lpatamia1");
                printToTerminal("  Email:    lpatamia@luc.edu");
                break;
            case 'clear':
                terminalOutput.innerHTML = "";
                break;
            default:
                printToTerminal(`'${command}' is not recognized as a command. Type 'help'.`);
        }
    }

    function printToTerminal(text) {
        const p = document.createElement('p');
        p.textContent = text;
        terminalOutput.appendChild(p);
        terminalOutput.scrollTop = terminalOutput.scrollHeight;
    }
    
    // Initial focus on the input
    terminalInput.focus();
});