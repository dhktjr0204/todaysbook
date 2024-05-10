document.addEventListener('DOMContentLoaded', function () {
    const chatMessages = document.getElementById('chat-messages');
    const userInput = document.getElementById('user-input');
    const sendBtn = document.getElementById('send-btn');

    let eventSource = null;

    sendBtn.addEventListener('click', sendMessage);
    userInput.addEventListener('keyup', function (event) {
        if (event.key === 'Enter') {
            sendMessage();
        }
    });

    function sendMessage() {
        const content = userInput.value.trim();

        if (content !== '') {
            appendMessage('User', content);
            userInput.value = '';

            if (eventSource) {
                eventSource.close();
            }

            eventSource = new EventSource(`/alan/sse-streaming?content=${encodeURIComponent(content)}`);

            let isFirstChunk = true;
            let alanMessageElement = null;

            eventSource.onmessage = function (event) {
                const response = JSON.parse(event.data);
                if (response.type === 'continue') {
                    if (isFirstChunk) {
                        alanMessageElement = createMessageElement('Alan');
                        chatMessages.appendChild(alanMessageElement);
                        isFirstChunk = false;
                    }
                    const contentElement = document.createElement('span');
                    contentElement.innerHTML = formatContent(response.data.content);
                    alanMessageElement.querySelector('.read-text').appendChild(contentElement);
                } else if (response.type === 'complete') {
                    eventSource.close();
                }
                chatMessages.scrollTop = chatMessages.scrollHeight;
            };

            eventSource.onerror = function () {
                eventSource.close();
            };
        }
    }

    function appendMessage(sender, content) {
        const messageElement = createMessageElement(sender);
        messageElement.querySelector('.read-text').textContent = content;
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    function createMessageElement(sender) {
        const messageElement = document.createElement('div');
        messageElement.className = sender === 'User' ? 'user-chat' : 'alan-chat';
        messageElement.innerHTML = `
        ${sender === 'User' ? '<div class="read-text"></div><img src="/images/user_chat.png">' : '<img src="/images/alan_chat.png"><div class="read-text"></div>'}
    `;
        return messageElement;
    }

    function formatContent(content) {
        content = content.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
        content = content.replace(/\n/g, '<br>');
        return content;
    }
});


//
// document.addEventListener('DOMContentLoaded', function () {
//     const chatMessages = document.getElementById('chat-messages');
//     const userInput = document.getElementById('user-input');
//     const sendBtn = document.getElementById('send-btn');
//
//     let eventSource = null;
//
//     sendBtn.addEventListener('click', sendMessage);
//     userInput.addEventListener('keyup', function (event) {
//         if (event.key === 'Enter') {
//             sendMessage();
//         }
//     });
//
//     function sendMessage() {
//         const content = userInput.value.trim();
//
//         if (content !== '') {
//             appendMessage('User', content);
//             userInput.value = '';
//
//             if (eventSource) {
//                 eventSource.close();
//             }
//
//             eventSource = new EventSource(`/alan/sse-streaming?content=${encodeURIComponent(content)}`);
//
//             let isFirstChunk = true;
//             let alanMessageElement = null;
//
//             eventSource.onmessage = function (event) {
//                 const response = JSON.parse(event.data);
//                 if (response.type === 'continue') {
//                     if (isFirstChunk) {
//                         alanMessageElement = document.createElement('div');
//                         alanMessageElement.className = 'alan-message';
//                         alanMessageElement.innerHTML = `<strong>Alan:</strong>`;
//                         chatMessages.appendChild(alanMessageElement);
//                         isFirstChunk = false;
//                     }
//                     const contentElement = document.createElement('span');
//                     contentElement.innerHTML = formatContent(response.data.content);
//                     alanMessageElement.appendChild(contentElement);
//                 } else if (response.type === 'complete') {
//                     if (alanMessageElement) {
//                         // const contentElement = document.createElement('span');
//                         // contentElement.textContent = response.data.content;
//                         // alanMessageElement.appendChild(contentElement);
//                     } else {
//                         //appendMessage('Alan', response.data.content);
//                     }
//                     eventSource.close();
//                 }
//                 chatMessages.scrollTop = chatMessages.scrollHeight;
//             };
//
//             eventSource.onerror = function () {
//                 eventSource.close();
//             };
//         }
//     }
//
//
//     function appendMessage(sender, content, isPartial = false) {
//         const messageElement = document.createElement('div');
//         messageElement.className = sender === 'User' ? 'user-message' : 'alan-message';
//         messageElement.innerHTML = `<strong>${sender}: </strong> ${content}`;
//
//         if (isPartial && chatMessages.lastElementChild?.classList.contains('alan-message')) {
//             chatMessages.lastElementChild.innerHTML = messageElement.innerHTML;
//         } else {
//             chatMessages.appendChild(messageElement);
//         }
//         chatMessages.scrollTop = chatMessages.scrollHeight;
//     }
//
//     function formatContent(content) {
//         content = content.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
//         content = content.replace(/\n/g, '<br>');
//         return content;
//     }
//
// });