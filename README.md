# Holy Verses - Bible Android App

A comprehensive Bible reading application that provides daily verse notifications, offline Bible access, prayer tracking gamification, and cloud synchronization through Google Sign-In integration.

## Features

### 🎯 Core Functionality
- **Daily Verse Notifications**: Receive unique Bible verses at scheduled times
- **Offline Bible Access**: Complete Bible content stored locally after initial download
- **Prayer Tracking**: Gamified prayer system with points, streaks, and achievements
- **Google Sign-In**: Cloud synchronization of user progress and preferences

### 📱 User Interface
- **Material You Design**: Modern Google Material Design 3 implementation
- **Dark Mode Default**: Elegant dark theme optimized for reading
- **Google Sans Font**: Clean, readable typography throughout the app
- **Responsive Layout**: Optimized for various screen sizes and orientations

### 🔔 Notification System
- **Daily Verse Delivery**: Unique verses without repetition
- **Prayer Reminders**: Customizable prayer reminder schedules
- **Smart Scheduling**: Uses WorkManager for reliable background processing

### 🎮 Gamification Features
- **Prayer Points**: Earn points for consistent prayer habits
- **Achievement System**: Unlock badges for milestones and streaks
- **Progress Tracking**: Visual progress indicators and statistics
- **Streak Counter**: Maintain daily prayer consistency

## Technical Architecture

### 🏗️ Architecture Pattern
- **MVVM (Model-View-ViewModel)**: Clean separation of concerns
- **Repository Pattern**: Centralized data access layer
- **Dependency Injection**: Hilt for efficient dependency management

### 🛠️ Key Technologies
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room (SQLite wrapper)
- **Background Tasks**: WorkManager
- **Git Operations**: JGit for Bible content cloning
- **Notifications**: NotificationManager with WorkManager
- **Google Services**: Firebase Auth and Firestore

### 📁 Project Structure
```
app/
├── data/
│   ├── database/          # Room database and DAOs
│   ├── repository/        # Data repositories
│   └── remote/           # Git service for Bible content
├── domain/
│   └── model/            # Data models and entities
├── presentation/
│   ├── ui/               # Compose UI components
│   ├── screens/          # Screen implementations
│   └── viewmodel/        # ViewModels
├── service/              # Notification and background services
├── worker/               # WorkManager workers
└── di/                   # Hilt dependency injection modules
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 26+ (Android 8.0)
- Target SDK 36 (Android 14)

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle dependencies
4. Build and run the app

### Configuration
- The app will automatically clone Bible content from the specified GitHub repository on first launch
- Configure notification times in the Settings screen
- Set up prayer reminder schedules as needed

## Database Schema

### Core Tables
- **books**: Bible book information (name, testament, chapters)
- **chapters**: Chapter details for each book
- **verses**: Individual Bible verses with delivery tracking
- **users**: User authentication and profile data
- **prayer_sessions**: Prayer activity logging and scoring
- **achievements**: Gamification achievements and progress

## Features in Detail

### First Launch Setup
- Automatic Bible content download from GitHub repository
- Progress tracking with user feedback
- Error handling and retry mechanisms
- Offline storage for complete Bible access

### Bible Reading
- Book selection by testament (Old/New)
- Chapter navigation within books
- Verse-by-verse display with proper formatting
- Search functionality across all content
- Reading history and progress tracking

### Prayer System
- **Scoring Algorithm**:
  - Base points: 10 per prayer session
  - Streak bonus: +5 points per consecutive day (max +50)
  - Time accuracy bonus: +3 points for timely prayers
  - Weekly/Monthly consistency bonuses

- **Achievement Categories**:
  - First Steps: Complete first prayer
  - Dedicated: 7-day prayer streak
  - Faithful: 30-day prayer streak
  - Consistent: Monthly consistency
  - Century Club: 100 total prayers

### Notification Management
- Daily verse scheduling (1-5 times per day)
- Prayer reminder customization
- Notification sound and vibration options
- Smart delivery timing

## Performance Requirements

- **App Launch**: < 3 seconds after initial setup
- **Verse Search**: < 500ms response time
- **Database Queries**: < 100ms for single verse retrieval
- **Memory Usage**: < 100MB during normal operation
- **Battery Optimization**: Minimal background processing

## Security & Privacy

- Secure HTTPS connections for repository cloning
- Input validation and SQL injection protection
- Secure storage of user preferences
- Minimal permission requirements
- Clear privacy policy and data usage transparency

## Testing

### Test Coverage
- **Unit Tests**: Business logic and data layer
- **Integration Tests**: Database operations and notifications
- **UI Tests**: User interaction flows and accessibility
- **Target**: Minimum 80% code coverage

### Test Categories
- Bible content parsing and storage
- Notification scheduling and delivery
- Prayer scoring and achievement system
- Google Sign-In integration
- Error handling and edge cases

## Future Enhancements

### Phase 2 Features
- Multiple Bible translations
- Audio verse playback
- Social sharing and community features
- Reading plans and challenges
- Cross-references and concordance

### Phase 3 Features
- Advanced prayer analytics
- Custom prayer categories
- Prayer journal with text entries
- Community prayer requests
- Wearable device integration

## Contributing

1. Fork the repository
2. Create a feature branch
3. Implement your changes
4. Add appropriate tests
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the GitHub repository
- Check the documentation and FAQ
- Contact the development team

---

**Holy Verses** - Strengthening faith through technology and scripture.
