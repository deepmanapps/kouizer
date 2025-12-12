# Kouizer 🎯

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Open Trivia DB](https://img.shields.io/badge/Open%20Trivia%20DB-4285F4?style=for-the-badge&logo=google&logoColor=white)

A modern, interactive quiz application built with Angular that allows users to take quizzes from an external API or create their own custom questions. The app features a clean, responsive UI with real-time question management using RxJS Observables.

## ✨ Features

- **🎮 Interactive Quiz Taking**: Answer multiple-choice questions with a clean, intuitive interface
- **📝 Custom Quiz Creation**: Create your own questions with multiple answer options
- **🔄 Observable Pattern**: Reactive data management using RxJS BehaviorSubject
- **🎨 Modern UI**: Beautiful, responsive design with Tailwind CSS styling
- **📊 Score Tracking**: Real-time score calculation and results display
- **⚙️ Configurable Options**: Customize quiz settings (category, difficulty, type)
- **🌐 API Integration**: Fetch questions from Open Trivia Database API

## 🏗️ Architecture

### Frontend (Angular)
- **Component**: `AppComponent` - Main application component managing all views
- **Service**: `QuizService` - Handles API communication with the backend
- **State Management**: RxJS BehaviorSubject for reactive question management
- **Styling**: Tailwind CSS for modern, responsive design

### Observable Pattern Implementation

```typescript
// Data Stores
private questionSubject = new BehaviorSubject<Question[]>([]);
question$: Observable<Question[]> = this.questionSubject.asObservable();

// Getter for synchronous template access
get questions(): Question[] {
  return this.questionSubject.value;
}
```

**Benefits:**
- ✅ Reactive data updates
- ✅ Type-safe question management
- ✅ Flexible access patterns (synchronous getter + async Observable)
- ✅ Clean separation of concerns

## 🚀 Getting Started

### Prerequisites
- Node.js (v14 or higher)
- Angular CLI (`npm install -g @angular/cli`)
- Java 17+ (for backend)
- Maven (for backend)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd kouizer
   ```

2. **Install frontend dependencies**
   ```bash
   cd frontend/quizfront
   npm install
   ```

3. **Install backend dependencies**
   ```bash
   cd ../../backend
   mvn clean install
   ```

### Running the Application

1. **Start the application in single deployment mode**
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   Backend and Frontend will run on `http://localhost:8080`


3. **Open in Browser**
   Navigate to `http://localhost:4200`


## 🛠️ Technical Stack

### Frontend
- **Framework**: Angular 17+
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **State Management**: RxJS (BehaviorSubject, Observables)
- **HTTP Client**: Angular HttpClient

### Backend
- **Framework**: Spring Boot
- **Language**: Java 17
- **API**: RESTful services
- **External API**: Open Trivia Database

## 📁 Project Structure

```
kouizer/
├── frontend/
│   └── quizfront/
│       ├── src/
│       │   ├── app/
│       │   │   ├── app.component.ts      # Main component
│       │   │   ├── app.component.html    # Template
│       │   │   ├── app.component.css     # Styles
│       │   │   └── quiz.service.ts       # API service
│       │   └── ...
│       └── ...
└── backend/
    └── ...
```

## 🔑 Key Features Explained

### Observable Pattern
The app uses RxJS BehaviorSubject to manage question state reactively:
- **`questionSubject`**: Private subject that holds the current questions
- **`question$`**: Public Observable for reactive subscriptions
- **`questions` getter**: Synchronous access for templates

### Async Data Handling
Questions are loaded asynchronously from the API, with proper error handling:
```typescript
this.quizServices.getQuestionsFromOpenTDB(payload).subscribe({
  next: (response) => {
    // Process and update questions
    this.questionSubject.next(processedQuestions);
    this.currentView = 'quiz';
  },
  error: (err) => {
    alert("Failed to load quiz questions. Please check if the backend server is running.");
  }
});
```

## 👨‍💻 Author

Built with ❤️ using Angular and Spring Boot

---

**Happy Quizzing! 🎉**
