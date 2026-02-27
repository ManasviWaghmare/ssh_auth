// simple client-side routing/login simulation

if(location.pathname.endsWith('login.html') || location.pathname.endsWith('/')) {
    // login page logic
    const form = document.getElementById('login-form');
    form && form.addEventListener('submit', e => {
        e.preventDefault();
        // fake login, always succeed
        location.href = 'dashboard.html';
    });
}

if(location.pathname.endsWith('dashboard.html')) {
    document.getElementById('logout-btn') && document.getElementById('logout-btn').addEventListener('click', ()=> {
        location.href = 'login.html';
    });
}
