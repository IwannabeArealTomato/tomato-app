document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('container');
    const apiUrl = '/api';  // Base URL for the backend API

    // Initial animation for sign-in view
    setTimeout(() => {
        container.classList.add('sign-in');
    }, 200);

    // Toggle between sign-in and sign-up
    function toggle() {
        container.classList.toggle('sign-in');
        container.classList.toggle('sign-up');
    }

    // Store tokens securely in localStorage or secure storage
    function storeTokens(accessToken, refreshToken) {
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
    }

    // Get tokens from storage
    function getAccessToken() {
        return localStorage.getItem('accessToken');
    }

    function getRefreshToken() {
        return localStorage.getItem('refreshToken');
    }

    // Refresh token function for rotation
    async function refreshToken() {
        const refreshToken = getRefreshToken();
        if (!refreshToken) {
            alert('Session expired. Please sign in again.');
            toggle();
            return;
        }

        try {
            const response = await fetch(`${apiUrl}/auth/refresh`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ refreshToken })
            });

            if (response.ok) {
                const data = await response.json();
                storeTokens(data.accessToken, data.refreshToken);
                return data.accessToken;  // Return new access token
            } else {
                alert('Session expired. Please sign in again.');
                toggle();
            }
        } catch (error) {
            console.error("Token refresh failed:", error);
            alert("Token refresh failed. Please sign in again.");
        }
    }

    // Handle Sign-Up
    async function handleSignUp() {
        const username = document.querySelector('.sign-up input[placeholder="Username"]').value;
        const email = document.querySelector('.sign-up input[placeholder="Email"]').value;
        const password = document.querySelector('.sign-up input[placeholder="Password"]').value;
        const confirmPassword = document.querySelector('.sign-up input[placeholder="Confirm password"]').value;

        if (password !== confirmPassword) {
            alert("Passwords do not match.");
            return;
        }

        try {
            const response = await fetch(`${apiUrl}/signup`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, email, password })
            });

            if (response.ok) {
                alert("Sign up successful! Redirecting to sign in...");
                toggle();  // Switch to sign-in view on successful sign-up
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.message}`);
            }
        } catch (error) {
            console.error("Sign up failed:", error);
            alert("Sign up failed. Please try again.");
        }
    }

    // Handle Sign-In with JWT Authentication
    async function handleSignIn() {
        const username = document.querySelector('.sign-in input[placeholder="Username"]').value;
        const password = document.querySelector('.sign-in input[placeholder="Password"]').value;

        try {
            const response = await fetch(`${apiUrl}/signin`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                storeTokens(data.accessToken, data.refreshToken);
                alert("Sign in successful!");
                // Redirect or post-login actions
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.message}`);
            }
        } catch (error) {
            console.error("Sign in failed:", error);
            alert("Sign in failed. Please try again.");
        }
    }

    // Example of an API request requiring authentication
    async function fetchProtectedData() {
        let accessToken = getAccessToken();

        // If token is missing or expired, try to refresh it
        if (!accessToken) {
            accessToken = await refreshToken();
            if (!accessToken) return;  // Stop if refresh fails
        }

        try {
            const response = await fetch(`${apiUrl}/protected`, {
                method: 'GET',
                headers: { 'Authorization': `Bearer ${accessToken}` }
            });

            if (response.ok) {
                const data = await response.json();
                console.log("Protected data:", data);
            } else if (response.status === 401) {
                accessToken = await refreshToken();
                if (accessToken) fetchProtectedData();  // Retry after refreshing token
            } else {
                alert("Failed to access protected data.");
            }
        } catch (error) {
            console.error("Failed to fetch protected data:", error);
        }
    }

    // Event listeners for buttons
    document.querySelector('.sign-up .signup-button').addEventListener('click', handleSignUp);
    document.querySelector('.sign-in .signin-button').addEventListener('click', handleSignIn);

    // Toggle view event for 'already have an account' or 'create an account' link
    document.querySelector('.sign-up p b.pointer').addEventListener('click', toggle);
    document.querySelector('.sign-in p b.pointer').addEventListener('click', toggle);
});
