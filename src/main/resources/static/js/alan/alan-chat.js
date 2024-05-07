function sendQuestion() {
    const questionInput = document.getElementById('questionInput');
    const question = questionInput.value.trim();
    if (question !== '') {
        const userChat = document.querySelector('.user-chat .read-text');
        userChat.value = question;
        questionInput.value = '';

        const eventSource = new EventSource(`/alan/question?content=${encodeURIComponent(question)}`);

        eventSource.onmessage = function (event) {
            const alanChat = document.querySelector('.alan-chat .read-text');
            const text = event.data;

            if (text === 'end') {
                eventSource.close();
            } else {
                alanChat.value += text;
            }
        };

        eventSource.onerror = function (event) {
            console.error('EventSource error:', event);
            eventSource.close();
        };
    }
}