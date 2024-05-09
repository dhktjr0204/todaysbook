document.getElementById('chatForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const userInput = document.getElementById('userInput').value;

    fetch('/alan/chat', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({content: userInput})
    })
        .then(response => response.text())
        .then(html => {
            document.open();
            document.write(html);
            document.close();
        })
        .catch(error => console.error('Error:', error));
});