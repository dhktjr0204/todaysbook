document.addEventListener('DOMContentLoaded', function() {
    var chatForm = document.getElementById('chatForm');
    var userInput = document.getElementById('userInput');
    var chatHistory = document.getElementById('chatHistory');

    chatForm.addEventListener('submit', function(event) {
        event.preventDefault();
        var userMessage = userInput.value;

        fetch('/alan/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({content: userMessage})
        })
            .then(function(response) {
                return response.text();
            })
            .then(function(data) {
                chatHistory.innerHTML = data;
                userInput.value = '';
            })
            .catch(function(error) {
                console.log('Error:', error);
            });
    });
});