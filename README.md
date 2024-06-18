# Gamestudio Application

Gamestudio Application is a web-based game application built with Spring Boot. It includes a game called Slitherlink and allows players to post scores, rate games, and comment on games. The application features a simple user-friendly interface built with Thymeleaf, HTML, CSS, and JavaScript.

## Features

- Play the Slitherlink game in a web browser
- Post scores and see how you rank against other players
- Rate games and see the average rating from all players
- Comment on games and engage with the community
- User registration and login functionality

## Technologies Used

- Java
- Spring Boot
- Thymeleaf
- HTML
- CSS
- JavaScript
- JPA
- Maven
- PostgreSQL

## Getting Started

To get a local copy up and running follow these simple steps.

### Database Setup

This application uses PostgreSQL as its database. Follow these steps to set up the database:

1. Install PostgreSQL if you haven't already. You can download it from [here](https://www.postgresql.org/download/).
2. Create a new PostgreSQL database for the application.
3. Update the `application.properties` file in the `src/main/resources` directory with your database information. Here's an example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Prerequisites

- Java 17
- Maven

### Installation

1. Clone the repo - `git clone https://github.com/BerserkChmonya/gamestudio.git`
2. Navigate to the project directory - `cd gamestudio`
3. Build the project - `mvn clean install`
4. Run the application - `mvn spring-boot:run`

## Usage

After starting the server, open your web browser and navigate to `http://localhost:8000/` to start playing the game and interacting with the application. Gamestudio application main is for console playing.

## Slitherlink Game Rules

Slitherlink is a logic puzzle game. The game is played on a rectangular grid of dots. Some of the squares formed by the dots have numbers inside them.

The objective of the game is to connect horizontally and vertically adjacent dots so that the lines form a single loop with no loose ends. In addition, the number inside a square represents how many of its four sides are segments in the loop.

Here are the detailed rules:

1. Connect adjacent dots with vertical or horizontal lines to make a single loop.
2. The numbers indicate how many lines surround it, while empty cells may be surrounded by any number of lines.
3. The loop never crosses itself and never branches off.

Enjoy the game!
