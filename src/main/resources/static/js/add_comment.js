function addComment() {
    const commentText = document.getElementById('commentText').value;
    if (commentText) {
        const commentSection = document.querySelector('.comments_section');
        const newComment = document.createElement('div');
        newComment.className = 'comment';
        newComment.innerHTML = '<p><strong>You:</strong> ' + commentText + '</p>';
        commentSection.appendChild(newComment);
        document.getElementById('commentText').value = '';
    }
}