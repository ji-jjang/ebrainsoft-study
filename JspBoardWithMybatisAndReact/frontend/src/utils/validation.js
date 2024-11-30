export function checkBoardInput(form, type) {

  const MIN_PASSWORD_LENGTH = 4;
  const MAX_PASSWORD_LENGTH = 15;

  if (type === "delete") {
    const password = form.password.value;

    if (
      password.length < MIN_PASSWORD_LENGTH ||
      password.length > MAX_PASSWORD_LENGTH
    ) {
      alert(
        `비밀번호 길이는 ${MIN_PASSWORD_LENGTH} ~ ${MAX_PASSWORD_LENGTH} 이어야 합니다.`,
      );

      return false;
    }
    return true;
  }

  const MIN_AUTHOR_LENGTH = 3;
  const MAX_AUTHOR_LENGTH = 4;
  const MIN_TITLE_LENGTH = 4;
  const MAX_TITLE_LENGTH = 99;
  const MIN_CONTENT_LENGTH = 4;
  const MAX_CONTENT_LENGTH = 1999;

  const createdBy = form.createdBy.value;
  const password = form.password.value;
  const passwordConfirm = form.passwordConfirm
    ? form.passwordConfirm.value
 

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