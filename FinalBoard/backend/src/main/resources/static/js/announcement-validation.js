function checkAnnouncementForm(form) {

  const MIN_TITLE_LENGTH = 4;
  const MAX_TITLE_LENGTH = 99;
  const MIN_CONTENT_LENGTH = 4;
  const MAX_CONTENT_LENGTH = 3999;

  const title = form.title.value;
  const content = form.content.value;

  if (title.length < MIN_TITLE_LENGTH || title.length > MAX_TITLE_LENGTH) {
    alert(`제목 길이는 ${MIN_TITLE_LENGTH} ~ ${MAX_TITLE_LENGTH} 이어야 합니다.`);
    return false;
  }

  if (content.length < MIN_CONTENT_LENGTH || content.length
      > MAX_CONTENT_LENGTH) {
    alert(`내용의 길이 ${MIN_CONTENT_LENGTH} ~ ${MAX_CONTENT_LENGTH} 이어야 합니다.`);
    return false;
  }

  if (!confirm("정말로 등록/수정하시겠습니까?")) {
    return false;
  }

  return true;
}