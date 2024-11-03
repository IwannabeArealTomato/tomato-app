let posts = [];

async function fetchPosts() {
    try {
        const response = await fetch('/api/posts');
        if (response.ok) {
            posts = await response.json();
            displayPosts();
        } else {
            console.error('Failed to fetch posts');
        }
    } catch (error) {
        console.error('Error fetching posts:', error);
    }
}

async function createPost() {
    const content = document.getElementById('postContent').value;
    if (content) {
        try {
            const response = await fetch('/api/posts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ content })
            });

            if (response.ok) {
                document.getElementById('postContent').value = '';
                fetchPosts();
            } else {
                console.error('Failed to create post');
            }
        } catch (error) {
            console.error('Error creating post:', error);
        }
    }
}

async function likePost(postId) {
    try {
        const response = await fetch(`/api/posts/${postId}/like`, { method: 'POST' });
        if (response.ok) {
            fetchPosts();
        } else {
            console.error('Failed to like post');
        }
    } catch (error) {
        console.error('Error liking post:', error);
    }
}

async function deletePost(postId) {
    try {
        const response = await fetch(`/api/posts/${postId}`, { method: 'DELETE' });
        if (response.ok) {
            fetchPosts();
        } else {
            console.error('Failed to delete post');
        }
    } catch (error) {
        console.error('Error deleting post:', error);
    }
}

async function logout() {
    try {
        const response = await fetch('/api/logout', { method: 'POST' });
        if (response.ok) {
            location.href = 'index.html'; // 로그아웃 후 홈페이지로 이동
        } else {
            console.error('Failed to log out');
        }
    } catch (error) {
        console.error('Error logging out:', error);
    }
}

// Call fetchPosts on load to populate the feed
document.addEventListener('DOMContentLoaded', fetchPosts);
