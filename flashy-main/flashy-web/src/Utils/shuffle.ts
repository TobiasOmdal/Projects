
// Copied from SO and modified to typescript
// https://stackoverflow.com/questions/32308370/what-is-the-syntax-for-typescript-arrow-functions-with-generics
export const shuffle = <T>(array: T[]): T[] => {
  let currentIndex = array.length,  randomIndex;

  while (currentIndex > 0) {
    randomIndex = Math.floor(Math.random() * currentIndex);
    currentIndex--;

    [array[currentIndex], array[randomIndex]] = [
      array[randomIndex], array[currentIndex]];
  }

  return array;
}
