export interface ICrawler {
  id?: number;
  name?: string;
  fetch?: number;
  source?: string;
}

export const defaultValue: Readonly<ICrawler> = {};
