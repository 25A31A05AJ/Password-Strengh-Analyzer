function checkPassword() {

    let password = document.getElementById("password").value;
    let score = 0;
    let analysis = "";

    if (password.length === 0) {
        document.getElementById("result").innerHTML =
            "Please enter a password.";
        document.getElementById("analysis").innerHTML = "";
        document.getElementById("strength-fill").style.width = "0%";
        return;
    }

    let lowerPassword = password.toLowerCase();

    if (
        lowerPassword === "password" ||
        lowerPassword === "123456" ||
        lowerPassword === "qwerty" ||
        lowerPassword === "admin"
    ) {
        analysis += "Warning: This is a common password!<br><br>";
    }

    if (password.length >= 8) {
        score++;
        analysis += "✓ Length: Good<br>";
    } else {
        analysis += "✗ Length: Too Short<br>";
    }

    if (/[A-Z]/.test(password)) {
        score++;
        analysis += "✓ Uppercase: Good<br>";
    } else {
        analysis += "✗ Uppercase: Missing<br>";
    }

    if (/[a-z]/.test(password)) {
        score++;
        analysis += "✓ Lowercase: Good<br>";
    } else {
        analysis += "✗ Lowercase: Missing<br>";
    }

    if (/[0-9]/.test(password)) {
        score++;
        analysis += "✓ Number: Good<br>";
    } else {
        analysis += "✗ Number: Missing<br>";
    }

    if (/[^a-zA-Z0-9]/.test(password)) {
        score++;
        analysis += "✓ Special Character: Good<br>";
    } else {
        analysis += "✗ Special Character: Missing<br>";
    }

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

    document.getElementById("strength-fill").style.width = barWidth;
    document.getElementById("strength-percent").innerText = barWidth;
    document.getElementById("strength-fill").style.background =
        colorClass === "weak" ? "red" :
        colorClass === "medium" ? "orange" : "green";

    document.getElementById("result").innerHTML =
        'Password Strength: <span class="' + colorClass + '">' +
        strength + '</span><br>' +
        "Score: " + score + "/5";

    document.getElementById("analysis").innerHTML =
        "<br>Password Analysis:<br><br>" + analysis;
}
