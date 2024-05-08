function countBytes(editor, containerSelector, limit) {

    const content = editor.value;
    const byteCount = countUtf8Bytes(content);

    containerSelector.textContent = byteCount + "/" + limit;

    if (byteCount > limit) {
        alert('허용된 글자수가 초과되었습니다.')
        const truncatedContent = cutByLen(content, limit);
        editor.value = truncatedContent;
    }
}

function countUtf8Bytes(str) {
    let byteCount = 0;

    for (let i = 0; i < str.length; i++) {
        const charCode = str.charCodeAt(i);
        (charCode==10) ? byteCount+=2: byteCount+=1;
    }

    return byteCount;
}

function cutByLen(str, maxByte) {
    for (let b = i = 0; c = str.charCodeAt(i); i++) {

        b += c == 10 ? 2 : 1;
        if (b > maxByte) break;
    }

    return str.substring(0, i);
}
