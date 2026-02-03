import { Controller, Get, Route, Produces, Tags } from "tsoa";

@Route("privacy-policy")
@Tags("General")
export class PrivacyController extends Controller {
  @Get("/")
  @Produces("text/html")
  public async getPrivacyPolicy(): Promise<any> {
    this.setStatus(200);
    this.setHeader("Content-Type", "text/html");
    // Return the HTML string directly or read from file
    return `<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CacoRedes Privacy Policy</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 40px;
            background-color: #f5f5f5;
            color: #222;
            line-height: 1.6;
        }
        .container {
            max-width: 900px;
            margin: auto;
            background: #ffffff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        h1, h2 {
            margin-top: 24px;
        }
        p {
            margin: 12px 0;
        }
        ul {
            margin: 10px 0 10px 20px;
        }
        footer {
            margin-top: 40px;
            font-size: 14px;
            color: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>CacoRedes Privacy Policy</h1>
        <p>Last updated: February 3, 2026</p>

        <h2>Introduction</h2>
        <p>This Privacy Policy explains how our mobile application collects, uses, and protects information. By using the application, you agree to the practices described in this policy.</p>

        <h2>Information We Collect</h2>
        <p>The application may collect the following information:</p>
        <ul>
            <li>Username and password provided during account creation.</li>
            <li>Basic account-related data necessary for application functionality.</li>
        </ul>

        <h2>How We Use Information</h2>
        <p>The information collected is used only to operate and maintain the core features of the application, including authentication and account access.</p>

        <h2>User Tracking</h2>
        <p>Usernames, passwords, and account data are not used to track users across external services, applications, or websites. The application does not use personal credentials for behavioral tracking or advertising purposes.</p>

        <h2>Data Storage</h2>
        <p>All account data is stored securely within our internal database systems. Reasonable technical and organizational measures are implemented to protect data against unauthorized access, loss, or misuse.</p>

        <h2>Data Sharing</h2>
        <p>We do not sell, rent, or trade personal information. Data may only be disclosed if required by law or necessary to protect the integrity and security of the application.</p>

        <h2>Security</h2>
        <p>Passwords should be stored using secure hashing methods. While we take precautions to safeguard information, no method of electronic storage is completely secure.</p>

        <h2>User Rights</h2>
        <p>Users may request account deletion or modification of their information where applicable, subject to technical and legal requirements.</p>

        <h2>Changes to This Policy</h2>
        <p>This Privacy Policy may be updated periodically. Updates will be reflected by revising the "Last updated" date at the top of this page.</p>

        <h2>Contact</h2>
        <p>If you have questions regarding this Privacy Policy, contact the application administrator through the official support channel.</p>

        <footer>
            <p>&copy; 2026 CacoRedes. All rights reserved.</p>
        </footer>
    </div>
</body>
</html>
`;
  }
}