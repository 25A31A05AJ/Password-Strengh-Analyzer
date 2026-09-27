function checkPassword() {

    let password = document.getElementById("password").value;
    let score = 0;
    let analysis = "";

    if (password.length === 0) {
        document.getElementById("result").innerHTML =
            "Please enter a password.";
        document.getElementById("analysis").innerHTML = "";
        document.getElementById("strength-fill").style.width = "0%";
        document.getElementById("strength-percent").innerText = "0%";
        document.getElementById("breach-result").innerHTML = "";
        return;
    }

    let lowerPassword = password.toLowerCase();

    // Common Password Detection
    if (
        lowerPassword === "password" ||
        lowerPassword === "123456" ||
        lowerPassword === "qwerty" ||
        lowerPassword === "admin"
    ) {
        analysis += "⚠️ Warning: This is a common password!<br><br>";
    }

    // Sequential Pattern Detection
    let hasSequence = false;

    for (let i = 0; i < password.length - 2; i++) {
        let a = password.charCodeAt(i);
        let b = password.charCodeAt(i + 1);
        let c = password.charCodeAt(i + 2);

        if (
            (b === a + 1 && c === b + 1) ||
            (b === a - 1 && c === b - 1)
        ) {
            hasSequence = true;
            break;
        }
    }

    if (hasSequence) {
        analysis += "⚠️ Sequential Pattern: Detected<br>";
    } else {
        analysis += "✓ Sequential Pattern: Not Detected<br>";
    }

    analysis += "<br>";

    // Length
    if (password.length >= 8) {
        score++;
        analysis += "✓ Length: Good<br>";
    } else {
        analysis += "✗ Length: Too Short<br>";
    }

    // Uppercase
    if (/[A-Z]/.test(password)) {
        score++;
        analysis += "✓ Uppercase: Good<br>";
    } else {
        analysis += "✗ Uppercase: Missing<br>";
    }

    // Lowercase
    if (/[a-z]/.test(password)) {
        score++;
        analysis += "✓ Lowercase: Good<br>";
    } else {
        analysis += "✗ Lowercase: Missing<br>";
    }

    // Number
    if (/[0-9]/.test(password)) {
        score++;
        analysis += "✓ Number: Good<br>";
    } else {
        analysis += "✗ Number: Missing<br>";
    }

    // Special Character
    if (/[^a-zA-Z0-9]/.test(password)) {
        score++;
        analysis += "✓ Special Character: Good<br>";
    } else {
        analysis += "✗ Special Character: Missing<br>";
    }

    // Strength
    let strength;
    let colorClass;
    let barWidth;

    if (score <= 2) {
        strength = "WEAK";
        colorClass = "weak";
        barWidth = "40%";
    } else if (score <= 4) {
        strength = "MEDIUM";
        colorClass = "medium";
        barWidth = "70%";
    } else {
        strength = "STRONG";
        colorClass = "strong";
        barWidth = "100%";
    }

    // Strength Bar
    document.getElementById("strength-fill").style.width = barWidth;

    document.getElementById("strength-percent").innerText =
        barWidth;

    document.getElementById("strength-fill").style.background =
        colorClass === "weak" ? "red" :
        colorClass === "medium" ? "orange" :
        "green";

    // Result
    document.getElementById("result").innerHTML =
        'Password Strength: <span class="' + colorClass + '">' +
        strength +
        '</span><br>' +
        "Score: " + score + "/5";

    // Analysis
    document.getElementById("analysis").innerHTML =
        "<br>Password Analysis:<br><br>" + analysis;

    // Breach Detection
    checkBreach(password);
}


// Show / Hide Password
function togglePassword() {

    const password = document.getElementById("password");

    if (password.type === "password") {
        password.type = "text";
    } else {
        password.type = "password";
    }
}


// Dark / Light Mode
function toggleTheme() {

    document.body.classList.toggle("dark-mode");

    const button = document.querySelector(".theme-button");

    if (document.body.classList.contains("dark-mode")) {
        button.innerHTML = "☀️ Light Mode";
    } else {
        button.innerHTML = "🌙 Dark Mode";
    }
}


// Breach Detection
async function checkBreach(password) {

    const breachResult = document.getElementById("breach-result");

    breachResult.innerHTML =
        "🔍 Checking known data breaches...";

    try {

        const data = new TextEncoder().encode(password);

        const hashBuffer = await crypto.subtle.digest(
            "SHA-1",
            data
        );

        const hashArray = Array.from(
            new Uint8Array(hashBuffer)
        );

        const hash = hashArray
            .map(byte => byte.toString(16).padStart(2, "0"))
            .join("")
            .toUpperCase();

        const prefix = hash.substring(0, 5);
        const suffix = hash.substring(5);

        const response = await fetch(
            "https://api.pwnedpasswords.com/range/" + prefix
        );

        if (!response.ok) {
            throw new Error("API request failed");
        }

        const result = await response.text();

        const lines = result.split("\n");

        let found = false;
        let count = 0;

        for (let line of lines) {

            const parts = line.trim().split(":");

            if (parts[0] === suffix) {
                found = true;
                count = parts[1];
                break;
            }
        }

        if (found) {

            breachResult.innerHTML =
                "⚠️ Breach Detection: FOUND<br>" +
                "This password has appeared in known data breaches.<br>" +
                "Times seen: " + count;

        } else {

            breachResult.innerHTML =
                "✅ Breach Detection: NOT FOUND<br>" +
                "No known breach record was found for this password.";

        }

    } catch (error) {

        breachResult.innerHTML =
            "⚠️ Breach Detection: Unable to check right now.";

    }
}
