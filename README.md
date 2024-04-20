# Gamestudio Application

Gamestudio Application is a Spring Boot based game application. It includes game and allows players to post scores, ratings, and comments. Now, it has a console UI, but I'm going to implement web GUI soon.

## Features

- Play Slitherlink game
- Post scores
- Rate games
- Comment on games

## Technologies Used

- Java
- Spring Boot
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
   

### Prerequisites

- Java 17
- Maven

### Installation

1. Clone the repo - git clone https://github.com/BerserkChmonya/gamestudio.git
2. Navigate to the project directory - cd gamestudio
3. Build the project - mvn clean install
4. Run the application - mvn spring-boot:run


## Usage

After starting the application, follow the on-screen instructions to play the games and interact with the application.
!!! Rest services are available at http://localhost:8000/api. Double check for port!!!


## Slitherlink Game Rules

Slitherlink is a logic puzzle game. The game is played on a rectangular grid of dots. Some of the squares formed by the dots have numbers inside them.

The objective of the game is to connect horizontally and vertically adjacent dots so that the lines form a single loop with no loose ends. In addition, the number inside a square represents how many of its four sides are segments in the loop.

Here are the detailed rules:

1. Connect adjacent dots with vertical or horizontal lines to make a single loop.
2. The numbers indicate how many lines surround it, while empty cells may be surrounded by any number of lines.
3. The loop never crosses itself and never branches off.

Enjoy the game!