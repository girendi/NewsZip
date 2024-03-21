# NewsZip - Your Mobile News Hub
NewsExplorer is a native mobile application designed to provide users with easy access to a wide range of news articles using the NewsAPI. The app offers an intuitive way to explore news by categories, sources, and specific articles, including detailed views through a web interface.

## Features
<ul>
  <li>[News Categories]: Users can browse a list of news categories to find their topics of interest.</li>
  <li>[News Sources]: Clicking on a category displays relevant news sources.</li>
  <li>[News Articles]: Users can select a source to see a list of articles provided by that source.</li>
  <li>[Article Detail]: Selecting an article opens a detailed view in a web view, showing the full article.</li>
  <li>[Search Functionality]: The app includes a search feature to find news sources and articles.</li>
  <li>[Endless Scrolling]: Both the news sources and articles list support endless scrolling for a seamless reading experience.</li>
  <li>[Error Handling]: The application handles both positive and negative cases to ensure a robust user experience.</li>
</ul>

## Getting Started
### Prerequisites
<ul>
  <li>Android Studio</li>
  <li>A valid API key from NewsAPI</li>
</ul>

### Installation
<ul>
  <li>Clone this repository.</li>
  <li>Open the project in your IDE (Android Studio).</li>
  <li>Insert your NewsAPI key into the designated location in the code.</li>
  <li>Build and run the app on your device or emulator.</li>
</ul>

## Built With
<ul>
  <li>[Kotlin] - Primary programming language.</li>
  <li>[Retrofit] - For network requests.</li>
  <li>[Room] - For local data storage.</li>
  <li>[Clean Architecture] - This project is structured using Clean Architecture principles to ensure separation of concerns, scalability, and easier maintenance.</li>
  <li>[Coroutine Flow] - Utilized Coroutine Flow for managing asynchronous data streams with lifecycle-aware components, enhancing the app's responsiveness and performance.</li>
  <li>[Koin] - A lightweight dependency injection framework, Koin is used for managing component dependencies, making the code more modular and testable.</li>
</ul>

## Contributing
We welcome contributions. If you would like to contribute, please fork the repository and submit a pull request.

## Authors
Gideon Panjaitan - *Initial Work* - [girendi](https://github.com/girendi)

## Acknowledgments
<ul>
  <li>NewsAPI for providing a comprehensive API for news aggregation.</li>
</ul>
