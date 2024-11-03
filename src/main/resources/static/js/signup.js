document.addEventListener('DOMContentLoaded', function () {
    const apiUrl = '/api';

    // 회원가입 처리
    async function handleSignUp() {
        const username = document.querySelector('.sign-up input[placeholder="닉네임"]').value;
        const email = document.querySelector('.sign-up input[placeholder="이메일"]').value;
        const password = document.querySelector('.sign-up input[placeholder="비밀번호"]').value;
        const role = document.querySelector('.sign-up input[placeholder="사장 or 고객"]').value;

        if (!username || !email || !password || !role) {
            alert("모든 필드를 입력해 주세요.");
            return;
        }

        try {
            const response = await fetch(`${apiUrl}/signup`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, email, password, role })
            });

            if (response.ok) {
                alert("회원가입에 성공했습니다! 로그인 페이지로 이동합니다.");
                window.location.href = "login.html"; // 로그인 페이지로 이동
            } else {
                const errorData = await response.json();
                alert(`오류: ${errorData.message}`);
            }
        } catch (error) {
            console.error("회원가입 실패:", error);
            alert("회원가입에 실패했습니다. 다시 시도해주세요.");
        }
    }

    // 회원가입 버튼에 이벤트 리스너 추가
    document.querySelector('.sign-up .signup-button').addEventListener('click', handleSignUp);
});
