document.addEventListener('DOMContentLoaded', function() {
    const chatMessages = document.getElementById('chat-messages');
    const userInput = document.getElementById('user-input');
    const sendBtn = document.getElementById('send-btn');

    sendBtn.addEventListener('click', sendMessage);
    userInput.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            sendMessage();
        }
    });

    function sendMessage() {
        const message = userInput.value;
        if (message.trim() !== '') {
            appendMessage('User', message);
            userInput.value = '';

            fetch('/alan/sse-streaming?content=' + encodeURIComponent(message), {
                method: 'GET',
                headers: {
                    'Accept': 'text/event-stream'
                }
            })
                .then(function(response) {
                    const reader = response.body.getReader();
                    const decoder = new TextDecoder();

                    function processResult(result) {
                        if (result.done) return;

                        const chunk = decoder.decode(result.value, {stream: true});
                        appendMessage('Alan', chunk);
                        return reader.read().then(processResult);
                    }

                    reader.read().then(processResult);
                })
                .catch(function(error) {
                    appendMessage('Error', 'An error occurred while processing the request.');
                });
        }
    }

    function appendMessage(sender, message) {
        const messageElement = document.createElement('div');
        messageElement.textContent = `${sender}: ${message}`;
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }
});