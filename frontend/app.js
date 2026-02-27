const apiBase = 'http://localhost:8080/api/v1';

async function postData(path, data) {
    const res = await fetch(apiBase + path, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    return res.json();
}

// connection
const connectForm = document.getElementById('connect-form');
connectForm.addEventListener('submit', async e => {
    e.preventDefault();
    const formData = new FormData(connectForm);
    const obj = Object.fromEntries(formData.entries());
    document.getElementById('connect-result').textContent = 'Loading...';
    try {
        const result = await postData('/ssh/connect', obj);
        document.getElementById('connect-result').textContent = JSON.stringify(result, null, 2);
    } catch (err) {
        document.getElementById('connect-result').textContent = err;
    }
});

// execute
const execForm = document.getElementById('execute-form');
execForm.addEventListener('submit', async e => {
    e.preventDefault();
    const formData = new FormData(execForm);
    const obj = Object.fromEntries(formData.entries());
    document.getElementById('execute-result').textContent = 'Loading...';
    try {
        const result = await postData('/ssh/execute', obj);
        document.getElementById('execute-result').textContent = JSON.stringify(result, null, 2);
    } catch (err) {
        document.getElementById('execute-result').textContent = err;
    }
});

// upload
const uploadForm = document.getElementById('upload-form');
uploadForm.addEventListener('submit', async e => {
    e.preventDefault();
    const formData = new FormData(uploadForm);
    const obj = Object.fromEntries(formData.entries());
    document.getElementById('upload-result').textContent = 'Loading...';
    try {
        const result = await postData('/keys/upload', obj);
        document.getElementById('upload-result').textContent = JSON.stringify(result, null, 2);
    } catch (err) {
        document.getElementById('upload-result').textContent = err;
    }
});
