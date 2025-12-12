import { Component,inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { QuizService } from './quiz.service';

// --- INTERFACES ---
interface Question {
  id: number;
  text: string;
  options: string[];
  correctIndex: number;
}

interface requestPayload {
  categoryId: number;
  amount: number;
  difficulty: string;
}

interface QuizConfig {
  amount: number;      // Number of Questions
  category: any;       // Category ID or 'any'
  difficulty: string;  // 'any', 'easy', 'medium', 'hard'
  type: string;        // 'any', 'multiple', 'boolean'
  source: string;      // 'local' or 'custom'
}

// Interface for the Form Data
interface CustomQuestionForm {
  text: string;
  correctAnswer: string;
  incorrect1: string;
  incorrect2: string;
  incorrect3: string;
}

interface opentdbData{

  response_code: number;
  results : opentdbDataResults [];

}

interface opentdbDataResults{
  category: string;
  type: string;
  difficulty: string;
  question: string;
  correct_answer:string;
  incorrect_answers: string[];
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styles: []
})
export class AppComponent {
  private quizServices = inject(QuizService);
  results: any = [];
  // State Management
  currentView: 'home' | 'options' | 'quiz' | 'score' | 'create' = 'home';

  nickname: string = '';
  showError: boolean = false;

  // OpenTDB Style Configuration
  config: QuizConfig = {
    amount: 5,
    category: 'any',
    difficulty: 'any',
    type: 'any',
    source: 'local'
  };

  // Standard OpenTDB Categories
  categories = [
    { id: 9, name: 'General Knowledge' },
    { id: 10, name: 'Entertainment: Books' },
    { id: 11, name: 'Entertainment: Film' },
    { id: 12, name: 'Entertainment: Music' },
    { id: 14, name: 'Entertainment: Television' },
    { id: 15, name: 'Entertainment: Video Games' },
    { id: 17, name: 'Science & Nature' },
    { id: 18, name: 'Science: Computers' },
    { id: 19, name: 'Science: Mathematics' },
    { id: 21, name: 'Sports' },
    { id: 22, name: 'Geography' },
    { id: 23, name: 'History' },
    { id: 27, name: 'Animals' },
    { id: 28, name: 'Vehicles' }
  ];

  // Creator Form Data
  form: CustomQuestionForm = {
    text: '',
    correctAnswer: '',
    incorrect1: '',
    incorrect2: '',
    incorrect3: ''
  };

  currentQuestionIndex: number = 0;
  userAnswers: (number | null)[] = [];
  score: number = 0;

  // Data Stores
  questions: Question[] = [];
  customQuestions: Question[] = []; // User created questions

  // Local Mock Fallback
  localQuestions: Question[] = [];
 // localQuestions: Question[] = [
  //  { id: 1, text: "Fallback: What is Angular?", options: ["Framework", "Library", "Database", "OS"], correctIndex: 0 },
  //  { id: 2, text: "Fallback: What is 2+2?", options: ["3", "4", "5", "6"], correctIndex: 1 }
 // ];


  // --- CREATOR LOGIC ---

  addCustomQuestion() {
    if (!this.form.text || !this.form.correctAnswer || !this.form.incorrect1) {
      alert("Please fill in at least the Question, Correct Answer, and one Incorrect Answer.");
      return;
    }

    let options = [
      this.form.correctAnswer,
      this.form.incorrect1,
      this.form.incorrect2,
      this.form.incorrect3
    ].filter(opt => opt && opt.trim() !== '');

    const correctAnswerText = this.form.correctAnswer;

    // Simple shuffle
    options = options.sort(() => Math.random() - 0.5);

    const newIndex = options.indexOf(correctAnswerText);

    const newQ: Question = {
      id: Date.now(),
      text: this.form.text,
      options: options,
      correctIndex: newIndex
    };

    this.customQuestions.push(newQ);

    this.form = { text: '', correctAnswer: '', incorrect1: '', incorrect2: '', incorrect3: '' };
  }

  removeCustomQuestion(index: number) {
    this.customQuestions.splice(index, 1);
  }

  playCustomQuiz() {
    this.config.source = 'custom';
    if (!this.nickname) this.nickname = "Creator";
    this.startQuiz();
  }

  callAndAdaptToQuestions(rpld : requestPayload): Question[]{

   let initQuestions : Question[] = [];

    const rpayload : requestPayload = {
      categoryId: 9,
      amount: 3,
      difficulty: 'MEDIUM'
    };

    this.quizServices.getQuestionsFromOpenTDB(rpayload).subscribe({
      next: (response) => {
        this.results = response;
        console.log("DATA ==> ", this.results);
        let _opentdbData : opentdbData = this.results;
        for(let obj of _opentdbData.results){

            console.log("incorrect_answers ==> ", obj.incorrect_answers);
              
    }
      },
      error: (err) => {
        console.error("Error fetching quiz:", err);
      }
    });
     return initQuestions;
  }

  // --- MAIN QUIZ LOGIC ---
 shuffleArray<T>(array: T[]): T[] {
  for (let i = array.length - 1; i > 0; i--) {
    // 1. Generate a random index 'j' between 0 and 'i' (inclusive).
    // The Math.floor(Math.random() * (i + 1)) ensures a uniform random integer.
    const j = Math.floor(Math.random() * (i + 1));
    
    // 2. Swap element at 'i' with the element at 'j'.
    // This uses array destructuring for an efficient swap.
    [array[i], array[j]] = [array[j], array[i]];
  }
  
  // Return the shuffled array.
  return array;

}

findIndex(array: string[],correctAnswer:string): number{
let foundIndex: number = -1;
  array.forEach((q:string, index:number) => {
      if(correctAnswer === q) foundIndex= index;
  })
  return foundIndex;

}
  startQuiz() {
    // My test goes from here ...
    let initQuestions : Question[] = [];
    

   const rpayload : requestPayload = {
      categoryId: 9,
      amount: 3,
      difficulty: 'MEDIUM'
    };
    this.quizServices.getQuestionsFromOpenTDB(rpayload).subscribe({
      next: (response) => {
        this.results = response;
        console.log("DATA ==> ", this.results);
        let _opentdbData : opentdbData = this.results;
        let i:number=_opentdbData.results.length+1;
        let mixedQuestion:string[] =[];
        for(let obj of _opentdbData.results){

            console.log("obj ==> ", obj);
            mixedQuestion.push(obj.correct_answer);
            obj.incorrect_answers.forEach((q :string) => {
              mixedQuestion.push(q);
            })
            mixedQuestion=this.shuffleArray<string>(mixedQuestion);
            let oneQuestion: Question={
                id: i,
                text: obj.question,
                options: mixedQuestion,
                correctIndex: this.findIndex(mixedQuestion,obj.correct_answer)

            };
            initQuestions.push(oneQuestion)
            //----------------
            console.log("initQuestions ==> ", initQuestions);
            this.localQuestions=initQuestions;
            mixedQuestion=[];
    }
      },
      error: (err) => {
        console.error("Error fetching quiz:", err);
      }
    });

    

    
    // My test ends here ...

    if (!this.nickname.trim() && this.currentView !== 'create') {
      this.showError = true;
      return;
    }

    this.showError = false;

    // Reset State
    this.currentQuestionIndex = 0;
    this.score = 0;

    // Load Data based on Source
    if (this.config.source === 'custom') {
        this.questions = [...this.customQuestions];
    } else {
        // NOTE: In a real app with API, here is where you would use
        // this.config.category, this.config.difficulty, etc. to fetch URL
        this.questions = [...this.localQuestions];
    }

    this.userAnswers = new Array(this.questions.length).fill(null);
    this.currentView = 'quiz';
  }

  // --- UTILS ---
  getCategoryName(id: number): string {
    const cat = this.categories.find(c => c.id === id);
    return cat ? cat.name : 'Unknown';
  }

  // --- NAVIGATION ---
  goToOptions() { this.currentView = 'options'; }
  goToCreate() { this.currentView = 'create'; }
  goHome() { this.currentView = 'home'; }

  // --- QUIZ INTERACTION ---
  selectAnswer(optionIndex: number) { this.userAnswers[this.currentQuestionIndex] = optionIndex; }

  nextQuestion() {
    if (this.currentQuestionIndex < this.questions.length - 1) this.currentQuestionIndex++;
  }

  prevQuestion() {
    if (this.currentQuestionIndex > 0) this.currentQuestionIndex--;
  }

  finishQuiz() {
    this.calculateScore();
    this.currentView = 'score';
  }

  calculateScore() {
    this.score = 0;
    this.questions.forEach((q, index) => {
      if (this.userAnswers[index] === q.correctIndex) this.score++;
    });
  }
}
