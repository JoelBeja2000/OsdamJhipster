import { IVoluntariado } from 'app/shared/model/voluntariado.model';

export interface IInscripciones {
  id?: number;
  name?: string | null;
  voluntariado?: IVoluntariado | null;
}

export const defaultValue: Readonly<IInscripciones> = {};
