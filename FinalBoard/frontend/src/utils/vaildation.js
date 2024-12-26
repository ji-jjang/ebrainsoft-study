export const isValidEmailFormat = (email) => {
  const emailRegex = /\S+@\S+\.\S+/;
  return emailRegex.test(email);
};

export const isValidPasswordLength = (password) => {
  return password.length >= 4 && password.length <= 11;
};

export const isValidNameLength = (password) => {
  return password.length >= 2 && password.length <= 5;
};

export const isValidPasswordRules = (password) => {
  const chars = password.split("");
  for (let i = 0; i < chars.length - 2; i++) {
    const first = chars[i].charCodeAt(0);
    const second = chars[i + 1].charCodeAt(0);
    const third = chars[i + 2].charCodeAt(0);

    if (second === first + 1 && third === second + 1) {
      return false;
    }

    if (second === first - 1 && third === second - 1) {
      return false;
    }
  }

  return true;
};
