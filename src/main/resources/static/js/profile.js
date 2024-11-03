document.getElementById('editProfileForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const newName = document.getElementById('editName').value;
    const newEmail = document.getElementById('editEmail').value;

    if (newName && newEmail) {
        try {
            const response = await fetch('/api/profile', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name: newName, email: newEmail })
            });

            if (response.ok) {
                alert(`Profile updated: Name - ${newName}, Email - ${newEmail}`);
                // Close the modal and optionally update the displayed profile info
                new bootstrap.Modal(document.getElementById('editProfileModal')).hide();
            } else {
                const errorData = await response.json();
                alert(`오류 발생: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error updating profile:', error);
            alert('서버 오류가 발생했습니다.');
        }
    }
});
