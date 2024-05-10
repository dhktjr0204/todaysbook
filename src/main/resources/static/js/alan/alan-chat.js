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
                    let alanResponse = '';

                    function processResult(result) {
                        if (result.done) {
                            alanResponse = alanResponse.replace(/['"]+/g, '');
                            alanResponse = formatResponse(alanResponse);
                            appendMessage('Alan', alanResponse);
                            return;
                        }

                        const chunk = decoder.decode(result.value, {stream: true});
                        const cleanedChunk = chunk.replace(/data:/g, '').trim();
                        alanResponse += cleanedChunk;
                        return reader.read().then(processResult);
                    }

                    reader.read().then(processResult);
                })
                .catch(function(error) {
                    appendMessage('Error', 'An error occurred while processing the request.');
                });
        }
    }

    function formatResponse(response) {
        // 줄바꿈 처리
        response = response.replace(/\\n/g, '<br>');

        // 텍스트 강조 처리
        response = response.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

        // 각주 처리
        response = response.replace(/\[(.*?)\]/g, '<sup>$1</sup>');

        return response;
    }

    function appendMessage(sender, message) {
        const messageElement = document.createElement('div');
        messageElement.innerHTML = `${sender}: ${message}`;
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }
});