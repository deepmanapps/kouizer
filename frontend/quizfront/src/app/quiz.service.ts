import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class QuizService {

  private rpayload : requestPayload = {
    categoryId: 9,
    amount: 1,
    difficulty: 'MEDIUM'
  };
  private apiUrl = 'http://localhost:8080/api/quiz/opentdb/start';
  constructor(private http: HttpClient) {


    }



 getQuestionsFromOpenTDB(rqp: requestPayload):Observable<TriviaApiResponse>{

  return this.http.post<TriviaApiResponse>(this.apiUrl,rqp)

  }













// refill Question interface with the correspendant source, i keep the same interface.


}

export interface requestPayload {
  categoryId: number;
  amount: number;
  difficulty: string;
}

export interface Question {
  category: string;
  type: string;
  difficulty: string;
  text: string;           // Renamed from 'question' for clarity
  correctAnswer: string;  // Renamed from 'correct_answer'
  incorrectAnswers: string[]; // Renamed from 'incorrect_answers'
  allAnswers?: string[];  // Optional: handy to mix correct/incorrect later
}

export interface TriviaApiResponse {
  response_code: number;
  results: TriviaApiResult[];
}

export interface TriviaApiResult {
  category: string;
  type: string;
  difficulty: string;
  question: string;
  correct_answer: string;
  incorrect_answers: string[];
}

export interface customApiResponse{
  id: number;
  content: string;
  difficulty: string;
  categoryName: string;
  answers: customApiAnswer[];
  }

export interface customApiAnswer{
  id: number;
  content:string;
  estCorrect: boolean;
  }
