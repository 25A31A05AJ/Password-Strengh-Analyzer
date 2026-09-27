import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;

public class PasswordStrengthAnalyzer {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("     PASSWORD STRENGTH ANALYZER");
        System.out.println("=================================");

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        int score = 0;

        System.out.println();
        System.out.println("Password Analysis:");
        System.out.println("---------------------------------");

        // Common Password Detection
        String lowerPassword = password.toLowerCase();

        if (lowerPassword.equals("password") ||
            lowerPassword.equals("123456") ||
            lowerPassword.equals("qwerty") ||
            lowerPassword.equals("admin")) {

            System.out.println("⚠ Warning: This is a common password!");
        } else {
            System.out.println("✓ Common Password: Not Detected");
        }

        // Length
        if (password.length() >= 8) {
            score++;
            System.out.println("✓ Length: Good");
        } else {
            System.out.println("✗ Length: Too Short");
        }

        // Uppercase
        if (password.matches(".*[A-Z].*")) {
            score++;
            System.out.println("✓ Uppercase: Good");
        } else {
            System.out.println("✗ Uppercase: Missing");
        }

        // Lowercase
        if (password.matches(".*[a-z].*")) {
            score++;
            System.out.println("✓ Lowercase: Good");
        } else {
            System.out.println("✗ Lowercase: Missing");
        }

        // Number
        if (password.matches(".*[0-9].*")) {
            score++;
            System.out.println("✓ Number: Good");
        } else {
            System.out.println("✗ Number: Missing");
        }

        // Special Character
        if (password.matches(".*[^a-zA-Z0-9].*")) {
            score++;
            System.out.println("✓ Special Character: Good");
        } else {
            System.out.println("✗ Special Character: Missing");
        }

        // Sequential Pattern Detection
        boolean hasSequence = false;

        for (int i = 0; i < password.length() - 2; i++) {

            char a = password.charAt(i);
            char b = password.charAt(i + 1);
            char c = password.charAt(i + 2);

            if ((b == a + 1 && c == b + 1) ||
                (b == a - 1 && c == b - 1)) {

                hasSequence = true;
                break;
            }
        }

        if (hasSequence) {
            System.out.println("⚠ Sequential Pattern: Detected");
        } else {
            System.out.println("✓ Sequential Pattern: Not Detected");
        }

        // Score
        System.out.println("---------------------------------");
        System.out.println("Score: " + score + "/5");

        // Strength
        if (score <= 2) {
            System.out.println("Password Strength: WEAK");
        } else if (score <= 4) {
            System.out.println("Password Strength: MEDIUM");
        } else {
            System.out.println("Password Strength: STRONG");
        }

        // Percentage
        int percentage = score * 20;
        System.out.println("Strength Percentage: " + percentage + "%");

        // Breach Detection
        System.out.println();
        System.out.println("Breach Detection:");
        System.out.println("Checking known data breaches...");

        checkBreach(password);

        // Security Tips
        System.out.println();
        System.out.println("=================================");
        System.out.println("       PASSWORD SECURITY TIPS");
        System.out.println("=================================");

        System.out.println("✓ Use at least 8 characters");
        System.out.println("✓ Use uppercase and lowercase letters");
        System.out.println("✓ Add numbers");
        System.out.println("✓ Use special characters");
        System.out.println("✓ Avoid common passwords");
        System.out.println("✓ Avoid simple sequential patterns");

        System.out.println();
        System.out.println("Privacy Note:");
        System.out.println("The actual password is not directly");
        System.out.println("sent to the breach-checking service.");

        System.out.println();
        System.out.println("=================================");

        sc.close();
    }


    // Breach Detection
    public static void checkBreach(String password) {

        try {

            // Create SHA-1 hash
            MessageDigest sha1 =
                    MessageDigest.getInstance("SHA-1");

            byte[] hashBytes =
                    sha1.digest(
                            password.getBytes(StandardCharsets.UTF_8)
                    );

            // Convert hash to hexadecimal
            StringBuilder hashBuilder =
                    new StringBuilder();

            for (byte b : hashBytes) {

                hashBuilder.append(
                        String.format("%02X", b)
                );
            }

            String hash = hashBuilder.toString();

            // Split hash
            String prefix = hash.substring(0, 5);
            String suffix = hash.substring(5);

            // HIBP Pwned Passwords API
            URL url = new URL(
                    "https://api.pwnedpasswords.com/range/"
                            + prefix
            );

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty(
                    "User-Agent",
                    "PasswordStrengthAnalyzer"
            );

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()
                            )
                    );

            StringBuilder response =
                    new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }

            reader.close();

            // Search for matching hash
            String[] lines =
                    response.toString().split("\n");

            boolean found = false;
            String count = "0";

            for (String currentLine : lines) {

                String[] parts =
                        currentLine.trim().split(":");

                if (parts.length == 2 &&
                    parts[0].equalsIgnoreCase(suffix)) {

                    found = true;
                    count = parts[1];
                    break;
                }
            }

            if (found) {

                System.out.println(
                        "⚠ Breach Detection: FOUND"
                );

                System.out.println(
                        "This password has appeared in known data breaches."
                );

                System.out.println(
                        "Times seen: " + count
                );

            } else {

                System.out.println(
                        "✓ Breach Detection: NOT FOUND"
                );

                System.out.println(
                        "No known breach record was found."
                );
            }

            connection.disconnect();

        } catch (Exception e) {

            System.out.println(
                    "⚠ Breach Detection: Unable to check right now."
            );
        }
    }
}
