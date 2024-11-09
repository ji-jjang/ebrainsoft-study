function checkBoardInput(form) {
  const MIN_AUTHOR_LENGTH = 3;
  const MAX_AUTHOR_LENGTH = 4;
  const MIN_PASSWORD_LENGTH = 4;
  const MAX_PASSWORD_LENGTH = 15;
  const MIN_TITLE_LENGTH = 4;
  const MAX_TITLE_LENGTH = 99;
  const MIN_CONTENT_LENGTH = 4;
  const MAX_CONTENT_LENGTH = 1999;

  const createdBy = form.createdBy.value;
  const password = form.password.value;
  const passwordConfirm = form.passwordConfirm ? form.passwordConfirm.value
      : null; // 수정 폼에는 확인란 없음
  const title = form.title.value;
  const content = form.content.value;

  console.log("hello checker");

  if (createdBy.length < MIN_AUTHOR_LENGTH || createdBy.length
      > MAX_AUTHOR_LENGTH) {
    alert(`작성자는 ${MIN_AUTHOR_LENGTH} ~ ${MAX_AUTHOR_LENGTH} 이어야 합니다.`);
    return false;
  }

  if (password.length < MIN_PASSWORD_LENGTH || password.length
      > MAX_PASSWORD_LENGTH) {
    alert(`비밀번호 길이는 ${MIN_PASSWORD_LENGTH} ~ ${MAX_PASSWORD_LENGTH} 이어야 합니다.`);
    return false;
  }

  if (passwordConfirm !== null && password !== passwordConfirm) {
    alert("입력한 비밀번호가 일치하지 않습니다.");
    return false;
  }

  if (title.length < MIN_TITLE_LENGTH || title.length > MAX_TITLE_LENGTH) {
    alert(`제목 길이는 ${MIN_TITLE_LENGTH} ~ ${MAX_TITLE_LENGTH} 이어야 합니다.`);
    return false;
  }

  if (content.length < MIN_CONTENT_LENGTH || content.length
      > MAX_CONTENT_LENGTH) {
    alert(`내용의 길이 ${MIN_CONTENT_LENGTH} ~ ${MAX_CONTENT_LENGTH} 이어야 합니다.`);
    return false;
  }

  return true;
}