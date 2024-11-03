document.addEventListener('DOMContentLoaded', function () {
    const apiUrl = '/api';

    // 토큰을 안전하게 로컬 스토리지에 저장
    function storeTokens(accessToken, refreshToken) {
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
    }

    // 로그인 처리 (JWT 인증)
    async function handleSignIn() {
        const email = document.querySelector('.sign-in input[placeholder="이메일"]').value;
        const password = document.querySelector('.sign-in input[placeholder="비밀번호"]').value;

        if (!email || !password) {
            alert("이메일과 비밀번호를 입력해 주세요.");
            return;
        }

        try {
            const response = await fetch(`${apiUrl}/signin`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                const data = await response.json();
                storeTokens(data.accessToken, data.refreshToken);
                alert("로그인에 성공했습니다!");
                // 로그인 성공 후 리다이렉션 추가 가능
            } else {
                const errorData = await response.json();
                alert(`오류: ${errorData.message}`);
            }
        } catch (error) {
            console.error("로그인 실패:", error);
            alert("로그인에 실패했습니다. 다시 시도해주세요.");
        }
    }

    // 로그인 버튼에 이벤트 리스너 추가
    document.querySelector('.sign-in .signin-button').addEventListener('click', handleSignIn);
});
