import { ICrawler } from 'app/shared/model/sample/crawler.model';

export interface IFilters {
  id?: number;
  crawler?: ICrawler | null;
}

export const defaultValue: Readonly<IFilters> = {};
