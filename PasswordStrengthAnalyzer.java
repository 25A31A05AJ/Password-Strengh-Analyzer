import java.util.Scanner;

public class PasswordStrengthAnalyzer {

    public static void main(String[] args) {
System.out.println("=================================");
System.out.println("     PASSWORD STRENGTH ANALYZER");
System.out.println("=================================");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your password: ");
        String password = sc.nextLine();
String lowerPassword = password.toLowerCase();

if (lowerPassword.equals("password") ||
    lowerPassword.equals("123456") ||
    lowerPassword.equals("qwerty") ||
    lowerPassword.equals("admin")) {

    System.out.println("Warning: This is a common password!");
}
        System.out.println("Password entered: " + password);
        int score = 0;
System.out.println();
System.out.println("Password Analysis:");

if (password.length() >= 8)
    System.out.println("✓ Length: Good");
else
    System.out.println("✗ Length: Too Short");

if (password.length() >= 8)
    score++;
//Uppercase
if (password.matches(".*[A-Z].*")) {
    score++;
    System.out.println("✓ Uppercase: Good");
} else {
    System.out.println("✗ Uppercase: Missing");
}
//Lowercase
if (password.matches(".*[a-z].*")) {
    score++;
    System.out.println("✓ Lowercase: Good");
} else {
    System.out.println("✗ Lowercase: Missing");
}
//Number
if (password.matches(".*[0-9].*")) {
    score++;
    System.out.println("✓ Number: Good");
} else {
    System.out.println("✗ Number: Missing");
}
//Special character
if (password.matches(".*[^a-zA-Z0-9].*")) {
    score++;
    System.out.println("✓ Special Character: Good");
} else {
    System.out.println("✗ Special Character: Missing");
}
System.out.println("Score: " + score + "/5");
if (score <= 2) {
    System.out.println("Password Strength: WEAK");
} else if (score <= 4) {
    System.out.println("Password Strength: MEDIUM");
} else {
    System.out.println("Password Strength: STRONG");
}


        sc.close();

    }
}